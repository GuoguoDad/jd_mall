package com.example.news.ui.waterfall

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.news.R
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlinx.android.synthetic.main.layout_waterfall.*

class WaterfallListActivity: Activity() {
    private var dataList: MutableList<ProductBean> = arrayListOf()
    private val adapter = WaterfallListAdapter(R.layout.layout_waterfall_item, dataList)

    private var imgList: ArrayList<Int> = arrayListOf(
        R.drawable.waterfall01,R.drawable.swaterfall02, R.drawable.waterfall03, R.drawable.waterfall05,
        R.drawable.waterfall06, R.drawable.waterfall07, R.drawable.waterfall08, R.drawable.waterfall09,
        R.drawable.waterfall10, R.drawable.waterfall11, R.drawable.waterfall12, R.drawable.waterfall13,
        R.drawable.waterfall14, R.drawable.waterfall15, R.drawable.waterfall16, R.drawable.waterfall17,
        R.drawable.waterfall18, R.drawable.waterfall19, R.drawable.waterfall20, R.drawable.waterfall21,
        R.drawable.waterfall22)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_waterfall)

        getFirstPageList()
        initView()
    }

    private fun initView() {
        //下拉刷新
        waterfallLayout.setRefreshHeader(ClassicsHeader(this))
        waterfallLayout.setOnRefreshListener { layout ->
            run {
                getPageList(true, layout)
            }
        }
        //瀑布列表
        waterfall_recycler_view.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter.setOnItemClickListener{_, view, position ->
            if (view.id == R.id.waterfallItemLayout) {
                Toast.makeText(this, dataList[position].title, Toast.LENGTH_SHORT).show()
            }
        }
        waterfall_recycler_view.adapter = adapter
        val space = resources.getDimension(R.dimen.waterfall_space)
        waterfall_recycler_view.addItemDecoration(SpacesItemDecoration(space.toInt()))

        //上拉加载更多
        waterfallLayout.setRefreshFooter(ClassicsFooter(this))
        waterfallLayout.setOnLoadMoreListener { layout ->
            run {
                getPageList(false, layout)
            }
        }
        waterfallLayout.setEnableAutoLoadMore(true)
    }

    /**
     * 加载第一页数据
     */
    private fun getFirstPageList() {
        for(img in imgList) {
            dataList.add(ProductBean(img, R.string.waterfall_item_name))
        }
        adapter.setList(dataList)
    }

    /**
     * 分页加载数据
     */
    private fun getPageList(isRefresh: Boolean, layout: RefreshLayout) {
        if (isRefresh) {
            dataList = arrayListOf()
        }
        for(img in imgList) {
            dataList.add(ProductBean(img, R.string.waterfall_item_name))
        }
        adapter.setList(dataList)
        if (isRefresh) {
            layout.finishRefresh()
        } else {
            layout.finishLoadMoreWithNoMoreData()
        }
    }
}
