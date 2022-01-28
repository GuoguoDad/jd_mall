package com.example.main.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.main.R
import com.example.main.databinding.FragmentHomeBinding
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var dataList: MutableList<GoodsBean> = arrayListOf()
    private lateinit var adapter: GoodsListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initView()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.refreshLayout.removeAllViews()
        _binding = null
    }

    private fun initView() {
        adapter = GoodsListAdapter(R.layout.fragment_home_recyclerview_item, dataList)

        firstPageList()
        //下拉刷新
        binding.refreshLayout.setRefreshHeader(ClassicsHeader(this.context))
        binding.refreshLayout.setOnRefreshListener { layout ->
            run {
                getPageList(true, layout)
            }
        }
        //设置recyclerView的layoutManager和adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        adapter.addChildClickViewIds(R.id.home_recycle_list_item)
        adapter.setOnItemClickListener{ _, view, position ->
            if (view.id == R.id.home_recycle_list_item) {
                Toast.makeText(this.context, dataList[position].name, Toast.LENGTH_SHORT).show()
            }
        }
        binding.recyclerView.adapter = adapter
        //上拉加载更多
        binding.refreshLayout.setRefreshFooter(ClassicsFooter(this.context))
        binding.refreshLayout.setOnLoadMoreListener { layout ->
            run {
                getPageList(false, layout)
            }
        }
        binding.refreshLayout.setEnableAutoLoadMore(true)
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
}