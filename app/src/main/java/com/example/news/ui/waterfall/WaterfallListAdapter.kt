package com.example.news.ui.waterfall

import android.widget.ImageView
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.news.R

open class WaterfallListAdapter(layoutResId: Int, data: MutableList<GoodsBean>): BaseQuickAdapter<GoodsBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: GoodsBean) {
        holder.getView<ImageView>(R.id.waterfall_item_img).load(item.thumb)
        holder.setText(R.id.waterfall_item_title, item.name)
    }
}