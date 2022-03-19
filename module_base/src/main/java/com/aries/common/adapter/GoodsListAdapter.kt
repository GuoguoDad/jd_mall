package com.aries.common.adapter

import android.widget.ImageView
import coil.load
import com.aries.common.R
import com.aries.common.bean.GoodsBean
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.aries.common.util.CoilUtil

class GoodsListAdapter(data: MutableList<GoodsBean>): BaseMultiItemQuickAdapter<GoodsBean, BaseViewHolder>(data) {
    private var imageLoader = CoilUtil.getImageLoader()

    init {
        addItemType(1, R.layout.home_goods_item)
        addItemType(2, R.layout.home_goods_second_item)
    }

    override fun convert(holder: BaseViewHolder, item: GoodsBean) {
        when (holder.itemViewType) {
            1 -> {
                holder.getView<ImageView>(R.id.img).load(item.imgUrl, imageLoader ) {
                    crossfade(true)
                    placeholder(R.drawable.default_img)
                    error(R.drawable.default_img)
                }
                holder.setText(R.id.tv_content, item.description)
                holder.setText(R.id.tv_price, "￥${item.price}")
            }
            2 -> {
                holder.getView<ImageView>(R.id.secondImg).load(item.imgUrl, imageLoader ) {
                    crossfade(true)
                    placeholder(R.drawable.default_img)
                    error(R.drawable.default_img)
                }
                holder.setText(R.id.tag, item.tag)
                holder.setText(R.id.des1, item.des1)
                holder.setText(R.id.des2, item.des2)
            }
        }
    }
}