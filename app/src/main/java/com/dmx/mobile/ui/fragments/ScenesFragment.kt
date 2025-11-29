package com.dmx.mobile.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmx.mobile.databinding.FragmentScenesBinding
import com.dmx.mobile.ui.adapters.SceneAdapter
import com.dmx.mobile.viewmodel.DMXViewModel

class ScenesFragment : Fragment() {
    
    private var _binding: FragmentScenesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DMXViewModel by activityViewModels()
    private lateinit var adapter: SceneAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScenesBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupButtons()
        observeScenes()
    }
    
    private fun setupRecyclerView() {
        adapter = SceneAdapter(
            onLoadClick = { scene -> viewModel.loadScene(scene) },
            onDeleteClick = { scene -> viewModel.deleteScene(scene) }
        )
        
        binding.recyclerScenes.layoutManager = LinearLayoutManager(context)
        binding.recyclerScenes.adapter = adapter
    }
    
    private fun setupButtons() {
        binding.btnSaveScene.setOnClickListener {
            showSaveSceneDialog()
        }
    }
    
    private fun observeScenes() {
        viewModel.scenes.observe(viewLifecycleOwner) { scenes ->
            adapter.submitList(scenes)
        }
    }
    
    private fun showSaveSceneDialog() {
        val input = EditText(context)
        input.hint = "Nombre de la escena"
        
        AlertDialog.Builder(context)
            .setTitle("Guardar Escena")
            .setView(input)
            .setPositiveButton("Guardar") { _, _ ->
                val name = input.text.toString()
                if (name.isNotBlank()) {
                    viewModel.saveScene(name)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
