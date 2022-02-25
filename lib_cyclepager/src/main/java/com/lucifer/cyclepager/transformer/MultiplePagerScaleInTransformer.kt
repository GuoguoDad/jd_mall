package com.lucifer.cyclepager.transformer

import android.view.View
import android.view.ViewParent
import androidx.viewpager2.widget.ViewPager2

import androidx.core.view.ViewCompat

import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import java.lang.IllegalStateException


class MultiplePagerScaleInTransformer(
    @param:Px private val marginPx: Int,
    private val scale: Float,
) :
    ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val viewPager = requireViewPager(page)
        val offset = position * marginPx
        if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
            if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                page.setTranslationX(offset)
            } else {
                page.setTranslationX(-offset)
            }
            page.setScaleY(1 - scale * Math.abs(position))
        } else {
            page.setTranslationY(-offset)
            page.setScaleX(1 - scale * Math.abs(position))
        }
    }

    private fun requireViewPager(page: View): ViewPager2 {
        val parent: ViewParent = page.getParent()
        val parentParent: ViewParent = parent.getParent()
        if (parent is RecyclerView && parentParent is ViewPager2) {
            return parentParent
        }
        throw IllegalStateException(
            "Expected the page view to be managed by a ViewPager2 instance.")
    }
}
