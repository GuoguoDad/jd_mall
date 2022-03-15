package com.example.category.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.common.base.BaseResponse

data class RightCategoryState(
    val content: ContentCateResponse = ContentCateResponse("", emptyList()),
    val contentResponse: Async<BaseResponse<ContentCateResponse>> = Uninitialized
): MavericksState