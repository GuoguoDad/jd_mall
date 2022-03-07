package com.example.category.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.category.CategoryBean
import com.example.common.base.BaseResponse

data class LeftCategoryState(
    val brandList: List<CategoryBean> = emptyList(),
    val brandListResponse: Async<BaseResponse<List<CategoryBean>>> = Uninitialized
): MavericksState