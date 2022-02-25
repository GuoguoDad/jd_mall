package com.example.home.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.ImageLoader
import coil.load
import com.example.home.R
import com.example.home.ui.BannerBean
import com.lucifer.cyclepager.CycleViewPager2Helper
import com.lucifer.cyclepager.adapter.CyclePagerAdapter
import com.lucifer.cyclepager.indicator.DotsIndicator
import kotlinx.android.synthetic.main.fragment_banner.view.*

class BannerView: FrameLayout {
    private var bannerList: MutableList<BannerBean> = arrayListOf()
    private lateinit var imageLoader: ImageLoader
    private val bannerAdapter by lazy { BannerAdapter() }

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
        imageLoader = Coil.imageLoader(context)
        LayoutInflater.from(context).inflate(R.layout.fragment_banner, this, true)

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val dotsRadius = resources.getDimension(R.dimen.dots_radius)
        val dotsPadding = resources.getDimension(R.dimen.dots_padding)
        val dotsBottomMargin = resources.getDimension(R.dimen.dots_bottom_margin)

        homeFragmentBanner.run {
            CycleViewPager2Helper(homeFragmentBanner)
                .setAdapter(bannerAdapter as RecyclerView.Adapter<RecyclerView.ViewHolder>?)
                .setMultiplePagerScaleInTransformer(
                    nextItemVisiblePx.toInt(),
                    currentItemHorizontalMarginPx.toInt(),
                    0.1f
                )
                .setDotsIndicator(
                    dotsRadius,
                    Color.RED,
                    Color.WHITE,
                    dotsPadding,
                    0,
                    dotsBottomMargin.toInt(),
                    0,
                    DotsIndicator.Direction.CENTER
                 )
                .setAutoTurning(12000L)
                .build()
        }
//        homeFragmentBanner.setPageTransformer(ZoomOutPageTransformer())
    }

    fun setData(data: List<BannerBean>) {
        bannerList.clear()
        bannerList.addAll(data)
        bannerAdapter.notifyDataSetChanged()
    }


    private inner class BannerAdapter: CyclePagerAdapter<ItemHolder>() {
        override val realItemCount: Int
            get() = bannerList.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            var view: View = LayoutInflater.from(parent.context).inflate(R.layout.fragment_pager, parent, false)
            return ItemHolder(view)
        }

        override fun onBindRealViewHolder(holder: ItemHolder, position: Int) {
            holder.itemView.findViewById<TextView>(R.id.tv_title).text = bannerList[position].name
            holder.itemView.findViewById<ImageView>(R.id.iv_pager).load(bannerList[position].imgUrl, imageLoader ) {
                crossfade(true)
                placeholder(R.drawable.default_img)
                error(R.drawable.default_img)
            }
        }
    }

    private inner class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}