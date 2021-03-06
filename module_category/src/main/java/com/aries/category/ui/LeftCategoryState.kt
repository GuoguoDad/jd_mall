package com.aries.category.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.aries.common.base.BaseResponse

data class LeftCategoryState(
    val brandList: List<CategoryBean> = emptyList(),
    val brandListResponse: Async<BaseResponse<List<CategoryBean>>> = Uninitialized
): MavericksState