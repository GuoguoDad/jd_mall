package com.aries.mine.ui

import com.aries.common.base.BaseResponse
import com.aries.common.bean.GoodsBean
import retrofit2.http.Body
import retrofit2.http.POST

data class MenuBean(var menuIcon: String, var menuName: String, var menuCode: String)
data class MineInfoResponse( var functionList: MutableList<MenuBean>)

data class QueryRecommendListParams(var currentPage: Number, var pageSize: Number)
data class GoodsListResponse(var dataList: MutableList<GoodsBean>, var totalCount: Int, var totalPageCount: Int)

interface ApiService {
    @POST("mall/mine/queryMineInfo")
    suspend fun queryMineInfo(): BaseResponse<MineInfoResponse>

    @POST("mall/mine/queryRecommendList")
    suspend fun queryRecommendListByPage(@Body body: QueryRecommendListParams): BaseResponse<GoodsListResponse>
}