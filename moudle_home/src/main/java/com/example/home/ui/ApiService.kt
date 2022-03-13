package com.example.home.ui

import com.example.common.base.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

data class BannerBean(var imgUrl: String, var type: String)
data class TabBean(var name: String, var code: String)
data class HomeInfoResponse(var bannerList: MutableList<BannerBean>, var tabList: MutableList<TabBean>)

data class GoodsBean(var imgUrl: String, var description: String, var price: String)
data class QueryGoodsListParams(var code: String ,var currentPage: Number, var pageSize: Number)
data class GoodsListResponse(var dataList: MutableList<GoodsBean>, var totalCount: Int, var totalPageCount: Int)

open interface ApiService {

    @POST("mall/home/queryHomePageInfo")
    suspend fun queryHomePageInfo(): BaseResponse<HomeInfoResponse>

    @POST("mall/home/queryGoodsListByPage")
    suspend fun queryGoodsListByPage(@Body body: QueryGoodsListParams): BaseResponse<GoodsListResponse>
}
