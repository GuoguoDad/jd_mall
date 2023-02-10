package com.aries.mine.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import com.aries.common.base.BaseFragment
import com.aries.mine.databinding.FiveMenuGridviewBinding
import com.aries.mine.ui.MenuBean
import com.aries.mine.ui.adapter.FiveGridAdapter

class GridFragment(data: MutableList<MenuBean>, index: Int, pageSize: Int): BaseFragment<FiveMenuGridviewBinding>() {
    private val nineGridAdapter: FiveGridAdapter by lazy { FiveGridAdapter(this.requireContext(), data, index, pageSize) }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FiveMenuGridviewBinding {
        return FiveMenuGridviewBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding.gridView.run {
            adapter = nineGridAdapter as ListAdapter
        }
    }

    override fun initData() { }
}