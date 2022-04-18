package com.aries.common.ui.detail

import com.alibaba.android.arouter.facade.annotation.Route
import com.aries.common.R
import com.aries.common.base.BaseActivity
import com.aries.common.constants.RouterPaths
import com.aries.common.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.detail_header.*

@Route(path = RouterPaths.GOODS_DETAIL)
class DetailActivity: BaseActivity(R.layout.activity_detail) {
    private val tabs = arrayListOf("商品", "评价", "详情", "推荐")

    override fun initView() {
        StatusBarUtil.setBarTextModal(this, true)
        initTabLayout()
    }

    override fun initData() {

    }

    private fun initTabLayout() {
        detailHeaderLayout.setPadding(0, StatusBarUtil.getHeight(), 0 , 0)
        detailHeaderTabLayout.removeAllTabs()
        tabs.forEach { v ->
            detailHeaderTabLayout.addTab(detailHeaderTabLayout.newTab().setText(v))
        }
    }
}