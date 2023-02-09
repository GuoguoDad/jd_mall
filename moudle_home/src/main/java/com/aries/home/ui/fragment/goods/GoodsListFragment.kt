package com.aries.home.ui.fragment.goods

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.alibaba.android.arouter.launcher.ARouter
import com.aries.common.adapter.GoodsListAdapter
import com.aries.common.constants.RouterPaths
import com.aries.common.decoration.SpacesItemDecoration
import com.aries.home.databinding.HomeGoodsBinding

class  GoodsListFragment(var code: String): Fragment(), MavericksView {
    private lateinit var binding:  HomeGoodsBinding

    private val viewModel: GoodsViewModel by activityViewModel()

    private val goodsListAdapter by lazy { GoodsListAdapter(arrayListOf()) }
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = HomeGoodsBinding.inflate(LayoutInflater.from(this.context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    fun initView() {
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
        goodsListAdapter.setOnItemClickListener { adapter, view, position ->
            when (goodsListAdapter.data[position].type) {
                "1" -> ARouter.getInstance().build(RouterPaths.GOODS_DETAIL).navigation()
                "2" -> ARouter.getInstance().build(RouterPaths.WebView_ACTIVITY)
                    .withBoolean("isDarkTheme", false)
                    .withString("url","https://shop.m.jd.com/?shopId=17529")
                    .navigation()
            }
        }
    }

    fun initData() {
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