package com.example.home.ui.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Success
import com.example.common.util.HttpUtil
import com.example.home.ui.state.HomeState
import com.example.home.ui.constants.ActionType
import com.example.home.ui.ApiService
import com.example.home.ui.QueryGoodsListParams
import kotlinx.coroutines.Dispatchers

class HomeViewModel(initialState: HomeState): MavericksViewModel<HomeState>(initialState) {
    private var apiService: ApiService = HttpUtil.instance.service(ApiService::class.java)

    fun init(isRefresh: Boolean) {
        setState { copy(isLoading = true) }
        withState {
            if (it.homeInfoResponse is Loading) return@withState

            suspend {
                apiService.queryHomePageInfo()
            }.execute(Dispatchers.IO) { state ->
                copy(
                    homeInfoResponse = state,
                    isLoading = state !is Success,
                    fetchType = if (isRefresh) ActionType.REFRESH else ActionType.INIT,
                    bannerList = (state()?.data?.bannerList ?: it.bannerList),
                    tabList = (state()?.data?.tabList ?: it.tabList)
                )
            }
        }
    }

    fun initGoodsList() {
        setState { copy(currentPage = 1) }
        withState {
            if (it.goodsListResponse is Loading) return@withState

            suspend {
                apiService.queryGoodsListByPage(QueryGoodsListParams(it.currentPage, it.pageSize))
            }.execute(Dispatchers.IO) { state ->
                copy(
                    currentPage = if (state is Success) currentPage.plus(1) else currentPage,
                    goodsListResponse = state,
                    goodsListFetchType = ActionType.INIT,
                    goodsList = (state()?.data?.dataList ?: it.goodsList),
                    totalPage = (state()?.data?.totalPageCount ?: 0)
                )
            }
        }
    }

    fun loadMoreGoodsList() {
        withState {
            if (it.goodsListResponse is Loading) return@withState

            suspend {
                apiService.queryGoodsListByPage(QueryGoodsListParams(it.currentPage, it.pageSize))
            }.execute(Dispatchers.IO) { state ->
                copy(
                    goodsListResponse = state,
                    goodsListFetchType = ActionType.LOADMORE,
                    goodsList = goodsList + (state()?.data?.dataList ?: arrayListOf()),
                    currentPage = if (state is Success) currentPage.plus(1) else currentPage,
                    totalPage = (state()?.data?.totalPageCount ?: 0)
                )
            }
        }
    }
}