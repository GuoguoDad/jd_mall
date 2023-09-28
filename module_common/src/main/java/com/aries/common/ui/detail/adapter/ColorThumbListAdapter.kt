package com.aries.common.ui.detail.adapter

import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import coil.load
import com.aries.common.R
import com.aries.common.ui.detail.BannerBean
import com.aries.common.util.CoilUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class ColorThumbListAdapter(@LayoutRes layoutResId: Int, data: MutableList<BannerBean>): BaseQuickAdapter<BannerBean, BaseViewHolder>(layoutResId, data) {
    private var imageLoader = CoilUtil.getImageLoader()

    override fun convert(holder: BaseViewHolder, item: BannerBean) {
        holder.getView<ImageView>(R.id.thumbImg).load(item.thumb, imageLoader ) {
            crossfade(true)
            placeholder(R.drawable.default_img)
        }
        holder.getView<LinearLayout>(R.id.colorOptionLayout).run {
            if (item.select!!)
                setBackgroundResource(R.drawable.detail_color_thumb_select)
            else
                setBackgroundResource(R.drawable.detail_color_thumb_unselect)
        }
    }
}