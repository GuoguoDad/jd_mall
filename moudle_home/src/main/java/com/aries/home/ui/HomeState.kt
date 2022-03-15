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
    val homeInfoResponse: Async<BaseResponse<HomeInfoResponse>> = Uninitialized,

    val goodsListFetchType: ActionType = ActionType.INIT,
    val goodsList: List<GoodsBean> = emptyList(),
    val goodsListResponse: Async<BaseResponse<GoodsListResponse>> = Uninitialized,
    val currentPage: Int = 1,
    val pageSize: Int = 10,
    val totalPage: Int = 0
): MavericksState
