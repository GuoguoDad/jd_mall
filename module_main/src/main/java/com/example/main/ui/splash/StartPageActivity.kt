package com.example.main.ui.splash

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.common.constants.RouterPaths
import com.example.main.ui.base.BaseActivity

@Route(path = RouterPaths.SPLASH_ACTIVITY)
class StartPageActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread{
            Thread.sleep(1000)
            ARouter.getInstance().build(RouterPaths.MAIN_ACTIVITY).navigation()
            finish()
        }.start()
    }
}


