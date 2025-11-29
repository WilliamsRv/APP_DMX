package com.dmx.mobile

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dmx.mobile.databinding.ActivityMainBinding
import com.dmx.mobile.ui.adapters.ViewPagerAdapter
import com.dmx.mobile.viewmodel.DMXViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val viewModel: DMXViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupViewPager()
        setupButtons()
        observeConnection()
        
        // Escanear dispositivos al iniciar
        viewModel.scanUSBDevices()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }
    
    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter
        
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.manual_control)
                1 -> getString(R.string.color_presets)
                2 -> getString(R.string.scenes)
                else -> "Tab $position"
            }
        }.attach()
    }
    
    private fun setupButtons() {
        binding.btnScanDevices.setOnClickListener {
            viewModel.scanUSBDevices()
            showDeviceSelectionDialog()
        }
        
        binding.btnDisconnect.setOnClickListener {
            viewModel.disconnect()
        }
        
        binding.btnBlackout.setOnClickListener {
            viewModel.blackout()
        }
    }
    
    private fun observeConnection() {
        viewModel.isConnected.observe(this) { connected ->
            binding.connectionStatus.text = if (connected) {
                getString(R.string.connected)
            } else {
                getString(R.string.disconnected)
            }
            
            binding.btnDisconnect.isEnabled = connected
        }
        
        viewModel.availableDevices.observe(this) { devices ->
            if (devices.isEmpty()) {
                binding.connectionStatus.text = getString(R.string.no_devices)
            }
        }
    }
    
    private fun showDeviceSelectionDialog() {
        val devices = viewModel.availableDevices.value ?: emptyList()
        
        if (devices.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle(R.string.no_devices)
                .setMessage("No se encontraron dispositivos USB-DMX. Conecta un adaptador USB-DMX (FTDI FT232R o compatible) y vuelve a escanear.")
                .setPositiveButton("OK", null)
                .show()
            return
        }
        
        val deviceNames = devices.map { driver ->
            "${driver.device.deviceName} - ${driver.device.manufacturerName ?: "Unknown"}"
        }.toTypedArray()
        
        AlertDialog.Builder(this)
            .setTitle(R.string.select_device)
            .setItems(deviceNames) { _, which ->
                viewModel.connectToDevice(devices[which])
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}
