package com.aries.common.ui.detail

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Success
import com.aries.common.ui.detail.ConvertUtil.setDefaultSelect
import com.aries.common.util.HttpUtil
import kotlinx.coroutines.Dispatchers

class DetailViewModal(initialState: DetailState) : MavericksViewModel<DetailState>(initialState){
    private var apiService: ApiService = HttpUtil.instance.service(ApiService::class.java)

    fun queryGoodsDetail() {
        withState {
            if (it.detailInfoResponse is Loading) return@withState

            suspend {
                apiService.queryGoodsDetail()
            }.execute(Dispatchers.IO) { state ->
                val bannerList = (state()?.data?.bannerList ?: it.bannerList)
                copy(
                    detailInfoResponse = state,
                    bannerList = setDefaultSelect(bannerList),
                    currentBanner = if (bannerList.isNotEmpty()) bannerList[0] else it.currentBanner,
                    goodsInfo =if (state is Success) state().data.goodsInfo else it.goodsInfo
                )
            }
        }
    }

    fun updateSelectColorThumb(list: List<BannerBean>, banner: BannerBean) {
        setState {
            copy(hasCodeFlag = hasCodeFlag + 1, bannerList = list, currentBanner = banner)
        }
    }
}