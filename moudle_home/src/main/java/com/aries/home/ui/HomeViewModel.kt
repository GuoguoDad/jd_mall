package com.aries.home.ui

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Success
import com.aries.common.util.HttpUtil
import com.aries.home.ui.constants.ActionType
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
                    nineMenuList = (state()?.data?.nineMenuList ?: it.nineMenuList),
                    adUrl = (state()?.data?.adUrl ?: it.adUrl),
                    tabList = (state()?.data?.tabList ?: it.tabList)
                )
            }
        }
    }
}