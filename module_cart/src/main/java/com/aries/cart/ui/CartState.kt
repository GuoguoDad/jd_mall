package com.aries.cart.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.aries.common.base.BaseResponse
import com.aries.common.bean.GoodsBean

data class CartState(
    val cartGoodsList: List<StoreGoodsBean> = emptyList(),
    val cartGoodsListResponse: Async<BaseResponse<List<StoreGoodsBean>>> = Uninitialized,

    val goodsList: List<GoodsBean> = emptyList(),
    val goodsListResponse: Async<BaseResponse<GoodsListResponse>> = Uninitialized,
    val currentPage: Int = 1,
    val pageSize: Int = 10,
    val totalPage: Int = 0
): MavericksState
