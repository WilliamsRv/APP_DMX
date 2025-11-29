package com.dmx.mobile.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dmx.mobile.ui.fragments.ColorPresetsFragment
import com.dmx.mobile.ui.fragments.ManualControlFragment
import com.dmx.mobile.ui.fragments.ScenesFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    
    override fun getItemCount(): Int = 3
    
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ManualControlFragment()
            1 -> ColorPresetsFragment()
            2 -> ScenesFragment()
            else -> ManualControlFragment()
        }
    }
}
