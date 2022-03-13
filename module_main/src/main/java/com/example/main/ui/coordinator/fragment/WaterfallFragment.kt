package com.example.main.ui.coordinator.fragment

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.common.base.BaseFragment
import com.example.common.decoration.SpacesItemDecoration
import com.example.common.dialog.PreviewPictureDialog
import com.example.main.R
import com.example.main.ui.coordinator.viewmodel.ScrollTabViewModel
import com.example.main.ui.waterfall.WaterfallListAdapter
import kotlinx.android.synthetic.main.view_list.*

class WaterfallFragment: BaseFragment(R.layout.view_list), MavericksView {
    private val adapter: WaterfallListAdapter by lazy {
        WaterfallListAdapter(R.layout.layout_waterfall_item, arrayListOf())
    }
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }
    private val previewPicture: PreviewPictureDialog by lazy { PreviewPictureDialog(this.requireActivity()) }

    private val viewModel: ScrollTabViewModel by activityViewModel()

    override fun initView() {
        recyclerViewContainer.setEnableRefresh(false)
        recyclerViewContainer.setOnLoadMoreListener {
            viewModel.queryProductListByPage(false)
        }
        //瀑布列表
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE //解决加载下一页后重新排列的问题
        recyclerView.layoutManager = staggeredGridLayoutManager

        recyclerView.adapter = adapter
        adapter.setOnItemClickListener{_, view, position ->
            if (view.id == R.id.waterfallItemLayout) {
                val imgUrls = adapter.data.map { v-> v.thumb }
                previewPicture.show(imgUrls, position)
            }
        }
        val space = resources.getDimension(R.dimen.waterfall_space)
        recyclerView.addItemDecoration(SpacesItemDecoration(space.toInt()))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerViewContainer.removeAllViews()
    }

    override fun initData() {
        viewModel.queryProductListByPage(true)
    }

    override fun invalidate() {
        withState(viewModel) {
            when (it.isLoading) {
                true -> {}
                false -> {
                    when (it.actionType) {
                        "init" -> {
                            if (it.GoodsList.isNotEmpty()) {
                                adapter.setList(it.GoodsList)
                            }
                        }
                        "loadMore" -> {
                            if (it.GoodsList.isNotEmpty()) {
                                adapter.addData(adapter.data.size ,it.GoodsList)
                                if (it.currentPage <= it.totalPage)
                                    recyclerViewContainer.run {
                                        finishLoadMore()
                                    }
                                else
                                    recyclerViewContainer.run {
                                        finishLoadMoreWithNoMoreData()
                                    }
                            }
                        }
                    }

                }
            }
        }
    }
}