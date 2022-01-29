package com.example.common

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.example.common.abs.BaseApplication

class CommonApplication: BaseApplication() {
    override fun init() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
            ARouter.printStackTrace()
        }
        ARouter.init(getContext() as Application?)
    }
}