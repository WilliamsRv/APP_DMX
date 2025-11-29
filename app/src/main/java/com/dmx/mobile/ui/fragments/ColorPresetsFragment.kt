package com.dmx.mobile.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dmx.mobile.databinding.FragmentColorPresetsBinding
import com.dmx.mobile.models.ColorPreset
import com.dmx.mobile.ui.adapters.ColorPresetAdapter
import com.dmx.mobile.viewmodel.DMXViewModel

class ColorPresetsFragment : Fragment() {
    
    private var _binding: FragmentColorPresetsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DMXViewModel by activityViewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentColorPresetsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }
    
    private fun setupRecyclerView() {
        val adapter = ColorPresetAdapter { preset ->
            // Aplicar preset al canal 1 (puedes hacer esto configurable)
            viewModel.applyColorPreset(1, preset, hasWhite = true)
        }
        
        binding.recyclerColorPresets.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerColorPresets.adapter = adapter
        adapter.submitList(ColorPreset.PRESETS)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
