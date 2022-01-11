package com.example.news.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.news.R
import com.example.news.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private var dataList: MutableList<GoodsModel> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerView = binding.recyclerView

        initRecycleView()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun initRecycleView() {
        var goodsModel: GoodsModel
        for (i in 1..20) {
            goodsModel = GoodsModel(
                "https://oss.suning.com/sffe/sffe/default_goods.png",
                "苹果（Apple）iPhone 13 Pro max ${i}",
                "89999"
            )
            dataList.add(goodsModel)
        }

        recyclerView.layoutManager = LinearLayoutManager(this.context)
        var adapter = GoodsListAdapter(R.layout.fragment_home_recyclerview_item, dataList)
        adapter.addChildClickViewIds(R.id.home_recycle_list_item)
        adapter.setOnItemClickListener{
                adapter, view, position ->
            if (view.id == R.id.home_recycle_list_item) {
                Toast.makeText(this.context, dataList[position].name, Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView.adapter = adapter
    }
}