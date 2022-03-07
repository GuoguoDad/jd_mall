package com.example.category

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.common.base.BaseResponse

data class CategoryState(
    val brandList: List<CategoryBean> = emptyList(),
    val brandListResponse: Async<BaseResponse<List<CategoryBean>>> = Uninitialized,
    val content: ContentCateResponse = ContentCateResponse("", emptyList()),
    val contentResponse: Async<BaseResponse<ContentCateResponse>> = Uninitialized
): MavericksState