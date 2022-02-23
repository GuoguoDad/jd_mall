package com.example.home.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.common.base.BaseResponse

data class GoodListState(
    val fetchType: FetchType = FetchType.REFRESH,
    val dataList: List<GoodsBean> = emptyList(),
    val newList: List<GoodsBean> = emptyList(),
    val pageResponse: Async<BaseResponse<GoodsListRes>> = Uninitialized,
    val currentPage: Int = 1,
    val pageSize: Int = 10,
    val totalPage: Int = 0
): MavericksState
