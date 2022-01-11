package com.example.news.ui.home

import android.widget.ImageView
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.news.R

class GoodsListAdapter(layoutResId: Int, data: MutableList<GoodsModel>) : BaseQuickAdapter<GoodsModel, BaseViewHolder>(layoutResId, data) {

    override fun convert(holder: BaseViewHolder, item: GoodsModel) {
        holder.setText(R.id.tv_title, item.name)
              .setText(R.id.tv_price, item.price)

        holder.getView<ImageView>(R.id.iv_img).load(item.url)
    }
}