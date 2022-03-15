package com.aries.common.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import coil.Coil
import coil.ImageLoader
import coil.load
import com.github.chrisbanes.photoview.PhotoView

class PreviewPictureAdapter(private val context: Context, private val urls: List<String>): PagerAdapter() {
    private var imageLoader: ImageLoader = Coil.imageLoader(context)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val photoView = PhotoView(context)
        photoView.load(urls[position], imageLoader){
            crossfade(true)
        }
        container.addView(photoView)
        return photoView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val photoView = `object` as PhotoView
        container.removeView(photoView)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return urls.size
    }
}