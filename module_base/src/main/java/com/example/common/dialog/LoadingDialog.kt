package com.example.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import com.example.common.R
import android.view.Gravity
import android.view.Window

import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import kotlinx.android.synthetic.main.loading_dialog.*

open class LoadingDialog(context: Context) : Dialog(context, R.style.Loading) {
    private var loadingAnimation: Animation
//    private var spinner: AnimationDrawable
    init {
        setContentView(R.layout.loading_dialog)

        loadingAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        loadingAnimation.duration = 1500
        loadingAnimation.interpolator = LinearInterpolator()

//        spinner = loading_img.background as AnimationDrawable

        val window: Window? = window
        val params: WindowManager.LayoutParams? = window?.attributes
        if (params != null) {
            params.gravity = Gravity.CENTER
            // 设置背景层透明度
            params.dimAmount = 0.2f
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
        loading_img.startAnimation(loadingAnimation)
//        spinner.start()
    }
}