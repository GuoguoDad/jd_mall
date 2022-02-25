package com.example.home.ui

import com.example.common.base.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

data class GoodsBean(var url: String, var name: String, var price: String)

data class BannerBean(var imgUrl: String, var name: String)

data class HomeItem<T>(var type: String, var data: T)

data class HomeItemBean(var bannerList: MutableList<BannerBean>?, var goodsList: MutableList<GoodsBean>?)

data class QueryHomeParams(var currentPage: Number, var pageSize: Number)

data class HomeResponse(var dataList: MutableList<HomeItem<HomeItemBean>>, var totalCount: Int, var totalPageCount: Int)

open interface ApiService {

    @POST("mall/home/queryListByPage")
    suspend fun queryHomePageData(@Body body: QueryHomeParams): BaseResponse<HomeResponse>
}
