package com.lucifer.cyclepager.adapter

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.NO_ID

import com.lucifer.cyclepager.util.CyclePositionUtil

abstract class CyclePagerAdapter<VH : RecyclerView.ViewHolder?> : RecyclerView.Adapter<VH>() {
    override fun getItemCount(): Int {
        return if (realItemCount > 1) realItemCount + 2 else realItemCount
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindRealViewHolder(holder, CyclePositionUtil.getRealPosition(position, realItemCount))
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: List<Any>) {
        super.onBindViewHolder(holder,
            CyclePositionUtil.getRealPosition(position, realItemCount),
            payloads)
    }

    override fun getItemViewType(position: Int): Int {
        return getRealItemViewType(CyclePositionUtil.getRealPosition(position, realItemCount))
    }

    override fun getItemId(position: Int): Long {
        return getRealItemId(CyclePositionUtil.getRealPosition(position, realItemCount))
    }

    abstract val realItemCount: Int

    abstract fun onBindRealViewHolder(holder: VH, position: Int)
    fun getRealItemViewType(position: Int): Int {
        return 0
    }

    fun getRealItemId(position: Int): Long {
        return NO_ID
    }
}
