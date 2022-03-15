package com.aries.main.ui.coordinator

import com.aries.common.base.BaseResponse
import com.aries.main.ui.waterfall.ProductListRes
import com.aries.main.ui.waterfall.QueryProductListParams
import retrofit2.http.Body
import retrofit2.http.POST

data class BannerBean(var imgUrl: String, var name: String)

interface ApiService {
    @POST("mall/main/queryBanner")
    suspend fun queryBannerList(): BaseResponse<List<BannerBean>>

    @POST("mall/main/queryScrollTabs")
    suspend fun queryScrollTab(): BaseResponse<List<String>>

    @POST("mall/product/queryListByPage")
    suspend fun queryProductListByPage(@Body body: QueryProductListParams): BaseResponse<ProductListRes>
}