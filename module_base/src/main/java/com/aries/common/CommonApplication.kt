package com.aries.common

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class CommonApplication: BaseApplication() {
    override fun init() {
        initRouter()
        initRefreshLayout()
    }

    private fun initRouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
            ARouter.printStackTrace()
        }
        ARouter.init(getContext() as Application?)
    }

    private fun initRefreshLayout() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }
    }
}