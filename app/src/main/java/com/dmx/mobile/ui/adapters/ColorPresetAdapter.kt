package com.dmx.mobile.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmx.mobile.databinding.ItemColorPresetBinding
import com.dmx.mobile.models.ColorPreset

class ColorPresetAdapter(
    private val onPresetClick: (ColorPreset) -> Unit
) : ListAdapter<ColorPreset, ColorPresetAdapter.ColorPresetViewHolder>(DiffCallback) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorPresetViewHolder {
        val binding = ItemColorPresetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ColorPresetViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ColorPresetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ColorPresetViewHolder(
        private val binding: ItemColorPresetBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(preset: ColorPreset) {
            binding.colorName.text = preset.name
            binding.colorPreview.setBackgroundColor(
                Color.rgb(preset.red, preset.green, preset.blue)
            )
            
            binding.root.setOnClickListener {
                onPresetClick(preset)
            }
        }
    }
    
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ColorPreset>() {
            override fun areItemsTheSame(oldItem: ColorPreset, newItem: ColorPreset): Boolean {
                return oldItem.name == newItem.name
            }
            
            override fun areContentsTheSame(oldItem: ColorPreset, newItem: ColorPreset): Boolean {
                return oldItem == newItem
            }
        }
    }
}
