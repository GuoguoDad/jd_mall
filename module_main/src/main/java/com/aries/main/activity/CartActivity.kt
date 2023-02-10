package com.aries.main.activity

import android.view.LayoutInflater
import com.alibaba.android.arouter.facade.annotation.Route
import com.aries.cart.ui.CartFragment
import com.aries.common.base.BaseActivity
import com.aries.common.constants.RouterPaths
import com.aries.common.util.StatusBarUtil
import com.aries.main.R
import com.aries.main.databinding.ActivityCartBinding

@Route(path = RouterPaths.CART_ACTIVITY)
class CartActivity: BaseActivity<ActivityCartBinding>() {
    override fun getViewBinding(): ActivityCartBinding {
        return ActivityCartBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        StatusBarUtil.setBarTextModal(this, true)
        supportFragmentManager.beginTransaction().replace(R.id.cartContent, CartFragment()).commitAllowingStateLoss()
    }
    override fun initData() { }
}