package com.aries.cart.ui

import com.aries.common.base.BaseFragment
import com.aries.cart.R
import com.aries.common.util.StatusBarUtil
import com.aries.common.util.UnreadMsgUtil
import kotlinx.android.synthetic.main.cart_top_address.*
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : BaseFragment(R.layout.fragment_cart) {
    override fun initView() {
        initStatusBarPlaceholder()
        UnreadMsgUtil.show(threePointsBadgeNum, 2)
    }

    override fun initData() {
    }


    private fun initStatusBarPlaceholder() {
        topAddressLayout.setPadding(0, StatusBarUtil.getHeight(), 0 , 0)
        val layoutParams = statusBarPlaceholder.layoutParams
        layoutParams.height = StatusBarUtil.getHeight()
        statusBarPlaceholder.layoutParams = layoutParams
    }
}