package com.aries.mine.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.aries.common.base.BaseResponse
import com.aries.common.bean.GoodsBean

data class MineState(
    val fiveMenuList: List<MenuBean> = emptyList(),
    val mineInfoResponse: Async<BaseResponse<MineInfoResponse>> = Uninitialized,

    val goodsList: List<GoodsBean> = emptyList(),
    val nextPageGoodsList: List<GoodsBean> = emptyList(),
    val goodsListResponse: Async<BaseResponse<GoodsListResponse>> = Uninitialized,
    val currentPage: Int = 1,
    val pageSize: Int = 10,
    val totalPage: Int = 0
): MavericksState