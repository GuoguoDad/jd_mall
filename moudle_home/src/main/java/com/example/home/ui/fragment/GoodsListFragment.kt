package com.example.home.ui.fragment

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.common.base.BaseFragment
import com.example.common.decoration.SpacesItemDecoration
import com.example.home.R
import com.example.home.ui.GoodsBean
import com.example.home.ui.adapter.GoodsListAdapter
import kotlinx.android.synthetic.main.home_goods.*

class GoodsListFragment(data: MutableList<GoodsBean>): BaseFragment(R.layout.home_goods) {
    private val goodsListAdapter by lazy { GoodsListAdapter(R.layout.home_goods_item, data) }
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun initView() {
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE //解决加载下一页后重新排列的问题

        recyclerView.run {
            addItemDecoration(SpacesItemDecoration(10))
            adapter = goodsListAdapter
            layoutManager = staggeredGridLayoutManager
        }
    }

    override fun initData() {}
}