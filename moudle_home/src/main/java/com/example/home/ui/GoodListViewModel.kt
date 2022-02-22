package com.example.home.ui

import com.airbnb.mvrx.MavericksViewModel

class GoodListViewModel(initialState: GoodListState): MavericksViewModel<GoodListState>(initialState) {

    init {
        refresh(1)
    }

    fun refresh(pageSize: Int) = withState {
        setState { copy(isRefresh = true, isHasMore = true) }

    }

    fun loadMore(currentPage: Int, pageSize: Int) {

    }
}