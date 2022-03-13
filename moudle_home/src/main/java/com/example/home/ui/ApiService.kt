package com.example.home.ui

import com.example.common.base.BaseResponse
import retrofit2.http.POST

data class BannerBean(var imgUrl: String, var type: String)

data class TabBean(var name: String, var code: String)

data class GoodsBean(var imgUrl: String, var description: String, var price: String)

data class HomeInfoResponse(var bannerList: MutableList<BannerBean>, var tabList: MutableList<TabBean>, var goodsList: MutableList<GoodsBean>)

open interface ApiService {

    @POST("mall/home/queryHomePageInfo")
    suspend fun queryHomePageInfo(): BaseResponse<HomeInfoResponse>
}
