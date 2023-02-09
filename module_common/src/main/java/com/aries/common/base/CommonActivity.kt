package com.aries.common.base

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.Field

abstract class CommonActivity: AppCompatActivity() {

    protected fun setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                val decorViewClazz = Class.forName("com.android.internal.policy.DecorView")
                val field: Field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor")
                field.isAccessible = true
                field.setInt(window.decorView, Color.TRANSPARENT) //设置透明
            } catch (_: Exception) {
            }
        }
    }
}