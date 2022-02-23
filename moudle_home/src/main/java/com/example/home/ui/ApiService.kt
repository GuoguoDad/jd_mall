package com.example.home.ui

import com.example.common.base.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

data class QueryGoodListParams(var currentPage: Number, var pageSize: Number)

data class GoodsBean(var url: String, var name: String, var price: String)

data class GoodsListRes(var dataList: List<GoodsBean>, var totalCount: Int, var totalPageCount: Int)

open interface ApiService {

    @POST("mall/goods/queryListByPage")
    suspend fun queryGoodListByPage(@Body body: QueryGoodListParams): BaseResponse<GoodsListRes>
}
