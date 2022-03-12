package com.example.common.util

import com.example.common.BaseApplication

object StatusBarUtil {
    /**
     * 获取状态栏高度，单位px
     */
    fun getHeight(): Int {
        var result = 0
        val resourceId: Int = BaseApplication.getContext().resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = BaseApplication.getContext().resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}