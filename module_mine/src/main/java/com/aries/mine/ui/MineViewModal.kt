package com.aries.mine.ui

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Success
import com.aries.common.util.HttpUtil
import kotlinx.coroutines.Dispatchers

class MineViewModal(initialState: MineState): MavericksViewModel<MineState>(initialState) {
    private var apiService: ApiService = HttpUtil.instance.service(ApiService::class.java)

    fun initRecommendList() {
        setState { copy(currentPage = 1) }
        withState {
            if (it.goodsListResponse is Loading) return@withState

            suspend {
                apiService.queryRecommendListByPage(QueryRecommendListParams(it.currentPage, it.pageSize))
            }.execute(Dispatchers.IO) { state ->
                copy(
                    currentPage = if (state is Success) currentPage.plus(1) else currentPage,
                    goodsListResponse = state,
                    goodsList = (state()?.data?.dataList ?: it.goodsList),
                    totalPage = (state()?.data?.totalPageCount ?: 0)
                )
            }
        }
    }

    fun loadMoreRecommendList() {
        withState {
            if (it.goodsListResponse is Loading) return@withState

            suspend {
                apiService.queryRecommendListByPage(QueryRecommendListParams(it.currentPage, it.pageSize))
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