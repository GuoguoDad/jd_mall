package com.lucifer.cyclepager.indicator

import android.view.View

interface Indicator {
    val indicatorView: View

    fun onChanged(itemCount: Int, currentPosition: Int)
    fun onPageSelected(position: Int)
    fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
    fun onPageScrollStateChanged(state: Int)
}