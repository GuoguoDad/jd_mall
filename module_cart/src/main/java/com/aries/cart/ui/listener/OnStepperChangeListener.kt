package com.aries.cart.ui.listener

import com.aries.cart.ui.CartBean

interface OnStepperChangeListener {
    fun onStepperChange(bean: CartBean, value: Int)
}