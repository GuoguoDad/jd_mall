package com.example.main.ui.waterfall

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.common.constants.RouterPaths
import com.example.common.ui.BaseActivity
import com.example.common.util.HttpUtil
import com.example.main.R
import com.example.main.kit.util.LoadingDialog
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlinx.android.synthetic.main.layout_header.*
import kotlinx.android.synthetic.main.layout_waterfall.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.*
import java.lang.reflect.Method

@Route(path = RouterPaths.WATERFALL_ACTIVITY)
class WaterfallListActivity: BaseActivity(R.layout.layout_waterfall) {
    private var currentPage: Int = 1
    private var pageSize: Int = 11
    private lateinit var mCheckForGapMethod: Method
    private lateinit var mMarkItemDecorInsetsDirtyMethod: Method
    private var dataList: MutableList<GoodsBean> = arrayListOf()
    private lateinit var adapter: WaterfallListAdapter
    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager
    private lateinit var apiInstance: ApiService
    private lateinit var loadingDialog: LoadingDialog

    override fun initView() {
        apiInstance = HttpUtil.instance.service(ApiService::class.java)
        loadingDialog = LoadingDialog(this)

        //返回
        leftText.setOnClickListener {
            finish()
        }

        //下拉刷新
        waterfallLayout.setRefreshHeader(ClassicsHeader(this))
        waterfallLayout.setOnRefreshListener { layout ->
            run {
                getPageList(true,1, layout)
            }
        }

        //瀑布列表
        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE //解决加载下一页后重新排列的问题
        waterfall_recycler_view.layoutManager = staggeredGridLayoutManager
        waterfall_recycler_view.itemAnimator = null

        mCheckForGapMethod = StaggeredGridLayoutManager::class.java.getDeclaredMethod("checkForGaps")
        mMarkItemDecorInsetsDirtyMethod = RecyclerView::class.java.getDeclaredMethod("markItemDecorInsetsDirty")
        mCheckForGapMethod.isAccessible = true
        mMarkItemDecorInsetsDirtyMethod.isAccessible = true

        waterfall_recycler_view.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val result = mCheckForGapMethod.invoke(waterfall_recycler_view.layoutManager) as Boolean
                //如果发生了重新排序，刷新itemDecoration
                if(result) {
                    mMarkItemDecorInsetsDirtyMethod.invoke(waterfall_recycler_view)
                }
            }
        })

        adapter = WaterfallListAdapter(R.layout.layout_waterfall_item, dataList)
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

    override fun initData() {
        getPageList(true,1, null)
    }

    /**
     * 分页加载数据
     */
    private fun getPageList(isRefresh: Boolean, pageNo: Int, layout: RefreshLayout?) {
        loadingDialog.show()
        CoroutineScope(Dispatchers.IO).launch {
            val result = apiInstance.queryProductListByPage(QueryProductListParams(pageNo, pageSize))
            loadingDialog.dismiss()

            val list = result.data.dataList
            val lastIndex = dataList.size
            if (isRefresh) {
                dataList.clear()
            }
            dataList.addAll(list)

            runOnUiThread {
                currentPage = if (isRefresh) 1 else pageNo
                if (isRefresh) {
                    adapter.setList(dataList)
                    layout?.finishRefresh()
                } else {
                    adapter.addData(lastIndex, list)
                    if (pageNo < result.data.totalPageCount ) {
                        layout?.finishLoadMore()
                    }else{
                        layout?.finishLoadMoreWithNoMoreData()
                    }
                }
            }
        }
    }
}
