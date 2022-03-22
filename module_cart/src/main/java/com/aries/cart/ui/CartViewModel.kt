package com.aries.cart.ui

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Success
import com.aries.common.util.HttpUtil
import kotlinx.coroutines.Dispatchers

class CartViewModel(initialState: CartState): MavericksViewModel<CartState>(initialState) {
    private var apiService: ApiService = HttpUtil.instance.service(ApiService::class.java)

    fun queryCartGoodsList(isRefresh: Boolean) {
        withState {
            if (it.goodsListResponse is Loading) return@withState

            suspend {
                apiService.queryCartGoodsList()
            }.execute(Dispatchers.IO) { state ->
                copy(
                    cartGoodsListResponse = state,
                    fetchType = if (isRefresh) "refresh" else "init",
                    cartGoodsList = ConvertUtil.setAllCheck(state()?.data?: it.cartGoodsList)
                )
            }
        }
    }

    fun updateCartGoodsList(list: List<StoreGoodsBean>) {
        setState {
            copy(hasCodeFlag = hasCodeFlag + 1, cartGoodsList = list)
        }
    }

    fun initMaybeLikeList() {
        setState { copy(currentPage = 1) }
        withState {
            if (it.goodsListResponse is Loading) return@withState

            suspend {
                apiService.queryMaybeLikeListByPage(QueryMaybeLikeListParams(it.currentPage, it.pageSize))
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

    fun loadMoreMaybeLikeList() {
        withState {
            if (it.goodsListResponse is Loading) return@withState

            suspend {
                apiService.queryMaybeLikeListByPage(QueryMaybeLikeListParams(it.currentPage, it.pageSize))
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