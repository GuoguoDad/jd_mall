package com.aries.category.ui

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Success
import com.aries.common.util.HttpUtil
import kotlinx.coroutines.Dispatchers

class RightCategoryViewModel(initialState: RightCategoryState): MavericksViewModel<RightCategoryState>(initialState) {
    private var apiService: ApiService = HttpUtil.instance.service(ApiService::class.java)


    fun queryContentByCate(categoryCode: String) {
        withState {
            if (it.contentResponse is Loading) return@withState

            suspend {
                apiService.queryContentByCate(categoryCode)
            }.execute(Dispatchers.IO) { state ->
                copy(
                    contentResponse = state,
                    content = if (state is Success) state()?.data!! else it.content
                )
            }
        }
    }
}