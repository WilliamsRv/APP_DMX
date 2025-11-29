package com.dmx.mobile.dmx

/**
 * Controlador DMX512
 * Maneja la comunicación con dispositivos DMX a través de USB
 */
class DMXController {
    companion object {
        const val DMX_CHANNELS = 512
        const val DMX_PACKET_SIZE = 513 // Start byte + 512 canales
    }
    
    // Buffer DMX: primer byte es START CODE (0), luego 512 canales
    private val dmxBuffer = ByteArray(DMX_PACKET_SIZE) { 0 }
    
    init {
        dmxBuffer[0] = 0 // DMX512 Start Code
    }
    
    /**
     * Establece el valor de un canal DMX
     * @param channel Canal DMX (1-512)
     * @param value Valor (0-255)
     */
    fun setChannel(channel: Int, value: Int) {
        if (channel in 1..DMX_CHANNELS) {
            dmxBuffer[channel] = value.toByte()
        }
    }
    
    /**
     * Obtiene el valor de un canal DMX
     * @param channel Canal DMX (1-512)
     * @return Valor del canal (0-255)
     */
    fun getChannel(channel: Int): Int {
        return if (channel in 1..DMX_CHANNELS) {
            dmxBuffer[channel].toInt() and 0xFF
        } else {
            0
        }
    }
    
    /**
     * Establece múltiples canales consecutivos
     * @param startChannel Canal inicial (1-512)
     * @param values Array de valores
     */
    fun setChannels(startChannel: Int, values: IntArray) {
        values.forEachIndexed { index, value ->
            val channel = startChannel + index
            if (channel in 1..DMX_CHANNELS) {
                dmxBuffer[channel] = value.toByte()
            }
        }
    }
    
    /**
     * Establece todos los canales a 0 (blackout)
     */
    fun blackout() {
        for (i in 1..DMX_CHANNELS) {
            dmxBuffer[i] = 0
        }
    }
    
    /**
     * Obtiene el buffer DMX completo para enviar
     */
    fun getDMXPacket(): ByteArray {
        return dmxBuffer.copyOf()
    }
    
    /**
     * Establece el dimmer de una fixture RGB
     * @param startChannel Canal inicial de la fixture
     * @param red Valor rojo (0-255)
     * @param green Valor verde (0-255)
     * @param blue Valor azul (0-255)
     */
    fun setRGB(startChannel: Int, red: Int, green: Int, blue: Int) {
        setChannels(startChannel, intArrayOf(red, green, blue))
    }
    
    /**
     * Establece una fixture RGBW
     */
    fun setRGBW(startChannel: Int, red: Int, green: Int, blue: Int, white: Int) {
        setChannels(startChannel, intArrayOf(red, green, blue, white))
    }
}
