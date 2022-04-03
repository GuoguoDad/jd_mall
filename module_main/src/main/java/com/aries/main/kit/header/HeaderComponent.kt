package com.aries.main.kit.header

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.aries.main.R

class Header: ConstraintLayout {
    private var title: String? = null
    private var rightText: String? = null
    private var leftText: String? = null
    var titleView: TextView? = null
    var rightTextView: TextView? = null
    var leftTextView: TextView? = null

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        // 拿到对应的属性名称，记得在最后执行 types.recycle()
        val types = context.obtainStyledAttributes(attrs, R.styleable.HeaderComponent)
        title = types.getString(R.styleable.HeaderComponent_title)
        rightText = types.getString(R.styleable.HeaderComponent_rightText)
        leftText = types.getString(R.styleable.HeaderComponent_leftText)

        // 拿到我们编写的布局文件，这部分就相当于固定的部分
        val view = LayoutInflater.from(getContext()).inflate(R.layout.floating_header, this)
        titleView = view.findViewById(R.id.title)
        rightTextView = view.findViewById(R.id.rightText)
        leftTextView = view.findViewById(R.id.leftText)

        // 根据 xml 中设置的值设置
        titleView?.text = title
        rightTextView?.text = rightText
        leftTextView?.text = leftText
        types.recycle()
    }

    // 提供方便快捷的方法
    fun setTitle(title: String) {
        titleView?.text = title
    }
}