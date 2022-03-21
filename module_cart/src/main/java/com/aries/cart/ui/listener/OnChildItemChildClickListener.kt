package com.aries.cart.ui.listener

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter

open interface OnChildItemChildClickListener {
    fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, parentPosition: Int, position: Int)
}