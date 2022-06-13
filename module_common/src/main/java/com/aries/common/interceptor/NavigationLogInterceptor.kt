package com.aries.common.interceptor

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.orhanobut.logger.Logger

@Interceptor(priority = 2, name = "打点拦截器")
class NavigationLogInterceptor: IInterceptor {
    private val tag = "NavigationLogInterceptor-> "
    override fun init(context: Context?) {
        Logger.e("$tag -----------NavigationLogInterceptor 初始化----------")
    }

    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        val path = postcard?.path
        val params = postcard?.extras.toString()
        Logger.e("$tag path:$path,params:$params")

        callback?.onContinue(postcard)
    }
}