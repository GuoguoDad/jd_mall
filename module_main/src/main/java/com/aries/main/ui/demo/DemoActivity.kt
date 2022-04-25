package com.aries.main.ui.demo

import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.aries.common.base.BaseActivity
import com.aries.common.constants.RouterPaths
import com.aries.main.R
import com.aries.common.util.GsonUtil
import com.aries.main.ui.demo.adapter.ModuleSelectionAdapter
import com.aries.main.ui.demo.bean.ModuleBean
import com.aries.main.ui.demo.bean.ModuleBeanWrapper
import com.aries.main.ui.demo.decor.ItemOffsetDecoration
import kotlinx.android.synthetic.main.fragment_mine.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder

@Route(path = RouterPaths.DEMO_ACTIVITY)
class DemoActivity : BaseActivity(R.layout.fragment_mine) {
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
                            "003" -> {
                                ARouter.getInstance().build(RouterPaths.STEPPER_DEMO).navigation()
                            }
                            "004" -> {
                                ARouter.getInstance().build(RouterPaths.STICKY_DEMO).navigation()
                            }
                            "005" -> {
                                ARouter.getInstance().build(RouterPaths.RN_PAGE).navigation()
                            }
                            else -> {
                                Toast.makeText(localThis ,bean?.name, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            })
            contentLayout!!.adapter = moduleSelectionAdapter

            val manager = GridLayoutManager(this, 6, GridLayoutManager.VERTICAL, false)
            manager.spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (moduleSelectionAdapter!!.getItemViewType(position)) {
                        titleItem -> 6
                        galleryItem -> 2
                        else -> 5
                    }
                }
            }
            contentLayout!!.layoutManager = manager
            val space = resources.getDimension(R.dimen.space)
            contentLayout!!.addItemDecoration(ItemOffsetDecoration(space.toInt()))
        }
    }

    override fun initData() {

    }

    private fun initListData(): ModuleBeanWrapper? {
        try {
            val jsonIs: InputStream = this.assets.open("module.json")
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