package com.aries.common.ui.detail

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.aries.common.base.BaseResponse
import com.aries.common.bean.GoodsBean

data class DetailState(
    val hasCodeFlag: Int = 0,
    val bannerList: List<BannerBean> = emptyList(),
    val goodsInfo: GoodsInfo = GoodsInfo("","", emptyList(), "", emptyList()),
    val detailInfo: DetailInfo = DetailInfo("","", emptyList(), emptyList()),
    val detailInfoResponse: Async<BaseResponse<GoodsDetailInfoResponse>> = Uninitialized,

    val goodsList: List<GoodsBean> = emptyList(),
    val nextPageGoodsList: List<GoodsBean> = emptyList(),
    val goodsListResponse: Async<BaseResponse<GoodsListResponse>> = Uninitialized,
    val currentPage: Int = 1,
    val pageSize: Int = 10,
    val totalPage: Int = 0
): MavericksState {
    override fun hashCode(): Int {
        return hasCodeFlag
    }
}