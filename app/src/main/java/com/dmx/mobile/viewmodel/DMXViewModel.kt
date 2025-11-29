package com.dmx.mobile.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dmx.mobile.dmx.DMXController
import com.dmx.mobile.models.ColorPreset
import com.dmx.mobile.models.Fixture
import com.dmx.mobile.models.Scene
import com.dmx.mobile.usb.USBDMXManager
import com.hoho.android.usbserial.driver.UsbSerialDriver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class DMXViewModel(application: Application) : AndroidViewModel(application) {
    
    private val dmxController = DMXController()
    private val usbManager = USBDMXManager(application)
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    
    private val _isConnected = MutableLiveData(false)
    val isConnected: LiveData<Boolean> = _isConnected
    
    private val _availableDevices = MutableLiveData<List<UsbSerialDriver>>(emptyList())
    val availableDevices: LiveData<List<UsbSerialDriver>> = _availableDevices
    
    private val _fixtures = MutableLiveData<List<Fixture>>(emptyList())
    val fixtures: LiveData<List<Fixture>> = _fixtures
    
    private val _scenes = MutableLiveData<List<Scene>>(emptyList())
    val scenes: LiveData<List<Scene>> = _scenes
    
    init {
        usbManager.onConnectionChanged = { connected ->
            _isConnected.postValue(connected)
            if (connected) {
                startDMXTransmission()
            } else {
                stopDMXTransmission()
            }
        }
    }
    
    fun scanUSBDevices() {
        val devices = usbManager.findAvailableDevices()
        _availableDevices.value = devices
    }
    
    fun connectToDevice(driver: UsbSerialDriver) {
        usbManager.requestConnection(driver)
    }
    
    fun disconnect() {
        usbManager.disconnect()
    }
    
    fun setChannelValue(channel: Int, value: Int) {
        dmxController.setChannel(channel, value)
    }
    
    fun getChannelValue(channel: Int): Int {
        return dmxController.getChannel(channel)
    }
    
    fun setRGB(startChannel: Int, red: Int, green: Int, blue: Int) {
        dmxController.setRGB(startChannel, red, green, blue)
    }
    
    fun setRGBW(startChannel: Int, red: Int, green: Int, blue: Int, white: Int) {
        dmxController.setRGBW(startChannel, red, green, blue, white)
    }
    
    fun applyColorPreset(startChannel: Int, preset: ColorPreset, hasWhite: Boolean = false) {
        if (hasWhite) {
            setRGBW(startChannel, preset.red, preset.green, preset.blue, preset.white)
        } else {
            setRGB(startChannel, preset.red, preset.green, preset.blue)
        }
    }
    
    fun blackout() {
        dmxController.blackout()
    }
    
    fun addFixture(fixture: Fixture) {
        val currentList = _fixtures.value.orEmpty().toMutableList()
        currentList.add(fixture)
        _fixtures.value = currentList
    }
    
    fun removeFixture(fixture: Fixture) {
        val currentList = _fixtures.value.orEmpty().toMutableList()
        currentList.remove(fixture)
        _fixtures.value = currentList
    }
    
    fun saveScene(name: String) {
        val channelValues = mutableMapOf<Int, Int>()
        for (i in 1..DMXController.DMX_CHANNELS) {
            val value = dmxController.getChannel(i)
            if (value > 0) {
                channelValues[i] = value
            }
        }
        
        val currentScenes = _scenes.value.orEmpty().toMutableList()
        val newScene = Scene(
            id = currentScenes.size + 1,
            name = name,
            channelValues = channelValues
        )
        currentScenes.add(newScene)
        _scenes.value = currentScenes
    }
    
    fun loadScene(scene: Scene) {
        dmxController.blackout()
        scene.channelValues.forEach { (channel, value) ->
            dmxController.setChannel(channel, value)
        }
    }
    
    fun deleteScene(scene: Scene) {
        val currentScenes = _scenes.value.orEmpty().toMutableList()
        currentScenes.remove(scene)
        _scenes.value = currentScenes
    }
    
    private fun startDMXTransmission() {
        usbManager.startContinuousSend(
            dmxDataProvider = { dmxController.getDMXPacket() },
            updateRate = 40 // 25 fps
        )
    }
    
    private fun stopDMXTransmission() {
        usbManager.stopContinuousSend()
    }
    
    override fun onCleared() {
        super.onCleared()
        usbManager.cleanup()
        scope.cancel()
    }
}
