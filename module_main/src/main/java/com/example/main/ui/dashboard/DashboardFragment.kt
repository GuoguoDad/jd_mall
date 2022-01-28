package com.example.main.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.example.main.R
import com.example.main.kit.util.GsonUtil
import com.example.main.databinding.FragmentDashboardBinding
import com.example.main.ui.dashboard.adapter.ModuleSelectionAdapter
import com.example.main.ui.dashboard.bean.ModuleBean
import com.example.main.ui.dashboard.bean.ModuleBeanWrapper
import com.example.main.ui.dashboard.decor.ItemOffsetDecoration
import com.example.main.ui.waterfall.WaterfallListActivity
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

        initView()
        initData()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        recyclerView = binding.rvModule
    }

    private fun initData() {
        val localThis = this
        val wrapper = initListData()
        if (wrapper != null) {
            moduleSelectionAdapter = ModuleSelectionAdapter(wrapper)
            moduleSelectionAdapter!!.setOnItemClickListener(object : ModuleSelectionAdapter.OnItemClickListener {
                override fun onClick(view: View?, bean: ModuleBean?) {
                    if (bean != null) {
                        when(bean.code) {
                            "001" -> {
                                val to = Intent(localThis.context, WaterfallListActivity::class.java)
                                startActivity(to)
                            }
                            else -> {
                                Toast.makeText(localThis.context ,bean?.name, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            })
            recyclerView!!.adapter = moduleSelectionAdapter

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
            return GsonUtil.instance.fromJson(stringBuilder.toString(),  ModuleBeanWrapper::class.java)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}