package com.example.main.ui.waterfall

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.common.base.BaseResponse

data class WaterfallState(
    val fetchType: ActionType = ActionType.REFRESH,
    val dataList: List<GoodsBean> = emptyList(),
    val newList: List<GoodsBean> = emptyList(),
    val pageResponse: Async<BaseResponse<ProductListRes>> = Uninitialized,
    val isLoading: Boolean = false,
    val currentPage: Int = 1,
    val pageSize: Int = 10,
    val totalPage: Int = 0
): MavericksState
