package com.aries.home.ui.fragment.goods

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.alibaba.android.arouter.launcher.ARouter
import com.aries.common.adapter.GoodsListAdapter
import com.aries.common.base.BaseFragment
import com.aries.common.constants.RouterPaths
import com.aries.common.decoration.SpacesItemDecoration
import com.aries.home.databinding.HomeGoodsBinding
import com.orhanobut.logger.Logger

class  GoodsListFragment(): BaseFragment<HomeGoodsBinding>(), MavericksView {
    private val viewModel: GoodsViewModel by activityViewModel()
    private lateinit var code: String

    private val goodsListAdapter by lazy { GoodsListAdapter(arrayListOf()) }
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): HomeGoodsBinding {
        return HomeGoodsBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        code = arguments?.getString("code").toString()
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE //解决加载下一页后重新排列的问题

        binding.recyclerView.run {
            addItemDecoration(SpacesItemDecoration(10))
            adapter = goodsListAdapter
            layoutManager = staggeredGridLayoutManager
            setOnClickListener {  }
        }
        goodsListAdapter.loadMoreModule.setOnLoadMoreListener {
            viewModel.loadMoreGoodsList(code)
        }
        goodsListAdapter.setOnItemClickListener { _, _, position ->
            when (goodsListAdapter.data[position].type) {
                "1" -> ARouter.getInstance().build(RouterPaths.GOODS_DETAIL).navigation()
                "2" -> ARouter.getInstance().build(RouterPaths.WebView_ACTIVITY)
                    .withBoolean("isDarkTheme", false)
                    .withString("url","https://shop.m.jd.com/?shopId=17529")
                    .navigation()
            }
        }
    }

    override fun initData() {
        viewModel.initGoodsList(code)
    }

    override fun invalidate() {
        withState(viewModel) {
            if (it.goodsList.isNotEmpty()) {
                goodsListAdapter.setList(it.goodsList)
                goodsListAdapter.loadMoreModule.loadMoreComplete()
            }
            if (it.nextPageGoodsList.isNotEmpty()) {
                goodsListAdapter.addData(goodsListAdapter.data.size, it.goodsList)

                if (it.currentPage <= it.totalPage)
                    goodsListAdapter.loadMoreModule.loadMoreComplete()
                else
                    goodsListAdapter.loadMoreModule.loadMoreEnd()
            }
        }
    }
}