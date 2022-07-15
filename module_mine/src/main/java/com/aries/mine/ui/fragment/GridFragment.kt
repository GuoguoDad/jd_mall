package com.aries.mine.ui.fragment

import android.widget.ListAdapter
import com.aries.common.base.BaseFragment
import com.aries.mine.R
import com.aries.mine.ui.MenuBean
import com.aries.mine.ui.adapter.FiveGridAdapter
import kotlinx.android.synthetic.main.five_menu_gridview.*

class GridFragment(data: MutableList<MenuBean>, index: Int, pageSize: Int):  BaseFragment(R.layout.five_menu_gridview) {
    private val nineGridAdapter: FiveGridAdapter by lazy { FiveGridAdapter(this.requireContext(), data, index, pageSize) }

    override fun initView() {
        gridView.run {
            adapter = nineGridAdapter as ListAdapter
        }
    }

    override fun initData() {}
}