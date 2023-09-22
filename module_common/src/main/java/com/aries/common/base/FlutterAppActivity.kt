package com.aries.common.base

import android.content.Context
import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.TransparencyMode
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor

class FlutterAppActivity: FlutterActivity() {
    override fun provideFlutterEngine(context: Context): FlutterEngine? {
        val flutterEngine = FlutterEngine(this)
        intent.getStringExtra("routeName")?.let { flutterEngine.navigationChannel.setInitialRoute(it) }
        flutterEngine.dartExecutor.executeDartEntrypoint(DartExecutor.DartEntrypoint.createDefault())

        return  flutterEngine
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).init()
    }

    override fun getTransparencyMode(): TransparencyMode {
        return TransparencyMode.transparent
    }
}