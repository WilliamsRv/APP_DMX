package com.dmx.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dmx.mobile.databinding.FragmentManualControlBinding
import com.dmx.mobile.viewmodel.DMXViewModel

class ManualControlFragment : Fragment() {
    
    private var _binding: FragmentManualControlBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DMXViewModel by activityViewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManualControlBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRGBControls()
        setupChannelControl()
    }
    
    private fun setupRGBControls() {
        binding.sliderRed.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                binding.tvRedValue.text = value.toInt().toString()
                updateRGB()
            }
        }
        
        binding.sliderGreen.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                binding.tvGreenValue.text = value.toInt().toString()
                updateRGB()
            }
        }
        
        binding.sliderBlue.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                binding.tvBlueValue.text = value.toInt().toString()
                updateRGB()
            }
        }
        
        binding.sliderWhite.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                binding.tvWhiteValue.text = value.toInt().toString()
                updateRGB()
            }
        }
    }
    
    private fun updateRGB() {
        val startChannel = binding.etStartChannel.text.toString().toIntOrNull() ?: 1
        val red = binding.sliderRed.value.toInt()
        val green = binding.sliderGreen.value.toInt()
        val blue = binding.sliderBlue.value.toInt()
        val white = binding.sliderWhite.value.toInt()
        
        viewModel.setRGBW(startChannel, red, green, blue, white)
    }
    
    private fun setupChannelControl() {
        binding.sliderChannel.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                binding.etValue.setText(value.toInt().toString())
                updateChannel()
            }
        }
        
        binding.etChannel.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                updateChannelSlider()
            }
        }
        
        binding.etValue.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.sliderChannel.value = binding.etValue.text.toString().toFloatOrNull() ?: 0f
                updateChannel()
            }
        }
    }
    
    private fun updateChannel() {
        val channel = binding.etChannel.text.toString().toIntOrNull() ?: 1
        val value = binding.etValue.text.toString().toIntOrNull() ?: 0
        viewModel.setChannelValue(channel, value)
    }
    
    private fun updateChannelSlider() {
        val channel = binding.etChannel.text.toString().toIntOrNull() ?: 1
        val currentValue = viewModel.getChannelValue(channel)
        binding.etValue.setText(currentValue.toString())
        binding.sliderChannel.value = currentValue.toFloat()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
