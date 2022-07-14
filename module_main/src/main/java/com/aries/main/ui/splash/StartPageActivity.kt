package com.aries.main.ui.splash

import android.app.Activity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.aries.common.constants.RouterPaths
import com.aries.main.R
import com.gyf.immersionbar.ImmersionBar

@Route(path = RouterPaths.SPLASH_ACTIVITY)
class StartPageActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).transparentBar().init()
        Thread{
            Thread.sleep(1000)
            ARouter.getInstance().build(RouterPaths.MAIN_ACTIVITY).navigation()
            finish()
        }.start()
    }
}


