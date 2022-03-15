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
import com.example.home.R
import com.aries.home.ui.BannerBean
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.AlphaPageTransformer
import kotlinx.android.synthetic.main.home_banner.view.*

class BannerView: FrameLayout {
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
        LayoutInflater.from(context).inflate(R.layout.home_banner, this, true)
    }

    fun setData(data: List<BannerBean>) {
        banner.run {
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