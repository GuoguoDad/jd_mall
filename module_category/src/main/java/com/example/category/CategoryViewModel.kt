package com.example.category

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.Success
import com.example.common.util.HttpUtil
import kotlinx.coroutines.Dispatchers

class CategoryViewModel(initialState: CategoryState): MavericksViewModel<CategoryState>(initialState) {
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