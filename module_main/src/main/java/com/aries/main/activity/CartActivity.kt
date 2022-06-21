package com.aries.main.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.aries.cart.ui.CartFragment
import com.aries.common.base.BaseActivity
import com.aries.common.constants.RouterPaths
import com.aries.common.util.StatusBarUtil
import com.aries.main.R

@Route(path = RouterPaths.CART_ACTIVITY)
class CartActivity: BaseActivity(R.layout.activity_cart) {
    override fun initView() {
        StatusBarUtil.setBarTextModal(this, true)
        supportFragmentManager.beginTransaction().replace(R.id.cartContent, CartFragment()).commitAllowingStateLoss()
    }

    override fun initData() { }
}