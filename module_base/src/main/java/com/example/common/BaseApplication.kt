package com.example.common

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.airbnb.mvrx.Mavericks
import com.example.common.config.ModuleConfig
import com.example.common.impl.IBaseApplication

abstract class BaseApplication : MultiDexApplication(), IBaseApplication {
    companion object {
        private var mContext: Context? = null
        fun getContext() = mContext!!
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        initComponent()
        Mavericks.initialize(this)
    }

    private fun initComponent() {
        for (module in ModuleConfig.modules) {
            try {
                val clazz = Class.forName(module)
                val baseApplication: IBaseApplication = clazz.newInstance() as IBaseApplication
                baseApplication.init()
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            }
        }
    }
}