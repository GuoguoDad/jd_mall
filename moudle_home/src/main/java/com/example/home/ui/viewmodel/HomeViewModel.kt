package com.example.home.ui.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Success
import com.example.common.util.HttpUtil
import com.example.home.ui.state.HomeState
import com.example.home.ui.constants.ActionType
import com.example.home.ui.ApiService
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
                    tabList = (state()?.data?.tabList ?: it.tabList),
                    goodsList = (state()?.data?.goodsList ?: it.goodsList),
                )
            }
        }
    }
}