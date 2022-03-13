package com.example.home.ui.fragment

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.common.base.BaseFragment
import com.example.common.decoration.SpacesItemDecoration
import com.example.home.R
import com.example.home.ui.adapter.GoodsListAdapter
import com.example.home.ui.constants.ActionType
import com.example.home.ui.state.HomeState
import com.example.home.ui.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_goods.*

class GoodsListFragment(var code: String): BaseFragment(R.layout.home_goods), MavericksView {
    private val viewModel: HomeViewModel by activityViewModel()

    private val goodsListAdapter by lazy { GoodsListAdapter(R.layout.home_goods_item, arrayListOf()) }
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
        goodsLayout.run {
            setOnLoadMoreListener {
                viewModel.loadMoreGoodsList(code)
            }
        }
        addStateChangeListener()
    }

    override fun initData() {
        viewModel.initGoodsList(code)
    }


    private fun addStateChangeListener() {
        viewModel.onEach(
            HomeState::goodsList,
            HomeState::goodsListFetchType,
            HomeState::currentPage,
            HomeState::totalPage
        ) { goodsList, goodsListFetchType, currentPage, totalPage ->
            run {
                when (goodsListFetchType) {
                    ActionType.INIT -> {
                        if (goodsList.isNotEmpty()) {
                            goodsListAdapter.setList(goodsList)
                        }
                    }
                    ActionType.LOADMORE -> {
                        if (goodsList.isNotEmpty()) {
                            goodsListAdapter.addData(goodsListAdapter.data.size, goodsList)
                        }
                        goodsLayout.run {
                            if (currentPage <= totalPage) finishLoadMore()
                            else finishLoadMoreWithNoMoreData()
                        }
                    }
                }
            }
        }
    }

    override fun invalidate() {}
}