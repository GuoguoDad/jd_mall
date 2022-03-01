package com.example.main.ui.scrolltab

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Success
import com.example.common.util.HttpUtil
import com.example.main.ui.waterfall.QueryProductListParams
import kotlinx.coroutines.Dispatchers

class ScrollTabViewModel(initialState: ScrollTabState): MavericksViewModel<ScrollTabState>(initialState) {
    private var apiService: ApiService = HttpUtil.instance.service(ApiService::class.java)

    fun queryBanner(isRefresh: Boolean) {
        setState { copy(actionType = "") }
        withState {
            if (it.bannerResponse is Loading) return@withState

            suspend {
                apiService.queryBannerList()
            }.execute(Dispatchers.IO) { state ->
                copy(
                    actionType = if (state is Success && isRefresh) "refresh" else "init",
                    bannerResponse = state,
                    bannerList = state()?.data ?: emptyList()
                )
            }
        }
    }

    fun queryScrollTab() {
        setState { copy(actionType = "") }
        withState {
            if (it.tabsResponse is Loading) return@withState

            suspend {
                apiService.queryScrollTab()
            }.execute(Dispatchers.IO) { state ->
                copy(
                    tabsResponse = state,
                    actionType = if (state is Success) "scrollTab" else "",
                    tabs = state()?.data ?: emptyList()
                )
            }
        }
    }

    fun queryProductListByPage(isInit: Boolean) {
        setState { copy(isLoading = true, actionType = "" , currentPage = if (isInit) 1 else currentPage) }
        withState {
            if (it.GoodsListResponse is Loading) return@withState

            suspend {
                apiService.queryProductListByPage(QueryProductListParams(it.currentPage, it.pageSize))
            }.execute(Dispatchers.IO) { state ->
                copy(
                    GoodsListResponse = state,
                    isLoading = state !is Success,
                    actionType = if (state is Success && currentPage === 1) "init" else "loadMore",
                    GoodsList = (state()?.data?.dataList ?: emptyList()),
                    currentPage = if (state is Success) currentPage.plus(1) else currentPage,
                    totalPage = (state()?.data?.totalPageCount ?: 0)
                )
            }
        }
    }
}