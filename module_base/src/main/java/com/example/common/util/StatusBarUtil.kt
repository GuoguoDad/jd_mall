package com.example.common.util

import com.example.common.BaseApplication

object StatusBarUtil {
    fun getHeight(): Int {
        var result = 0
        val resourceId: Int = BaseApplication.getContext().resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = PixelUtil.toDIPFromPixel(BaseApplication.getContext().resources.getDimensionPixelSize(resourceId).toFloat()).toInt()
        }
        return result
    }
}