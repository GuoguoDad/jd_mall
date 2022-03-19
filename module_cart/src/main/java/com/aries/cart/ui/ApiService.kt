package com.aries.cart.ui

import com.aries.common.base.BaseResponse
import com.aries.common.bean.GoodsBean
import retrofit2.http.Body
import retrofit2.http.POST

data class QueryMaybeLikeListParams(var currentPage: Number, var pageSize: Number)
data class GoodsListResponse(var dataList: MutableList<GoodsBean>, var totalCount: Int, var totalPageCount: Int)

open interface ApiService {
    @POST("mall/cart/queryMaybeLikeList")
    suspend fun queryMaybeLikeListByPage(@Body body: QueryMaybeLikeListParams): BaseResponse<GoodsListResponse>
}
