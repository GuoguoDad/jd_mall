package com.example.news.ui.waterfall

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.news.R
import com.example.news.kit.util.HttpUtil
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlinx.android.synthetic.main.layout_waterfall.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.*

class WaterfallListActivity: AppCompatActivity() {
    private var currentPage: Int = 1
    private var pageSize: Int = 11
    private var dataList: MutableList<GoodsBean> = arrayListOf()
    private val adapter = WaterfallListAdapter(R.layout.layout_waterfall_item, dataList)
    private val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    private val apiInstance = HttpUtil.instance.create(this).service(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_waterfall)

        initView()
    }

    private fun initView() {
        getPageList(true,1, null)
        //下拉刷新
        waterfallLayout.setRefreshHeader(ClassicsHeader(this))
        waterfallLayout.setOnRefreshListener { layout ->
            run {
                getPageList(true,1, layout)
            }
        }
        //瀑布列表
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        waterfall_recycler_view.layoutManager = staggeredGridLayoutManager
        //解决加载下一页后重新排列的问题
        waterfall_recycler_view.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                staggeredGridLayoutManager.invalidateSpanAssignments()
            }
        })
        adapter.setOnItemClickListener{_, view, position ->
            if (view.id == R.id.waterfallItemLayout) {
                Toast.makeText(this, dataList[position].name, Toast.LENGTH_SHORT).show()
            }
        }
        waterfall_recycler_view.adapter = adapter
        val space = resources.getDimension(R.dimen.waterfall_space)
        waterfall_recycler_view.addItemDecoration(SpacesItemDecoration(space.toInt()))

        //上拉加载更多
        waterfallLayout.setRefreshFooter(ClassicsFooter(this))
        waterfallLayout.setOnLoadMoreListener { layout ->
            run {
                getPageList(false, currentPage + 1, layout)
            }
        }
        waterfallLayout.setEnableAutoLoadMore(true)
    }

    /**
     * 分页加载数据
     */
    private fun getPageList(isRefresh: Boolean, pageNo: Int, layout: RefreshLayout?) {
        if (isRefresh) {
            dataList = arrayListOf()
        }
        CoroutineScope(Dispatchers.IO).launch {
            val result = apiInstance.queryProductListByPage(QueryProductListParams(pageNo, pageSize)).await()
            for (bean in result.data.dataList) {
                dataList.add(bean)
            }
            runOnUiThread {
                adapter.setList(dataList)
                currentPage = if (isRefresh) 1 else pageNo
                if (pageNo < result.data.totalPageCount ) {
                    layout?.finishLoadMore()
                }else{
                    layout?.finishLoadMoreWithNoMoreData()
                }
            }
        }
    }
}
