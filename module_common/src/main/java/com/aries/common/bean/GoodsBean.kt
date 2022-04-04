package com.aries.common.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

data class GoodsBean(
    var imgUrl: String,
    var description: String?,
    var price: String?,
    var tag: String?,
    var des1: String?,
    var des2: String?,
    var type: String
): MultiItemEntity {
    override val itemType: Int
        get() = type.toInt()
}