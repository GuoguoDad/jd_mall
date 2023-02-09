package com.aries.common.dialog

import android.app.Dialog
import android.content.Context
import com.aries.common.R
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window

import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.aries.common.databinding.LoadingDialogBinding

class LoadingDialog(context: Context) : Dialog(context, R.style.Loading) {
    private var binding: LoadingDialogBinding = LoadingDialogBinding.inflate(LayoutInflater.from(context))

    private var loadingAnimation: Animation
//    private var spinner: AnimationDrawable
    init {
        setContentView(binding.root)

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
        binding.loadingImg.startAnimation(loadingAnimation)
//        spinner.start()
    }
}