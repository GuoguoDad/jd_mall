package com.example.main.ui.waterfall

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.common.util.CoilUtil
import com.example.main.R
import com.example.main.kit.util.ScreenUtil

open class WaterfallListAdapter(layoutResId: Int, data: MutableList<GoodsBean>): BaseQuickAdapter<GoodsBean, BaseViewHolder>(layoutResId, data) {
    private var imageLoader: ImageLoader = CoilUtil.getImageLoader()
    private var imageWidth: Int = 0

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        imageWidth = ScreenUtil.getWindowWidth(context) / 2 - 4
    }

    override fun convert(holder: BaseViewHolder, item: GoodsBean) {
        var height = item.height * imageWidth / item.width
        holder.getView<ImageView>(R.id.waterfall_item_img).layoutParams = LinearLayout.LayoutParams(imageWidth, height)
        holder.getView<ImageView>(R.id.waterfall_item_img).load(item.thumb, imageLoader ) {
            crossfade(true)
            placeholder(R.drawable.default_img)
            error(R.drawable.default_img)
        }
        holder.setText(R.id.waterfall_item_title, item.name)
    }
}