package com.aries.cart.ui.listener

interface OnCartItemChangeListener {
    fun onItemStateChange(storeCode: String,  goodsCode: String, type: String)
}