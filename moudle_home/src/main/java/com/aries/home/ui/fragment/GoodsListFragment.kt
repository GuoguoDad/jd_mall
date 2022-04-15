package com.aries.home.ui.fragment

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.alibaba.android.arouter.launcher.ARouter
import com.aries.common.adapter.GoodsListAdapter
import com.aries.common.base.BaseFragment
import com.aries.common.constants.RouterPaths
import com.aries.common.decoration.SpacesItemDecoration
import com.aries.home.R
import com.aries.home.ui.constants.ActionType
import com.aries.home.ui.HomeState
import com.aries.home.ui.HomeViewModel
import kotlinx.android.synthetic.main.home_goods.*

class  GoodsListFragment(var code: String): BaseFragment(R.layout.home_goods), MavericksView {
    private val viewModel: HomeViewModel by activityViewModel()

    private val goodsListAdapter by lazy { GoodsListAdapter(arrayListOf()) }
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun initView() {
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE //解决加载下一页后重新排列的问题

        recyclerView.run {
            addItemDecoration(SpacesItemDecoration(10))
            adapter = goodsListAdapter
            layoutManager = staggeredGridLayoutManager
            setOnClickListener {  }
        }
        goodsListAdapter.loadMoreModule.setOnLoadMoreListener {
            viewModel.loadMoreGoodsList(code)
        }
        goodsListAdapter.setOnItemClickListener { adapter, view, position ->
            ARouter.getInstance().build(RouterPaths.GOODS_DETAIL).navigation()
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
                            goodsListAdapter.loadMoreModule.loadMoreComplete()
                        }
                    }
                    ActionType.LOADMORE -> {
                        if (goodsList.isNotEmpty()) {
                            goodsListAdapter.addData(goodsListAdapter.data.size, goodsList)
                        }
                        if (currentPage <= totalPage)
                            goodsListAdapter.loadMoreModule.loadMoreComplete()
                        else
                            goodsListAdapter.loadMoreModule.loadMoreEnd()
                    }
                }
            }
        }
    }

    override fun invalidate() {}
}