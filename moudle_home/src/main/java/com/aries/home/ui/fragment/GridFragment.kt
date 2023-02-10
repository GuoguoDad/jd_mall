package com.aries.home.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import com.aries.common.base.BaseFragment
import com.aries.home.databinding.NineMemuGridviewBinding
import com.aries.home.ui.MenuBean
import com.aries.home.ui.adapter.NineGridAdapter

class GridFragment(data: MutableList<MenuBean>, index: Int, pageSize: Int): BaseFragment<NineMemuGridviewBinding>() {
    private val nineGridAdapter: NineGridAdapter by lazy { NineGridAdapter(this.requireContext(), data, index, pageSize) }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): NineMemuGridviewBinding {
        return NineMemuGridviewBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding.gridView.run {
            adapter = nineGridAdapter as ListAdapter
        }
    }

    override fun initData() {}
}