package com.aries.home.ui.view

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import coil.ImageLoader
import coil.load
import com.aries.common.util.CoilUtil
import com.aries.home.R
import com.aries.home.databinding.HomeAdBinding

class AdView(context: Context): FrameLayout(context) {
    private var binding: HomeAdBinding
    private var imageLoader: ImageLoader = CoilUtil.getImageLoader()
    init {
        binding = HomeAdBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setData(url: String) {
        binding.adImg.load(url, imageLoader ) {
            crossfade(true)
            placeholder(R.drawable.default_img)
            error(R.drawable.default_img)
        }
    }
}