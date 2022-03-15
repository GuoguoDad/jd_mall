package com.aries.main.ui.coordinator

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.aries.common.base.BaseResponse
import com.aries.main.ui.coordinator.BannerBean
import com.aries.main.ui.waterfall.GoodsBean
import com.aries.main.ui.waterfall.ProductListRes

data class ScrollTabState (
    val actionType: String = "",
    val bannerList: List<BannerBean> = emptyList(),
    val bannerResponse: Async<BaseResponse<List<BannerBean>>> = Uninitialized,
    val tabs: List<String> = emptyList(),
    val tabsResponse: Async<BaseResponse<List<String>>> = Uninitialized,
    val GoodsList: List<GoodsBean> = emptyList(),
    val GoodsListResponse: Async<BaseResponse<ProductListRes>> = Uninitialized,
    val isLoading: Boolean = false,
    val currentPage: Int = 1,
    val pageSize: Int = 10,
    val totalPage: Int = 0
): MavericksState