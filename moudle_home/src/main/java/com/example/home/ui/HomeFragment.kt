package com.example.home.ui

import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.common.base.BaseFragment
import com.example.common.util.GsonUtil
import com.example.common.util.HttpUtil
import com.example.common.util.LoadingDialog
import com.example.home.R
import com.lucifer.cyclepager.util.Logger
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        fragmentHome.setEnableLoadMore(false)
//        //上拉加载更多
//        fragmentHome.setOnLoadMoreListener {
//            run {
//                viewModel.loadMore()
//            }
//        }
//        fragmentHome.setEnableAutoLoadMore(true)
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
//                            adapter.addData(adapter.data., it.newList)
//                            if (it.currentPage <= it.totalPage)
//                                fragmentHome.run {
//                                    finishLoadMore()
//                                }
//                            else
//                                fragmentHome.run {
//                                    finishLoadMoreWithNoMoreData()
//                                }
                        }
                    }
                }
            }

        }
    }
}