package com.example.news.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.databinding.FragmentHomeBinding
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshLayout: RefreshLayout
    private var dataList: MutableList<GoodsBean> = arrayListOf()
    var adapter = GoodsListAdapter(R.layout.fragment_home_recyclerview_item, dataList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        recyclerView = binding.recyclerView
        refreshLayout = binding.refreshLayout

        firstPageList()
        //下拉刷新
        refreshLayout.setRefreshHeader(ClassicsHeader(this.context))
        refreshLayout.setOnRefreshListener { layout ->
            run {
                getPageList(true, layout)
            }
        }
        //设置recyclerView的layoutManager和adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        adapter.addChildClickViewIds(R.id.home_recycle_list_item)
        adapter.setOnItemClickListener{ _, view, position ->
            if (view.id == R.id.home_recycle_list_item) {
                Toast.makeText(this.context, dataList[position].name, Toast.LENGTH_SHORT).show()
            }
        }
        recyclerView.adapter = adapter
        //上拉加载更多
        refreshLayout.setRefreshFooter(ClassicsFooter(this.context))
        refreshLayout.setOnLoadMoreListener { layout ->
            run {
                getPageList(false, layout)
            }
        }
        refreshLayout.setEnableAutoLoadMore(true)
    }

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
        adapter.setList(dataList)
    }

    private fun getPageList(isRefresh: Boolean, layout: RefreshLayout) {
        if (isRefresh) {
            dataList = arrayListOf()
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
        adapter.setList(dataList)
        if (isRefresh) {
            layout.finishRefresh()
        } else {
            layout.finishLoadMoreWithNoMoreData()
        }
    }
}