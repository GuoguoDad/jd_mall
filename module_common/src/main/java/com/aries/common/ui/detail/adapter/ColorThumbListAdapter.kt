package com.aries.common.ui.detail.adapter

import android.widget.ImageView
import androidx.annotation.LayoutRes
import coil.load
import com.aries.common.R
import com.aries.common.ui.detail.BannerBean
import com.aries.common.util.CoilUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.orhanobut.logger.Logger

class ColorThumbListAdapter(@LayoutRes layoutResId: Int, data: MutableList<BannerBean>): BaseQuickAdapter<BannerBean, BaseViewHolder>(layoutResId, data) {
    private var imageLoader = CoilUtil.getImageLoader()

    override fun convert(holder: BaseViewHolder, bean: BannerBean) {
        Logger.i(bean.toString())
        holder.getView<ImageView>(R.id.thumbImg).load(bean.thumb, imageLoader ) {
            crossfade(true)
            placeholder(R.drawable.default_img)
        }
    }
}