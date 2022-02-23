package com.example.home.ui

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.common.base.BaseFragment
import com.example.common.util.LoadingDialog
import com.example.home.R
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(R.layout.fragment_home), MavericksView {
    private var dataList: MutableList<GoodsBean> = arrayListOf()
    private lateinit var refreshLayout: RefreshLayout
    private lateinit var loadMoreLayout: RefreshLayout
    private lateinit var loadingDialog: LoadingDialog

    private val viewModel: GoodListViewModel by fragmentViewModel()
    private val adapter: GoodsListAdapter by lazy {
        GoodsListAdapter(R.layout.fragment_home_recyclerview_item, dataList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentHome.removeAllViews()
    }

    override fun initView() {
        loadingDialog = LoadingDialog(this.requireActivity())
        addLoadingListener()
        //下拉刷新
        fragmentHome.setRefreshHeader(ClassicsHeader(this.context))
        fragmentHome.setOnRefreshListener { layout ->
            run {
                refreshLayout = layout
                viewModel.refresh(true)
            }
        }
        //设置recyclerView的layoutManager和adapter
        fragmentHomeRecycleView.layoutManager = LinearLayoutManager(this.context)
        adapter.addChildClickViewIds(R.id.home_recycle_list_item)
        adapter.setOnItemClickListener{ _, view, position ->
            if (view.id == R.id.home_recycle_list_item) {
                Toast.makeText(this.context, adapter.data[position].name, Toast.LENGTH_SHORT).show()
            }
        }
        fragmentHomeRecycleView.adapter = adapter
        //上拉加载更多
        fragmentHome.setRefreshFooter(ClassicsFooter(this.context))
        fragmentHome.setOnLoadMoreListener { layout ->
            run {
                loadMoreLayout = layout
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
            when (it.fetchType) {
                ActionType.INIT -> {
                    dataList.addAll(it.dataList)
                    adapter.setList(it.dataList)
                }
                ActionType.REFRESH -> {
                    dataList.clear()
                    dataList.addAll(it.dataList)
                    adapter.setList(it.dataList)
                    refreshLayout.finishRefresh()
                }
                ActionType.LOADMORE -> {
                    dataList.addAll(it.newList)
                    adapter.addData(it.dataList.size, it.newList)
                    adapter.notifyDataSetChanged()
                    if (it.currentPage <= it.totalPage)
                        loadMoreLayout.finishLoadMore()
                    else
                        loadMoreLayout.finishLoadMoreWithNoMoreData()
                }
            }
        }
    }

    private fun addLoadingListener() {
        viewModel.onEach(GoodListState::isLoading) {
           if (it)
               loadingDialog.show()
           else
               loadingDialog.dismiss()
        }
    }
}