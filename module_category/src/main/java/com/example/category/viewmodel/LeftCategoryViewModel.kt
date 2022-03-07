package com.example.category.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Success
import com.example.category.ApiService
import com.example.category.state.LeftCategoryState
import com.example.common.util.HttpUtil
import kotlinx.coroutines.Dispatchers

class LeftCategoryViewModel(initialState: LeftCategoryState): MavericksViewModel<LeftCategoryState>(initialState) {
    private var apiService: ApiService = HttpUtil.instance.service(ApiService::class.java)

    fun initBrandList() {
        withState {
            if (it.brandListResponse is Loading) return@withState

            suspend {
                apiService.queryCategoryList()
            }.execute(Dispatchers.IO) { state ->
                copy(
                    brandListResponse = state,
                    brandList = if (state is Success) state()?.data else it.brandList
                )
            }
        }
    }
}