package com.aries.cart.ui

import com.aries.common.base.BaseResponse
import com.aries.common.bean.GoodsBean
import retrofit2.http.Body
import retrofit2.http.POST

data class QuickMenuBean(var menuIcon: Int, var menuName: String, var menuCode: String)

data class QueryMaybeLikeListParams(var currentPage: Number, var pageSize: Number)
data class GoodsListResponse(var dataList: MutableList<GoodsBean>, var totalCount: Int, var totalPageCount: Int)

data class CartGoodsBean(
    var code: String,
    var imgUrl: String,
    var description: String,
    var price: String,
    var check: Boolean? = true,
    var num: Int = 1
)

data class StoreGoodsBean(
    var storeName: String,
    var storeCode: String,
    var goodsList: ArrayList<CartGoodsBean>,
    var check: Boolean? = true
)


open interface ApiService {
    @POST("mall/cart/queryMaybeLikeList")
    suspend fun queryMaybeLikeListByPage(@Body body: QueryMaybeLikeListParams): BaseResponse<GoodsListResponse>


    @POST("mall/cart/queryCartGoodsList")
    suspend fun queryCartGoodsList(): BaseResponse<List<StoreGoodsBean>>
}
