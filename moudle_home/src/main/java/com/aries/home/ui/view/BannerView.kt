package com.aries.home.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import coil.ImageLoader
import coil.load
import com.aries.common.util.CoilUtil
import com.aries.home.R
import com.aries.home.databinding.HomeBannerBinding
import com.aries.home.ui.BannerBean
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.AlphaPageTransformer

class BannerView: FrameLayout {
    private lateinit var binding: HomeBannerBinding
    private var imageLoader: ImageLoader = CoilUtil.getImageLoader()
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        binding = HomeBannerBinding.inflate(LayoutInflater.from(this.context), this, true)
    }

    fun setData(data: List<BannerBean>) {
        binding.banner.run {
            adapter = object : BannerImageAdapter<BannerBean>(data) {
                override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerImageHolder {
                    val imageView = ImageView(parent?.context)
                    imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    imageView.scaleType = ImageView.ScaleType.FIT_XY
                    return BannerImageHolder(imageView)
                }
                override fun onBindView(holder: BannerImageHolder?, data: BannerBean?, position: Int, size: Int) {
                    holder?.imageView?.load(data?.imgUrl, imageLoader ) {
                        crossfade(true)
                        placeholder(R.drawable.default_img)
                    }
                }
            }
            indicator = CircleIndicator(this.context)
            setPageTransformer(AlphaPageTransformer())
            setIndicatorSelectedColorRes(R.color.theme_color)
            setLoopTime(6000L)
            isAutoLoop(true)
        }
    }
}