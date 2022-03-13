package com.example.home.ui.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.common.base.BaseResponse
import com.example.home.ui.BannerBean
import com.example.home.ui.GoodsBean
import com.example.home.ui.HomeInfoResponse
import com.example.home.ui.TabBean
import com.example.home.ui.constants.ActionType

data class HomeState(
    val fetchType: ActionType = ActionType.REFRESH,
    val bannerList: List<BannerBean> = emptyList(),
    val tabList: List<TabBean> = emptyList(),
    val homeInfoResponse: Async<BaseResponse<HomeInfoResponse>> = Uninitialized,
    val goodsList: List<GoodsBean> = emptyList(),
    val isLoading: Boolean = false
): MavericksState
