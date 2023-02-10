package com.aries.common.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.reflect.Field

abstract class BaseActivity<T: ViewBinding> : AppCompatActivity() {
    private lateinit var _binding: T
    protected val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentStatusBar()
        _binding = getViewBinding()
        setContentView(_binding.root)

        initView()
        initData()
    }

    protected abstract fun getViewBinding(): T

    abstract fun initView()

    abstract fun initData()

    private fun setTransparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                val decorViewClazz = Class.forName("com.android.internal.policy.DecorView")
                val field: Field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor")
                field.isAccessible = true
                field.setInt(window.decorView, Color.TRANSPARENT) //设置透明
            } catch (e: Exception) {
            }
        }
    }
}