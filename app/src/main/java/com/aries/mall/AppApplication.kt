package com.aries.mall

import com.aries.common.BaseApplication
import com.aries.rn.maskhub.Constants
import com.aries.rn.maskhub.Options
import com.aries.rn.maskhub.MiniAppVersionCheckDelegate
import com.aries.rn.maskhub.navigate.NavigatePackage
import com.aries.rn.maskhub.utils.MaskLog
import com.facebook.react.ReactApplication
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.shell.MainReactPackage
import com.facebook.soloader.SoLoader
import java.io.File

class AppApplication: BaseApplication(), ReactApplication {

    override fun init() {}

    private val mReactNativeHost: ReactNativeHost = object : ReactNativeHost(this) {
        override fun getUseDeveloperSupport(): Boolean {
            return BuildConfig.DEBUG
        }

        override fun getPackages(): List<ReactPackage> {
            return listOf(
                MainReactPackage(),
                NavigatePackage()
            )
        }

        override fun getJSMainModuleName(): String {
            return "index"
        }
    }

    override fun getReactNativeHost(): ReactNativeHost {
        return mReactNativeHost
    }

    override fun onCreate() {
        super.onCreate()
        Options.me()
            .contextDir(File(this.filesDir, Constants.BUNDLE_FOLDER_PREFIX).absolutePath)
            .debug(BuildConfig.DEBUG)
            .log(MaskLog.AndroidLog()) //                .assetsBundleFileName("index.android.jsbundle")
            .delegate(MiniAppVersionCheckDelegate(this))
            .reactInstanceManager(mReactNativeHost.reactInstanceManager)
        SoLoader.init(this,  /* native exopackage */false)
    }
}