package com.lucifer.cyclepager.adapter

import androidx.fragment.app.Fragment
import com.lucifer.cyclepager.util.CyclePositionUtil
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.lifecycle.Lifecycle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView.NO_ID
import androidx.viewpager2.adapter.FragmentStateAdapter


abstract class CyclePagerFragmentAdapter : FragmentStateAdapter {
    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity) {}
    constructor(fragment: Fragment) : super(fragment) {}
    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(fragmentManager,
        lifecycle) {
    }

    override fun createFragment(position: Int): Fragment {
        return createRealFragment(CyclePositionUtil.getRealPosition(position, realItemCount))
    }

    override fun getItemCount(): Int {
        return if (realItemCount > 1) realItemCount + 2 else realItemCount
    }

    override fun getItemId(position: Int): Long {
        return getRealItemId(CyclePositionUtil.getRealPosition(position, realItemCount))
    }

    override fun onBindViewHolder(holder: FragmentViewHolder, position: Int, payloads: List<Any>) {
        super.onBindViewHolder(holder,
            CyclePositionUtil.getRealPosition(position, realItemCount),
            payloads)
    }

    override fun getItemViewType(position: Int): Int {
        return getRealItemViewType(CyclePositionUtil.getRealPosition(position, realItemCount))
    }

    abstract val realItemCount: Int

    abstract fun createRealFragment(position: Int): Fragment
    fun getRealItemViewType(position: Int): Int {
        return 0
    }

    fun getRealItemId(position: Int): Long {
        return NO_ID
    }
}
