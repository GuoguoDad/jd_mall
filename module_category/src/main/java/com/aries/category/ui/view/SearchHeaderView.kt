package com.aries.category.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.aries.category.databinding.SerachBinding
import com.aries.common.util.PixelUtil
import com.aries.common.util.StatusBarUtil

class SearchHeaderView: FrameLayout {
    private lateinit var binding: SerachBinding

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
        binding = SerachBinding.inflate(LayoutInflater.from(this.context), this, true)
        var statusHeight = StatusBarUtil.getHeight()

        var padding = PixelUtil.toPixelFromDIP(8f).toInt()
        binding.searchOuterView.setPadding(
            0,
            statusHeight + padding,
            0,
            padding
        )
    }
}