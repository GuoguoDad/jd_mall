package com.aries.main.ui.coordinator.adapter

import android.widget.ImageView
import coil.ImageLoader
import coil.load
import com.aries.common.util.CoilUtil
import com.aries.main.R
import com.aries.main.ui.coordinator.BannerBean
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder

class BannerAdapter(var data: List<BannerBean>): BannerImageAdapter<BannerBean>(data) {
    private var imageLoader: ImageLoader = CoilUtil.getImageLoader()

    override fun onBindView(
        holder: BannerImageHolder?,
        data: BannerBean?,
        position: Int,
        size: Int,
    ) {
        holder?.imageView?.scaleType = ImageView.ScaleType.FIT_XY
        holder?.imageView?.load(data?.imgUrl, imageLoader) {
            crossfade(true)
            placeholder(R.drawable.default_img)
        }
    }
}