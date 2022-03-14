package com.example.home.ui.adapter

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.ImageLoader
import coil.load
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.home.R
import com.example.home.ui.GoodsBean

class GoodsListAdapter(data: MutableList<GoodsBean>): BaseMultiItemQuickAdapter<GoodsBean, BaseViewHolder>(data) {
    private lateinit var imageLoader: ImageLoader
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        imageLoader = Coil.imageLoader(context)
    }

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