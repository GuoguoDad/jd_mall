package com.lucifer.cyclepager.indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.*

import com.lucifer.cyclepager.util.DisplayUtil.dp2px

class DotsIndicator : View, Indicator {
    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mRadius = 0f
    private var mDotsPadding = 0f

    @ColorInt
    private var mSelectedColor = 0

    @ColorInt
    private var mUnSelectedColor = 0
    private var mDotsCount = 0
    private var mCurrentSelectedPosition = 0
    private var mLeftMargin = 0
    private var mBottomMargin = 0
    private var mRightMargin = 0

    @Direction
    private var mDirection = Direction.CENTER

    @IntDef(0, 1, 2)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Direction {
        companion object {
            var LEFT = 0
            var CENTER = 1
            var RIGHT = 2
        }
    }

    constructor(context: Context) : super(context) {
        initialize(context, null)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        initialize(context, attrs)
    }

    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int,
    ) : super(context, attrs, defStyleAttr) {
        initialize(context, attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context,
        @Nullable attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initialize(context, attrs)
    }

    private fun initialize(context: Context, @Nullable attrs: AttributeSet?) {
        mRadius = dp2px(3f)
        mDotsPadding = dp2px(3f)
        mSelectedColor = Color.GRAY
        mUnSelectedColor = Color.WHITE
        mRightMargin = 0
        mLeftMargin = mRightMargin
        mBottomMargin = dp2px(6f).toInt()
    }

    override val indicatorView: View
        get() {
            val layoutParams: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            when (mDirection) {
                Direction.LEFT -> layoutParams.gravity = Gravity.BOTTOM or Gravity.START
                Direction.CENTER -> layoutParams.gravity =
                    Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                Direction.RIGHT -> layoutParams.gravity = Gravity.BOTTOM or Gravity.END
            }
            layoutParams.leftMargin = mLeftMargin
            layoutParams.bottomMargin = mBottomMargin
            layoutParams.rightMargin = mRightMargin
            setLayoutParams(layoutParams)
            return this
        }

    override fun onChanged(itemCount: Int, currentPosition: Int) {
        mDotsCount = itemCount
        mCurrentSelectedPosition = currentPosition
        requestLayout()
        postInvalidate()
    }

    override fun onPageSelected(position: Int) {
        mCurrentSelectedPosition = position
        postInvalidate()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //do nothing
    }

    override fun onPageScrollStateChanged(state: Int) {
        //do nothing
    }

    fun setRadius(radius: Float) {
        mRadius = radius
    }

    fun setDotsPadding(dotsPadding: Float) {
        mDotsPadding = dotsPadding
    }

    fun setSelectedColor(@ColorInt selectedColor: Int) {
        mSelectedColor = selectedColor
    }

    fun setUnSelectedColor(@ColorInt unSelectedColor: Int) {
        mUnSelectedColor = unSelectedColor
    }

    fun setLeftMargin(leftMargin: Int) {
        mLeftMargin = leftMargin
    }

    fun setBottomMargin(bottomMargin: Int) {
        mBottomMargin = bottomMargin
    }

    fun setRightMargin(rightMargin: Int) {
        mRightMargin = rightMargin
    }

    fun setDirection(@Direction direction: Int) {
        mDirection = direction
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mDotsCount > 1) {
            val width = (mDotsCount * mRadius * 2 + (mDotsCount - 1) * mDotsPadding).toInt()
            val height = (mRadius * 2).toInt()
            setMeasuredDimension(width, height)
            return
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    protected override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mDotsCount > 1) {
            var mTempTranslateX = mRadius
            for (i in 0 until mDotsCount) {
                mPaint.setColor(if (mCurrentSelectedPosition == i) mSelectedColor else mUnSelectedColor)
                canvas.drawCircle(mTempTranslateX, mRadius, mRadius, mPaint)
                mTempTranslateX = mTempTranslateX + mRadius + mDotsPadding + mRadius
            }
        }
    }
}