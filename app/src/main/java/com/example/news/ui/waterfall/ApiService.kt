package com.example.news.ui.waterfall

import com.example.news.ui.base.BaseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class QueryProductListParams(var currentPage: Number, var pageSize: Number)

data class GoodsBean(var thumb: String, var name: String, var width: Int, var height: Int)

data class ProductListRes(var dataList: MutableList<GoodsBean>, var totalCount: Int, var totalPageCount: Int)

open interface ApiService {

    @POST("mall/product/queryListByPage")
    fun queryProductListByPage(@Body body: QueryProductListParams): Call<BaseResponse<ProductListRes>>
}
