package com.dmx.mobile.usb

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.util.Log
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import kotlinx.coroutines.*
import java.io.IOException

/**
 * Gestor de conexión USB para dispositivos DMX
 * Soporta FTDI FT232R y otros adaptadores USB-DMX
 */
class USBDMXManager(private val context: Context) {
    
    companion object {
        private const val TAG = "USBDMXManager"
        private const val ACTION_USB_PERMISSION = "com.dmx.mobile.USB_PERMISSION"
        private const val DMX_BAUD_RATE = 250000 // Velocidad estándar DMX512
        private const val DMX_DATA_BITS = 8
        private const val DMX_STOP_BITS = UsbSerialPort.STOPBITS_2
        private const val DMX_PARITY = UsbSerialPort.PARITY_NONE
        private const val DMX_BREAK_TIME = 100 // microsegundos
        private const val DMX_MAB_TIME = 12 // microsegundos (Mark After Break)
    }
    
    private val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
    private var usbSerialPort: UsbSerialPort? = null
    private var sendJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    var isConnected = false
        private set
    
    var onConnectionChanged: ((Boolean) -> Unit)? = null
    
    private val usbReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                ACTION_USB_PERMISSION -> {
                    synchronized(this) {
                        val device: UsbDevice? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            intent.getParcelableExtra(UsbManager.EXTRA_DEVICE, UsbDevice::class.java)
                        } else {
                            @Suppress("DEPRECATION")
                            intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
                        }
                        
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            device?.let { connectToDevice(it) }
                        } else {
                            Log.d(TAG, "Permiso USB denegado")
                        }
                    }
                }
                UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                    disconnect()
                }
            }
        }
    }
    
    init {
        val filter = IntentFilter().apply {
            addAction(ACTION_USB_PERMISSION)
            addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(usbReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            context.registerReceiver(usbReceiver, filter)
        }
    }
    
    /**
     * Busca dispositivos USB-DMX disponibles
     */
    fun findAvailableDevices(): List<UsbSerialDriver> {
        return UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
    }
    
    /**
     * Solicita conexión a un dispositivo
     */
    fun requestConnection(driver: UsbSerialDriver) {
        val device = driver.device
        if (usbManager.hasPermission(device)) {
            connectToDevice(device)
        } else {
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_MUTABLE
            } else {
                0
            }
            val permissionIntent = PendingIntent.getBroadcast(
                context,
                0,
                Intent(ACTION_USB_PERMISSION),
                flags
            )
            usbManager.requestPermission(device, permissionIntent)
        }
    }
    
    /**
     * Conecta al dispositivo USB
     */
    private fun connectToDevice(device: UsbDevice) {
        try {
            val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)
            val driver = availableDrivers.firstOrNull { it.device.deviceId == device.deviceId }
            
            if (driver == null) {
                Log.e(TAG, "No se encontró driver para el dispositivo")
                return
            }
            
            val connection = usbManager.openDevice(device)
            if (connection == null) {
                Log.e(TAG, "No se pudo abrir conexión USB")
                return
            }
            
            usbSerialPort = driver.ports[0].apply {
                open(connection)
                setParameters(DMX_BAUD_RATE, DMX_DATA_BITS, DMX_STOP_BITS, DMX_PARITY)
                
                // Configuración específica para FTDI
                dtr = false
                rts = false
            }
            
            isConnected = true
            onConnectionChanged?.invoke(true)
            Log.d(TAG, "Conectado a dispositivo DMX: ${device.deviceName}")
            
        } catch (e: IOException) {
            Log.e(TAG, "Error al conectar: ${e.message}", e)
            disconnect()
        }
    }
    
    /**
     * Desconecta del dispositivo
     */
    fun disconnect() {
        sendJob?.cancel()
        sendJob = null
        
        try {
            usbSerialPort?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error al cerrar puerto: ${e.message}")
        }
        
        usbSerialPort = null
        isConnected = false
        onConnectionChanged?.invoke(false)
        Log.d(TAG, "Desconectado del dispositivo DMX")
    }
    
    /**
     * Envía un paquete DMX
     */
    fun sendDMXPacket(data: ByteArray) {
        if (!isConnected || usbSerialPort == null) {
            Log.w(TAG, "No hay dispositivo conectado")
            return
        }
        
        try {
            usbSerialPort?.let { port ->
                // Enviar BREAK
                port.setBreak(true)
                Thread.sleep(0, DMX_BREAK_TIME * 1000)
                
                // MAB (Mark After Break)
                port.setBreak(false)
                Thread.sleep(0, DMX_MAB_TIME * 1000)
                
                // Enviar datos DMX
                port.write(data, 1000)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error al enviar DMX: ${e.message}", e)
            disconnect()
        } catch (e: InterruptedException) {
            Log.e(TAG, "Envío interrumpido", e)
        }
    }
    
    /**
     * Inicia envío continuo de DMX
     */
    fun startContinuousSend(dmxDataProvider: () -> ByteArray, updateRate: Long = 40) {
        sendJob?.cancel()
        
        sendJob = scope.launch {
            while (isActive && isConnected) {
                val data = dmxDataProvider()
                sendDMXPacket(data)
                delay(updateRate) // ~25 fps por defecto
            }
        }
    }
    
    /**
     * Detiene el envío continuo
     */
    fun stopContinuousSend() {
        sendJob?.cancel()
        sendJob = null
    }
    
    /**
     * Limpieza de recursos
     */
    fun cleanup() {
        disconnect()
        scope.cancel()
        try {
            context.unregisterReceiver(usbReceiver)
        } catch (e: IllegalArgumentException) {
            // Receiver ya no registrado
        }
    }
}
