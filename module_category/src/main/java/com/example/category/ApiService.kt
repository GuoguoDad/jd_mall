package com.example.category

import com.example.common.base.BaseResponse
import retrofit2.http.POST

data class CategoryBean(var name:String, var code: String, var isSelect: Boolean?, var fillet: String?)

interface ApiService {
    @POST("mall/category/list")
    suspend fun queryCategoryList(): BaseResponse<List<CategoryBean>>
}