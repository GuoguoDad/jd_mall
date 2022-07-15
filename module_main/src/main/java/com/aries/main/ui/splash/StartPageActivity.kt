package com.aries.main.ui.splash

import android.app.Activity
import android.os.Bundle
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavigationCallback
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
            ARouter.getInstance().build(RouterPaths.MAIN_ACTIVITY)
                .withTransition(0, R.anim.fade_out)
                .navigation(this, object : NavigationCallback {
                    override fun onFound(postcard: Postcard?) {}
                    override fun onLost(postcard: Postcard?) {}
                    override fun onArrival(postcard: Postcard?) {
                        finish()
                    }
                    override fun onInterrupt(postcard: Postcard?) {}
                })
        }.start()
    }
}


