package com.aries.cart.ui

object ConvertUtil {
    fun setAllCheck(data: List<StoreGoodsBean>): List<StoreGoodsBean> {
        data.forEach { v -> v.check = true; v.goodsList.forEach { m -> m.check = true } }
        return data
    }
}