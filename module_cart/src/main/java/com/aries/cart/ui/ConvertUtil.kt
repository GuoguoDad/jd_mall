package com.aries.cart.ui

object ConvertUtil {
    fun setAllCheck(data: List<StoreGoodsBean>): List<StoreGoodsBean> {
        data.forEach { v -> v.check = true; v.goodsList.forEach { m -> m.check = true } }
        return data
    }

    fun convertCartData(list: List<StoreGoodsBean>): ArrayList<CartBean> {
        val result: ArrayList<CartBean> = arrayListOf()
        list.forEach { m ->
            result.add(CartBean(1, m.storeName, m.storeCode, null, null, null, null, null, m.check!!))
            m.goodsList.forEach { n ->
                result.add(CartBean(2, m.storeName, m.storeCode, n.code, n.imgUrl, n.description, n.price, n.num, n.check!!))
            }
        }
        return result
    }
}