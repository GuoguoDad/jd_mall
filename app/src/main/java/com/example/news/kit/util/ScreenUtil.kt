package com.example.news.kit.util

import android.content.Context
import android.util.DisplayMetrics

open class ScreenUtil {
    companion object {
        fun getWindowDensity(context: Context): Float {
            var displayMetrics: DisplayMetrics = context.resources.displayMetrics

            return displayMetrics.scaledDensity
        }

        fun getWindowWidth(context: Context): Int {
            var displayMetrics: DisplayMetrics = context.resources.displayMetrics

            return displayMetrics.widthPixels
        }
    }
}