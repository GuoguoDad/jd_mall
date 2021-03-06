package com.aries.home.ui.fragment.goods

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Success
import com.aries.common.util.HttpUtil
import com.aries.home.ui.ApiService
import com.aries.home.ui.QueryGoodsListParams
import kotlinx.coroutines.Dispatchers

class GoodsViewModel(initialState: GoodsState): MavericksViewModel<GoodsState>(initialState) {
    private var apiService: ApiService = HttpUtil.instance.service(ApiService::class.java)

    fun initGoodsList(code: String) {
        setState { copy(currentPage = 1) }
        withState {
            if (it.goodsListResponse is Loading) return@withState

            suspend {
                apiService.queryGoodsListByPage(QueryGoodsListParams(code, it.currentPage, it.pageSize))
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

    fun loadMoreGoodsList(code: String) {
        withState {
            if (it.goodsListResponse is Loading) return@withState

            suspend {
                apiService.queryGoodsListByPage(QueryGoodsListParams(code, it.currentPage, it.pageSize))
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