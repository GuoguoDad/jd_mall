package com.example.home.ui

import com.airbnb.mvrx.MavericksState

data class GoodListState(
    var dataList: List<GoodsBean> = emptyList(),
    var currentPage: Int = 1,
    var pageSize: Int = 10,
    var isRefresh: Boolean = false,
    var isHasMore: Boolean = true
): MavericksState
