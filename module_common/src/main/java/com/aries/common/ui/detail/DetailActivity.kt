package com.aries.common.ui.detail

import android.os.Build
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.alibaba.android.arouter.facade.annotation.Route
import com.aries.common.R
import com.aries.common.base.BaseActivity
import com.aries.common.constants.RouterPaths
import com.aries.common.util.StatusBarUtil
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.detail_header.*

@Route(path = RouterPaths.GOODS_DETAIL)
class DetailActivity: BaseActivity(R.layout.activity_detail) {
    private val tabs = arrayListOf("商品", "评价", "详情", "推荐")

    override fun initView() {
        StatusBarUtil.setBarTextModal(this, true)
        initTabLayout()


    }

    override fun initData() {
        back.setOnClickListener {
            finish()
        }
    }

    private fun initTabLayout() {
        detailHeaderLayout.setPadding(0, StatusBarUtil.getHeight(), 0 , 0)
        detailHeaderTabLayout.removeAllTabs()
        detailHeaderTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    val title: TextView =  ((detailHeaderTabLayout.getChildAt(0) as LinearLayout).getChildAt(tab.position) as LinearLayout).getChildAt(1) as TextView
                    title.textSize = 20F
                    title.setTextAppearance(R.style.TabLayoutItemBold)
                }
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    val title: TextView =  ((detailHeaderTabLayout.getChildAt(0) as LinearLayout).getChildAt(tab.position) as LinearLayout).getChildAt(1) as TextView
                    title.textSize = 18F
                    title.setTextAppearance(R.style.TabLayoutItemNormal)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        tabs.forEach { v ->
            detailHeaderTabLayout.addTab(detailHeaderTabLayout.newTab().setText(v))
        }
    }
}