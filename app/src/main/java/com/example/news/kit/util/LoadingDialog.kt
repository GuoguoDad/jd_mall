package com.example.news.kit.util

import android.app.Dialog
import android.content.Context
import com.example.news.R
import android.view.Gravity
import android.view.Window

import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import kotlinx.android.synthetic.main.layout_loading.*


open class LoadingDialog(context: Context) : Dialog(context, R.style.Loading) {
    private var loadingAnimation: Animation
    init {
        // 加载布局
        setContentView(R.layout.layout_loading)
        loadingAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        loadingAnimation.duration = 1500
        loadingAnimation.interpolator = LinearInterpolator()
        // 设置Dialog参数
        val window: Window? = window
        val params: WindowManager.LayoutParams? = window?.attributes
        if (params != null) {
            params.gravity = Gravity.CENTER
        }
        window?.attributes = params
    }

    override fun onBackPressed() {
        // 回调
        cancel()
        // 关闭Loading
        dismiss()
    }

    override fun show() {
        super.show()
        loadingImg.startAnimation(loadingAnimation)
    }
}