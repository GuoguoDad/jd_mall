package com.example.news.ui.waterfall

import com.example.news.ui.base.BaseResponse
import retrofit2.Call
import retrofit2.http.GET

data class GoodsBean(var goodsCode: String, var goodsName: String)

data class ProductListRes(var dataList: MutableList<GoodsBean>, var totalCount: Int, var totalPageCount: Int)

open interface ApiService {
    @GET("http://10.46.82.16:8090/mall/coupon/getCarrefourCouponList")
    fun queryProductListByPage(): Call<BaseResponse<ProductListRes>>
}
