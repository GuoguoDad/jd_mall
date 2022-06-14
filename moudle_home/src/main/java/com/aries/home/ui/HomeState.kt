package com.aries.home.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.aries.common.base.BaseResponse
import com.aries.home.ui.constants.ActionType

data class HomeState(
    val isLoading: Boolean = false,
    val fetchType: ActionType = ActionType.REFRESH,
    val bannerList: List<BannerBean> = emptyList(),
    val tabList: List<TabBean> = emptyList(),
    val adUrl: String = "",
    val nineMenuList: List<MenuBean> = emptyList(),
    val homeInfoResponse: Async<BaseResponse<HomeInfoResponse>> = Uninitialized
): MavericksState
