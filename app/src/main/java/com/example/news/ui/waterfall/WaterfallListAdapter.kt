package com.example.news.ui.waterfall

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.news.R

open class WaterfallListAdapter(layoutResId: Int, data: MutableList<ProductBean>): BaseQuickAdapter<ProductBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(holder: BaseViewHolder, item: ProductBean) {
        holder.setImageResource(R.id.waterfall_item_img, item.img).setText(R.id.waterfall_item_title, item.title)
    }
}