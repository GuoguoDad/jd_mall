package com.aries.main.ui.mine

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.alibaba.android.arouter.launcher.ARouter
import com.aries.common.base.BaseFragment
import com.aries.common.constants.RouterPaths
import com.aries.main.R
import com.aries.common.util.GsonUtil
import com.aries.main.ui.mine.adapter.ModuleSelectionAdapter
import com.aries.main.ui.mine.bean.ModuleBean
import com.aries.main.ui.mine.bean.ModuleBeanWrapper
import com.aries.main.ui.mine.decor.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_mine.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder

class MineFragment : BaseFragment(R.layout.fragment_mine) {
    val titleItem = 0
    val galleryItem = 1
    private var moduleSelectionAdapter: ModuleSelectionAdapter? = null

    override fun initView() {
        val localThis = this
        val wrapper = initListData()
        if (wrapper != null) {
            moduleSelectionAdapter = ModuleSelectionAdapter(wrapper)
            moduleSelectionAdapter!!.setOnItemClickListener(object : ModuleSelectionAdapter.OnItemClickListener {
                override fun onClick(view: View?, bean: ModuleBean?) {
                    if (bean != null) {
                        when(bean.code) {
                            "001" -> {
                                ARouter.getInstance().build(RouterPaths.WATERFALL_ACTIVITY).navigation()
                            }
                            "002" -> {
                                ARouter.getInstance().build(RouterPaths.TAB_WATERFALL_ACTIVITY).navigation()
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

    override fun initData() {

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