package com.example.category

import com.example.common.base.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

data class CategoryBean(var name:String, var code: String, var isSelect: Boolean?, var fillet: String?)

data class CateBean(var iconUrl: String?, var categoryName: String,var categoryCode: String, var cateList: List<CateBean>?)

data class ContentCateResponse(var bannerUrl: String, var cateList: List<CateBean>)

interface ApiService {
    @GET("mall/category/list")
    suspend fun queryCategoryList(): BaseResponse<List<CategoryBean>>

    @GET("mall/category/queryContentByCategory")
    suspend fun queryContentByCate(@Query("categoryCode") categoryCode: String): BaseResponse<ContentCateResponse>
}