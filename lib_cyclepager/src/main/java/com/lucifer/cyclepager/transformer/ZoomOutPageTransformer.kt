package com.lucifer.cyclepager.transformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ZoomOutPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        val pageWidth: Int = view.getWidth()
        val pageHeight: Int = view.getHeight()
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0f)
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
            val vertMargin = pageHeight * (1 - scaleFactor) / 2
            val horzMargin = pageWidth * (1 - scaleFactor) / 2
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2)
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2)
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor)
            view.setScaleY(scaleFactor)

            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                    (1 - MIN_SCALE) * (1 - MIN_ALPHA))
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0f)
        }
    }

    companion object {
        private const val MIN_SCALE = 0.85f
        private const val MIN_ALPHA = 0.5f
    }
}
