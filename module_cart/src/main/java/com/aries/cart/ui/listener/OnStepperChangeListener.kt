package com.aries.cart.ui.listener

import com.aries.cart.ui.CartGoodsBean

open interface OnStepperChangeListener {
    fun onStepperChange(bean: CartGoodsBean, value: Int)
}