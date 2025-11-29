package com.dmx.mobile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmx.mobile.databinding.ItemSceneBinding
import com.dmx.mobile.models.Scene

class SceneAdapter(
    private val onLoadClick: (Scene) -> Unit,
    private val onDeleteClick: (Scene) -> Unit
) : ListAdapter<Scene, SceneAdapter.SceneViewHolder>(DiffCallback) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SceneViewHolder {
        val binding = ItemSceneBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SceneViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: SceneViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class SceneViewHolder(
        private val binding: ItemSceneBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(scene: Scene) {
            binding.sceneName.text = scene.name
            binding.sceneInfo.text = "Canales: ${scene.channelValues.size}"
            
            binding.btnLoadScene.setOnClickListener {
                onLoadClick(scene)
            }
            
            binding.btnDeleteScene.setOnClickListener {
                onDeleteClick(scene)
            }
        }
    }
    
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Scene>() {
            override fun areItemsTheSame(oldItem: Scene, newItem: Scene): Boolean {
                return oldItem.id == newItem.id
            }
            
            override fun areContentsTheSame(oldItem: Scene, newItem: Scene): Boolean {
                return oldItem == newItem
            }
        }
    }
}
