package com.aries.mine.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aries.mine.ui.MenuBean
import com.aries.mine.ui.fragment.GridFragment
import kotlin.math.ceil

class FiveViewPagerAdapter(fragment: Fragment, var data: List<MenuBean>, var pageSize: Int): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return ceil(data.size * 1.0 / pageSize).toInt()
    }

    override fun createFragment(position: Int): Fragment {
        return GridFragment(data.toMutableList(), position, pageSize)
    }
}