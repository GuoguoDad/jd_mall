package com.example.home.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.common.base.BaseResponse
import com.example.home.constants.ActionType
import com.example.home.HomeItem
import com.example.home.HomeItemBean
import com.example.home.HomeResponse

data class HomeState(
    val fetchType: ActionType = ActionType.REFRESH,
    val dataList: List<HomeItem<HomeItemBean>> = emptyList(),
    val newList: List<HomeItem<HomeItemBean>> = emptyList(),
    val pageResponse: Async<BaseResponse<HomeResponse>> = Uninitialized,
    val isLoading: Boolean = false,
    val currentPage: Int = 1,
    val pageSize: Int = 10,
    val totalPage: Int = 0
): MavericksState
