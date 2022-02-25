package com.lucifer.cyclepager.util

import android.util.Log

object Logger {
    private const val TAG = "CycleViewPager2"
    private var isDebug = true
    fun setIsDebug(isDebug: Boolean) {
        Logger.isDebug = isDebug
    }

    fun d(msg: String?) {
        if (isDebug) {
            if (msg != null) {
                Log.d(TAG, msg)
            }
        }
    }

    fun e(msg: String?) {
        if (isDebug) {
            if (msg != null) {
                Log.e(TAG, msg)
            }
        }
    }
}