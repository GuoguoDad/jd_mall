package com.example.news.ui.waterfall

import com.example.news.ui.base.BaseResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class QueryProductListParams(var currentPage: Number, var pageSize: Number)

data class GoodsBean(var thumb: String, var name: String)

data class ProductListRes(var dataList: MutableList<GoodsBean>, var totalCount: Int, var totalPageCount: Int)

open interface ApiService {

    @Headers("Content-Type: application/json","Accept: application/json")
    @POST("http://172.20.10.4:8090/mall/product/queryListByPage")
    fun queryProductListByPage(@Body body: QueryProductListParams): Call<BaseResponse<ProductListRes>>
}
