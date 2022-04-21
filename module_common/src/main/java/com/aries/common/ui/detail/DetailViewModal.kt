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
                    goodsInfo = if (state is Success) state().data.goodsInfo else it.goodsInfo,
                    detailInfo = if (state is Success) state().data.detailInfo else it.detailInfo
                )
            }
        }
    }

    fun updateSelectColorThumb(list: List<BannerBean>) {
        setState {
            copy(hasCodeFlag = hasCodeFlag + 1, bannerList = list)
        }
    }

    fun initRecommendList() {
        setState { copy(currentPage = 1) }
        withState {
            if (it.goodsListResponse is Loading) return@withState

            suspend {
                apiService.queryStoreGoodsList(QueryGoodsListParams(it.currentPage, it.pageSize))
            }.execute(Dispatchers.IO) { state ->
                copy(
                    currentPage = if (state is Success) currentPage.plus(1) else currentPage,
                    goodsListResponse = state,
                    goodsList = (state()?.data?.dataList ?: emptyList()),
                    totalPage = (state()?.data?.totalPageCount ?: 0)
                )
            }
        }
    }

    fun loadMoreRecommendList() {
        withState {
            if (it.goodsListResponse is Loading) return@withState

            suspend {
                apiService.queryStoreGoodsList(QueryGoodsListParams(it.currentPage, it.pageSize))
            }.execute(Dispatchers.IO) { state ->
                copy(
                    goodsListResponse = state,
                    goodsList = goodsList + (state()?.data?.dataList ?: arrayListOf()),
                    nextPageGoodsList = (state()?.data?.dataList ?: arrayListOf()),
                    currentPage = if (state is Success) currentPage.plus(1) else currentPage,
                    totalPage = (state()?.data?.totalPageCount ?: 0)
                )
            }
        }
    }
}