package com.aries.common.ui.detail

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.aries.common.base.BaseResponse

data class DetailState(
    val bannerList: List<BannerBean> = emptyList(),
    val currentBanner: BannerBean = BannerBean("","", "", emptyList()),
    val detailInfo: DetailInfo = DetailInfo("","", emptyList(), emptyList()),
    val detailInfoResponse: Async<BaseResponse<GoodsDetailInfoResponse>> = Uninitialized,
): MavericksState