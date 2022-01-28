package com.example.main.ui.base

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager

open class BaseActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTranslucent(this)
    }

    private fun setTranslucent(activity: Activity) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}