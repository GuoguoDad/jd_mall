package com.aries.home.ui

import com.aries.common.base.BaseResponse
import com.aries.common.bean.GoodsBean
import retrofit2.http.Body
import retrofit2.http.POST

data class BannerBean(var imgUrl: String, var type: String)
data class TabBean(var name: String, var code: String)
data class MenuBean(var menuIcon: String, var menuName: String, var menuCode: String, var h5url: String?)
data class HomeInfoResponse(var bannerList: MutableList<BannerBean>, var tabList: MutableList<TabBean>, var adUrl: String, var nineMenuList: MutableList<MenuBean>)

data class QueryGoodsListParams(var code: String ,var currentPage: Number, var pageSize: Number)
data class GoodsListResponse(var dataList: MutableList<GoodsBean>, var totalCount: Int, var totalPageCount: Int)

interface ApiService {

    @POST("mall/home/queryHomePageInfo")
    suspend fun queryHomePageInfo(): BaseResponse<HomeInfoResponse>

    @POST("mall/home/queryGoodsListByPage")
    suspend fun queryGoodsListByPage(@Body body: QueryGoodsListParams): BaseResponse<GoodsListResponse>
}
