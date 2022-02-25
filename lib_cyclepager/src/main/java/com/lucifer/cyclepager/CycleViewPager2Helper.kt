package com.lucifer.cyclepager

import androidx.viewpager2.widget.ViewPager2
import com.lucifer.cyclepager.indicator.DotsIndicator
import androidx.annotation.ColorInt
import com.lucifer.cyclepager.indicator.Indicator
import com.lucifer.cyclepager.transformer.MultiplePagerScaleInTransformer
import com.lucifer.cyclepager.itemdecoration.MarginItemDecoration
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView

import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2.OffscreenPageLimit
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import kotlin.collections.ArrayList

class CycleViewPager2Helper(private val cycleViewPager2: CycleViewPager2) {
    private var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null

    @ViewPager2.Orientation
    private var orientation = ViewPager2.ORIENTATION_HORIZONTAL

    @OffscreenPageLimit
    private var limit = 1
    private var compositePageTransformer: CompositePageTransformer? = null
    private var itemDecorations: MutableList<RecyclerView.ItemDecoration>? = null
    private var pageChangeCallbacks: MutableList<OnPageChangeCallback>? = null
    private var autoTurningTime: Long = 0
    private var indicator: Indicator? = null
    fun setAdapter(@Nullable adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?): CycleViewPager2Helper {
        this.adapter = adapter
        return this
    }

    fun setOrientation(@ViewPager2.Orientation orientation: Int): CycleViewPager2Helper {
        this.orientation = orientation
        return this
    }

    fun setOffscreenPageLimit(@OffscreenPageLimit limit: Int): CycleViewPager2Helper {
        this.limit = limit
        return this
    }

    fun addPageTransformer(pageTransformer: ViewPager2.PageTransformer): CycleViewPager2Helper {
        if (compositePageTransformer == null) {
            compositePageTransformer = CompositePageTransformer()
        }
        compositePageTransformer!!.addTransformer(pageTransformer)
        return this
    }

    private fun addItemDecoration(itemDecoration: RecyclerView.ItemDecoration): CycleViewPager2Helper {
        if (itemDecorations == null) {
            itemDecorations = ArrayList()
        }
        itemDecorations!!.add(itemDecoration)
        return this
    }

    fun registerOnPageChangeCallback(callback: OnPageChangeCallback): CycleViewPager2Helper {
        if (pageChangeCallbacks == null) {
            pageChangeCallbacks = ArrayList()
        }
        pageChangeCallbacks!!.add(callback)
        return this
    }

    fun setMultiplePagerScaleInTransformer(
        nextItemVisiblePx: Int,
        currentItemHorizontalMarginPx: Int,
        scale: Float,
    ): CycleViewPager2Helper {
        addItemDecoration(MarginItemDecoration(currentItemHorizontalMarginPx))
        addPageTransformer(MultiplePagerScaleInTransformer(nextItemVisiblePx + currentItemHorizontalMarginPx,
            scale))
        return this
    }

    fun setAutoTurning(autoTurningTime: Long): CycleViewPager2Helper {
        this.autoTurningTime = autoTurningTime
        return this
    }

    fun setIndicator(@Nullable indicator: Indicator?): CycleViewPager2Helper {
        this.indicator = indicator
        return this
    }

    fun setDotsIndicator(
        radius: Float, @ColorInt selectedColor: Int,
        @ColorInt unSelectedColor: Int, dotsPadding: Float,
        leftMargin: Int, bottomMargin: Int, rightMargin: Int,
        @DotsIndicator.Direction direction: Int,
    ): CycleViewPager2Helper {
        val dotsIndicator = DotsIndicator(cycleViewPager2.context)
        dotsIndicator.setRadius(radius)
        dotsIndicator.setSelectedColor(selectedColor)
        dotsIndicator.setUnSelectedColor(unSelectedColor)
        dotsIndicator.setDotsPadding(dotsPadding)
        dotsIndicator.setLeftMargin(leftMargin)
        dotsIndicator.setBottomMargin(bottomMargin)
        dotsIndicator.setRightMargin(rightMargin)
        dotsIndicator.setDirection(direction)
        indicator = dotsIndicator
        return this
    }

    fun build() {
        cycleViewPager2.orientation = orientation
        cycleViewPager2.offscreenPageLimit = limit
        if (adapter != null) {
            cycleViewPager2.adapter = adapter
        }
        if (itemDecorations != null && !itemDecorations!!.isEmpty()) {
            for (itemDecoration in itemDecorations!!) {
                cycleViewPager2.addItemDecoration(itemDecoration)
            }
        }
        if (compositePageTransformer != null) {
            cycleViewPager2.setPageTransformer(compositePageTransformer)
        }
        if (pageChangeCallbacks != null && !pageChangeCallbacks!!.isEmpty()) {
            for (pageChangeCallback in pageChangeCallbacks!!) {
                cycleViewPager2.registerOnPageChangeCallback(pageChangeCallback)
            }
        }
        cycleViewPager2.setIndicator(indicator!!)
        if (autoTurningTime > 0) {
            cycleViewPager2.setAutoTurning(autoTurningTime)
        }
    }
}
