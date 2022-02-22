package com.example.home.ui

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.example.common.ui.BaseFragment
import com.example.home.R
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(R.layout.fragment_home), MavericksView {
    private var dataList: MutableList<GoodsBean> = arrayListOf()
    private lateinit var adapter: GoodsListAdapter

//    private val viewModel: GoodListViewModel by fragmentViewModel()

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentHome.removeAllViews()
    }

    override fun initView() {
        adapter = GoodsListAdapter(R.layout.fragment_home_recyclerview_item, dataList)

        firstPageList()
        //下拉刷新
        fragmentHome.setRefreshHeader(ClassicsHeader(this.context))
        fragmentHome.setOnRefreshListener { layout ->
            run {
                getPageList(true, layout)
            }
        }
        //设置recyclerView的layoutManager和adapter
        fragmentHomeRecycleView.layoutManager = LinearLayoutManager(this.context)
        adapter.addChildClickViewIds(R.id.home_recycle_list_item)
        adapter.setOnItemClickListener{ _, view, position ->
            if (view.id == R.id.home_recycle_list_item) {
                Toast.makeText(this.context, dataList[position].name, Toast.LENGTH_SHORT).show()
            }
        }
        fragmentHomeRecycleView.adapter = adapter
        //上拉加载更多
        fragmentHome.setRefreshFooter(ClassicsFooter(this.context))
        fragmentHome.setOnLoadMoreListener { layout ->
            run {
                getPageList(false, layout)
            }
        }
        fragmentHome.setEnableAutoLoadMore(true)
    }

    override fun initData() {
        firstPageList()
    }

    /**
     * 加载第一页数据
     */
    private fun firstPageList() {
        var goodsModel: GoodsBean
        for (i in 0..10) {
            goodsModel = GoodsBean(
                "https://oss.suning.com/sffe/sffe/default_goods.png",
                "苹果（Apple）iPhone 13 Pro max ${i}",
                "89999"
            )
            dataList.add(goodsModel)
        }
        adapter.addData(dataList)
    }

    /**
     * 分页加载数据
     */
    private fun getPageList(isRefresh: Boolean, layout: RefreshLayout) {
        if (isRefresh) {
            dataList.clear()
        }
        var goodsModel: GoodsBean
        for (i in 0..10) {
            goodsModel = GoodsBean(
                "https://oss.suning.com/sffe/sffe/default_goods.png",
                "苹果（Apple）iPhone 13 Pro max ${i}",
                "89999"
            )
            dataList.add(goodsModel)
        }
        adapter.addData(dataList)
        if (isRefresh) {
            layout.finishRefresh()
        } else {
            layout.finishLoadMoreWithNoMoreData()
        }
    }

    override fun invalidate() {
    }
}