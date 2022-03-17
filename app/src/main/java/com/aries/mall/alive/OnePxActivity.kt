package com.aries.mall.alive

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Build.VERSION_CODES.KITKAT_WATCH
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.Gravity

class OnePxActivity: Activity() {
    private val tag: String = "OnePx"
    private val actionName: String = "finish"
    private lateinit var endReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate--->启动1像素保活")

        window.setGravity(Gravity.LEFT)
        window.attributes.x = 0
        window.attributes.y = 0
        window.attributes.height = 1
        window.attributes.width = 1

        endReceiver = object: BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                Log.d(tag, "onReceive---> finish")
                finish()
            }
        }
        registerReceiver(endReceiver, IntentFilter(actionName))
        checkScreen()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun checkScreen() {
        val pm: PowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (Build.VERSION.SDK_INT >= KITKAT_WATCH) {
            if (pm.isInteractive) {
                finish()
            }
        } else {
            if (pm.isScreenOn) {
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(endReceiver)
    }
}