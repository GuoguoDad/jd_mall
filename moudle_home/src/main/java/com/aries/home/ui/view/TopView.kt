package com.aries.home.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.aries.common.util.StatusBarUtil
import com.aries.home.R
import kotlinx.android.synthetic.main.home_top_view.view.*

class TopView: FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.home_top_view, this, true)
        container.setPadding(0, StatusBarUtil.getHeight(), 0, 0)
    }
}