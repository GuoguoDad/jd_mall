package com.aries.common.widget

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.widget.NestedScrollView

class AnimationNestedScrollView : NestedScrollView {
    private var listener: OnAnimationScrollChangeListener? = null

    constructor(@NonNull context: Context) : super(context)
    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet?) : super(context,attrs)
    constructor(
        @NonNull context: Context,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int,
    ) : super(context, attrs, defStyleAttr)

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        listener?.onScrollChanged(scrollY * 0.65f) //x0.65 使位移效果更加平滑 解决手指按住停留时抖动问题
    }

    fun setOnAnimationScrollListener(listener: OnAnimationScrollChangeListener?) {
        this.listener = listener
    }

    interface OnAnimationScrollChangeListener {
        fun onScrollChanged(dy: Float)
    }
}
