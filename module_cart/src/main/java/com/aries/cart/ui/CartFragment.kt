package com.aries.cart.ui

import com.aries.common.base.BaseFragment
import com.aries.cart.R
import com.aries.cart.ui.view.QuickEntryPopup
import com.aries.common.util.StatusBarUtil
import com.aries.common.util.UnreadMsgUtil
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.top_address.*

class CartFragment : BaseFragment(R.layout.fragment_cart) {
    override fun initView() {
        initStatusBarPlaceholder()

        UnreadMsgUtil.show(threePointsBadgeNum, 2)

        threePointsLayout.setOnClickListener {
            showQuickEntry()
        }
    }

    override fun initData() {}


    private fun initStatusBarPlaceholder() {
        topAddressLayout.setPadding(0, StatusBarUtil.getHeight(), 0 , 0)
        val layoutParams = statusBarPlaceholder.layoutParams
        layoutParams.height = StatusBarUtil.getHeight()
        statusBarPlaceholder.layoutParams = layoutParams
    }

    //顶部快捷入口
    private fun showQuickEntry() {
        XPopup.Builder(this.requireContext())
            .popupAnimation(PopupAnimation.TranslateFromTop)
            .hasShadowBg(false)
            .isLightStatusBar(true)
            .asCustom(QuickEntryPopup(this.requireContext()))
            .show()
    }
}