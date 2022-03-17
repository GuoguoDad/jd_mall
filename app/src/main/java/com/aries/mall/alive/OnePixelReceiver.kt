package com.aries.mall.alive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class OnePixelReceiver: BroadcastReceiver() {
    private val tag: String = "OnePx"
    private val actionName: String = "finish"

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_SCREEN_OFF -> {
                Log.e(tag, "屏幕关闭启动1像素Activity")

                var it = Intent(context, OnePxActivity::class.java)
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(it)
            }
            Intent.ACTION_SCREEN_ON -> {
                Log.e(tag, "屏幕打开 结束1像素")
                context?.sendBroadcast(Intent(actionName))
            }
        }
    }
}