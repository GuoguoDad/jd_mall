package com.example.news.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.fastjson.JSON
import com.example.news.R
import com.example.news.databinding.FragmentDashboardBinding
import com.example.news.ui.dashboard.adapter.ModuleSelectionAdapter
import com.example.news.ui.dashboard.bean.ModuleBean
import com.example.news.ui.dashboard.bean.ModuleBeanWrapper
import com.example.news.ui.dashboard.decor.ItemOffsetDecoration
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder

class DashboardFragment : Fragment() {
    val titleItem = 0
    val galleryItem = 1
    private var _binding: FragmentDashboardBinding? = null
    private var recyclerView: RecyclerView? = null
    private var moduleSelectionAdapter: ModuleSelectionAdapter? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerView = binding.rvModule

        initData()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initData() {
        val localThis = this
        val wrapper = initListData()
        if (wrapper != null) {
            moduleSelectionAdapter = ModuleSelectionAdapter(wrapper)
            moduleSelectionAdapter!!.setOnItemClickListener(object : ModuleSelectionAdapter.OnItemClickListener {
                override fun onClick(view: View?, bean: ModuleBean?) {
                    Toast.makeText(localThis.context ,bean?.name, Toast.LENGTH_SHORT).show()
                }
            })
            val manager = GridLayoutManager(this.context, 6, GridLayoutManager.VERTICAL, false)
            manager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (moduleSelectionAdapter!!.getItemViewType(position)) {
                        titleItem -> 6
                        galleryItem -> 2
                        else -> 5
                    }
                }
            }
            recyclerView!!.adapter = moduleSelectionAdapter
            recyclerView!!.layoutManager = manager
            val space = resources.getDimension(R.dimen.space)
            recyclerView!!.addItemDecoration(ItemOffsetDecoration(space.toInt()))
        }
    }

    private fun initListData(): ModuleBeanWrapper? {
        try {
            val jsonIs: InputStream? = this.context?.assets?.open("module.json")
            val bufferedReader = BufferedReader(InputStreamReader(jsonIs))
            var str: String?
            val stringBuilder = StringBuilder()
            while (bufferedReader.readLine().also { str = it } != null) {
                stringBuilder.append(str)
            }
            return JSON.parseObject(stringBuilder.toString(), ModuleBeanWrapper::class.java)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}