package com.aries.home.ui.fragment

import android.widget.ListAdapter
import com.aries.common.base.BaseFragment
import com.example.home.R
import com.aries.home.ui.MenuBean
import com.aries.home.ui.adapter.NineGridAdapter
import kotlinx.android.synthetic.main.nine_memu_gridview.*

class GridFragment(data: MutableList<MenuBean>, index: Int, pageSize: Int):  BaseFragment(R.layout.nine_memu_gridview) {
    private val nineGridAdapter: NineGridAdapter by lazy { NineGridAdapter(this.requireContext(), data, index, pageSize) }

    override fun initView() {
        gridView.run {
            adapter = nineGridAdapter as ListAdapter
        }
    }

    override fun initData() {}
}