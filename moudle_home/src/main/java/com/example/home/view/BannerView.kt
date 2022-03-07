package com.example.home.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import coil.Coil
import coil.ImageLoader
import coil.load
import com.example.common.util.DisplayUtil
import com.example.common.dialog.PreviewPicture
import com.example.home.R
import com.example.home.BannerBean
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.AlphaPageTransformer
import kotlinx.android.synthetic.main.view_scroll_card.view.*
import kotlin.properties.Delegates

class BannerView: FrameLayout {
    private lateinit var imageLoader: ImageLoader
    private lateinit var previewPicture: PreviewPicture
    private var imageWidth by Delegates.notNull<Int>()

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
        imageWidth = DisplayUtil.getScreenWidth(context)
        imageLoader = Coil.imageLoader(context)
        previewPicture = PreviewPicture(context)
        LayoutInflater.from(context).inflate(R.layout.view_scroll_card, this, true)
    }

    fun setData(data: List<BannerBean>) {
        banner.run {
            adapter = object : BannerImageAdapter<BannerBean>(data) {
                override fun onBindView(
                    holder: BannerImageHolder?,
                    data: BannerBean?,
                    position: Int,
                    size: Int,
                ) {
                    holder?.imageView?.scaleType = ImageView.ScaleType.FIT_XY
                    holder?.imageView?.load(data?.imgUrl, imageLoader ) {
                        crossfade(true)
                        placeholder(R.drawable.default_img)
                    }
                }
            }
            setOnBannerListener { _, position ->
                run {
                    var imgUrls = data.map { item -> item.imgUrl }
                    previewPicture.show(imgUrls, position)
                }
            }
            setBannerRound(DisplayUtil.dip2px(3f).toFloat())
            setIndicator(CircleIndicator(this.context))
            setPageTransformer(AlphaPageTransformer())
            setIndicatorSelectedColorRes(R.color.indicator_selected_color)
            setLoopTime(6000L)
            isAutoLoop(true)
        }
    }
}