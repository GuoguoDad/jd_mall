package com.aries.mall

import com.aries.common.BaseApplication
import com.aries.rn.Constants
import com.aries.rn.MiniAppVersionCheckDelegate
import com.aries.rn.Options
import com.aries.rn.navigate.NavigatePackage
import com.aries.rn.utils.MaskLog
import com.dylanvann.fastimage.FastImageViewPackage
import com.facebook.react.BuildConfig
import com.facebook.react.ReactApplication
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.shell.MainReactPackage
import com.facebook.soloader.SoLoader
import com.imagepicker.ImagePickerPackage
import com.masteratul.exceptionhandler.ReactNativeExceptionHandlerPackage
import com.swmansion.gesturehandler.RNGestureHandlerPackage
import com.swmansion.rnscreens.RNScreensPackage
import com.th3rdwave.safeareacontext.SafeAreaContextPackage
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
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
                NavigatePackage(),
                RNGestureHandlerPackage(),
                SafeAreaContextPackage(),
                RNScreensPackage(),
                ReactNativeExceptionHandlerPackage(),
                FastImageViewPackage(),
                ImagePickerPackage()
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
        SoLoader.init(this, /* native exopackage */ false);
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}