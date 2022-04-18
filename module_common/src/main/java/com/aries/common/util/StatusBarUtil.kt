package com.aries.common.util

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.aries.common.BaseApplication
import android.os.Build
import android.view.Window

import android.view.WindowManager
import java.lang.reflect.Field


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

    /**
     * 设置状态栏文字
     */
    fun setBarTextModal(activity: Activity, dark: Boolean) {
        val decor = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }
}