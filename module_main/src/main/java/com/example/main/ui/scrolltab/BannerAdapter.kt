package com.example.main.ui.scrolltab

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.ImageLoader
import coil.load
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder

class BannerAdapter(var data: List<BannerBean>): BannerImageAdapter<BannerBean>(data) {
    private lateinit var imageLoader: ImageLoader

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        imageLoader = Coil.imageLoader(recyclerView.context)
    }

    override fun onBindView(
        holder: BannerImageHolder?,
        data: BannerBean?,
        position: Int,
        size: Int,
    ) {
        holder?.imageView?.scaleType = ImageView.ScaleType.FIT_XY
        holder?.imageView?.load(data?.imgUrl, imageLoader) {
            crossfade(true)
            placeholder(com.example.home.R.drawable.banner01)
            error(com.example.home.R.drawable.banner01)
        }
    }
}