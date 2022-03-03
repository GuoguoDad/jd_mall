package com.example.home.ui

import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.common.base.BaseFragment
import com.example.common.dialog.LoadingDialog
import com.example.home.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(R.layout.fragment_home), MavericksView {
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this.requireActivity())
    }

    private val viewModel: HomeViewModel by activityViewModel()
    private val adapter: HomeAdapter by lazy { HomeAdapter() }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentHome.removeAllViews()
    }

    override fun initView() {
        //下拉刷新
        fragmentHome.setOnRefreshListener {
            run {
                viewModel.refresh(true)
            }
        }
        //设置recyclerView的layoutManager和adapter
        fragmentHomeRecycleView.layoutManager = LinearLayoutManager(this.context)
        fragmentHomeRecycleView.adapter = adapter

        //上拉加载更多
        fragmentHome.setOnLoadMoreListener {
            run {
                viewModel.loadMore()
            }
        }
        fragmentHome.setEnableAutoLoadMore(true)
    }

    override fun initData() {
        viewModel.refresh(false)
    }

    override fun invalidate() {
        withState(viewModel) {
            when (it.isLoading) {
                true -> loadingDialog.show()
                false -> {
                    loadingDialog.dismiss()

                    when (it.fetchType) {
                        ActionType.INIT -> {
                            adapter.setData(it.dataList)
                        }
                        ActionType.REFRESH -> {
                            adapter.setData(it.dataList)
                            fragmentHome.run {
                                finishRefresh()
                            }
                        }
                        ActionType.LOADMORE -> {
                            var list = it.newList.filter { item -> !item.type.equals("1") }
                            adapter.addData(list)
                            if (it.currentPage <= it.totalPage)
                                fragmentHome.run {
                                    finishLoadMore()
                                }
                            else
                                fragmentHome.run {
                                    finishLoadMoreWithNoMoreData()
                                }
                        }
                    }
                }
            }

        }
    }
}