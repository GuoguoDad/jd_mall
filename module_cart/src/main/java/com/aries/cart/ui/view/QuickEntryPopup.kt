package com.aries.cart.ui.view

import android.content.Context
import android.view.LayoutInflater
import com.aries.cart.R
import com.aries.cart.databinding.QuickEntryGridvewBinding
import com.aries.cart.ui.QuickMenuBean
import com.aries.cart.ui.adapter.QuickEntryPopupAdapter
import com.aries.common.util.DisplayUtil
import com.aries.common.util.StatusBarUtil
import com.lxj.xpopup.core.PositionPopupView

class QuickEntryPopup(context: Context): PositionPopupView(context) {
    private lateinit var binding: QuickEntryGridvewBinding
    private var data: ArrayList<QuickMenuBean> = arrayListOf()

    override fun getImplLayoutId(): Int {
        return R.layout.quick_entry_gridvew
    }

    override fun onCreate() {
        super.onCreate()
        binding = QuickEntryGridvewBinding.inflate(LayoutInflater.from(context))
        initData()

        binding.quickEntryLayout.setPadding(15, StatusBarUtil.getHeight(), 15, 28)

        binding.quickEntryGridView.adapter = QuickEntryPopupAdapter(context, data)

        binding.closeImg.setOnClickListener { dismiss() }


    }

    override fun getMaxWidth(): Int {
        return DisplayUtil.getScreenWidth(context)
    }

    private fun initData() {
        data.clear()
        data.add(QuickMenuBean(R.drawable.default_img, "消息", "message"))
        data.add(QuickMenuBean(R.drawable.default_img, "首页", "home"))
        data.add(QuickMenuBean(R.drawable.default_img, "搜索", "search"))
        data.add(QuickMenuBean(R.drawable.default_img, "分享", "share"))
        data.add(QuickMenuBean(R.drawable.default_img, "家庭清单", "order"))
        data.add(QuickMenuBean(R.drawable.default_img, "功能反馈", "feedback"))
    }
}