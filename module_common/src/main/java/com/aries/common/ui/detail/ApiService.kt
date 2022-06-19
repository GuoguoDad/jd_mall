package com.aries.common.ui.detail

import com.aries.common.base.BaseResponse
import com.aries.common.bean.GoodsBean
import com.chad.library.adapter.base.entity.SectionEntity
import com.stx.xhb.androidx.entity.BaseBannerInfo
import retrofit2.http.Body
import retrofit2.http.POST

data class BannerBean(var colorId: String,var colorName: String, var thumb: String, var imgList: List<String>, var select: Boolean?)
data class AppraiseBean(
    var headerUrl: String?,
    var userName: String?,
    var content: String?,
    var type: Int,
    var color: String?,
    var size: String?,
    var url: String?
): SectionEntity {
    override val isHeader: Boolean
        get() = this.type == 1
}
data class GoodsInfo(var originalPrice: String,var specialPrice: String, var tagList: List<String>, var goodsName: String, var appraiseList: List<AppraiseBean>)
data class DetailInfo(var hdzq: String, var dnyx: String, var introductionList: List<String>, var serviceList: List<String>)
data class GoodsDetailInfoResponse(var bannerList: List<BannerBean>, var goodsInfo: GoodsInfo, var detailInfo: DetailInfo)

data class QueryGoodsListParams(var currentPage: Int, var pageSize: Int)
data class GoodsListResponse(var dataList: MutableList<GoodsBean>, var totalCount: Int, var totalPageCount: Int)

data class TopBanner(var url: String, var title: String): BaseBannerInfo {
    override fun getXBannerUrl(): Any {
        return url
    }

    override fun getXBannerTitle(): String {
        return title
    }
}

interface ApiService {
    @POST("mall/detail/queryGoodsDetail")
    suspend fun queryGoodsDetail(): BaseResponse<GoodsDetailInfoResponse>

    @POST("mall/detail/queryStoreGoodsList")
    suspend fun queryStoreGoodsList(@Body body: QueryGoodsListParams): BaseResponse<GoodsListResponse>
}
