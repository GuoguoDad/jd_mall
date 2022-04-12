package com.aries.common.widget.consecutiveScroller

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.*
import android.view.animation.Interpolator
import android.widget.AbsListView
import android.widget.EdgeEffect
import android.widget.OverScroller
import androidx.core.view.*
import androidx.core.widget.EdgeEffectCompat
import androidx.recyclerview.widget.RecyclerView
import com.aries.common.R
import kotlin.math.abs

@SuppressLint("Recycle")
class ConsecutiveScrollerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) :
    ViewGroup(context, attrs, defStyleAttr), ScrollingView, NestedScrollingParent2,
    NestedScrollingChild2 {
    /**
     * 记录布局垂直的偏移量，它是包括了自己的偏移量(mScrollY)和所有子View的偏移量的总和，
     * 这个值不是真实的布局滑动偏移量，只是用于在滑动是记录和计算每次的滑动距离。
     */
    private var mSecondScrollY = 0

    /**
     * 联动容器可滚动的范围
     */
    var mScrollRange = 0

    /**
     * 联动容器滚动定位子view
     */
    private val mScroller: OverScroller

    /**
     * VelocityTracker
     */
    private var mVelocityTracker: VelocityTracker? = null
    private var mAdjustVelocityTracker: VelocityTracker? = null
    private var mAdjustYVelocity = 0

    /**
     * MaximumVelocity
     */
    private val mMaximumVelocity: Int

    /**
     * MinimumVelocity
     */
    private val mMinimumVelocity: Int
    private val mTouchSlop: Int

    /**
     * 手指触摸屏幕时的触摸点
     */
    private var mTouchY = 0
    private var mEventX = 0
    private var mEventY = 0
    private val mFixedYMap = HashMap<Int, Float?>()

    /**
     * 记录手指按下时的位置
     */
    private val mDownLocation = IntArray(2)

    /**
     * 是否处于状态
     */
    private var mTouching = false
    private var scrollOrientation = SCROLL_NONE
    /**
     * 滑动监听
     */
    private var onVerticalScrollChangeListener: OnScrollChangeListener? = null
    private var mActivePointerId = -1
    private val mParentHelper: NestedScrollingParentHelper
    private val mChildHelper: NestedScrollingChildHelper
    private val mScrollOffset = IntArray(2)
    private val mScrollConsumed = IntArray(2)
    private var mScrollToTopView: View? = null
    private var mAdjust = 0

    /**
     * 滑动到指定view，目标view的index
     */
    private var mScrollToIndex = -1

    /**
     * 滑动到指定view，平滑滑动时，每次滑动的距离
     */
    private var mSmoothScrollOffset = 0
    private var mScrollToIndexWithOffset = 0

    // 滑动到指定view时，为了防止滑动时间长或者死循环，限制最大循环次数
    private var mCycleCount = 0

    /**
     * 上边界阴影
     */
    private var mEdgeGlowTop: EdgeEffect? = null

    /**
     * 下边界阴影
     */
    private var mEdgeGlowBottom: EdgeEffect? = null

    /**
     * fling时，保存最后的滑动位置，在下一帧时通过对比新的滑动位置，判断滑动的方向。
     */
    private var mLastScrollerY = 0

    /**
     * 吸顶view是否常驻，不被推出屏幕
     */
    private var isPermanent = false
    /**
     * 禁用子view的水平滑动，如果ConsecutiveScrollerLayout下没有水平滑动的下级view，应该把它设置为true
     * 为true时，将不会分发滑动事件给子view，而是由ConsecutiveScrollerLayout处理，可以优化ConsecutiveScrollerLayout的滑动
     * 注意：如果你的ConsecutiveScrollerLayout下使用了ViewPager、HorizontalScrollView、水平滑动RecyclerView等，
     * 就不要设置disableChildHorizontalScroll为true.因为它会禁止水平滑动
     *
     */
    /**
     * 禁用子view的水平滑动，如果ConsecutiveScrollerLayout下没有水平滑动的下级view，应该把它设置为true
     * 为true时，将不会分发滑动事件给子view，而是由ConsecutiveScrollerLayout处理，可以优化ConsecutiveScrollerLayout的滑动
     */
    private var isDisableChildHorizontalScroll = false

    /**
     * 自动调整底部view的高度，使它不被吸顶布局覆盖。
     * 为true时，底部view的最大高度不大于 (父布局高度 - (当前吸顶view高度总高度 + mAdjustHeightOffset))
     */
    private var mAutoAdjustHeightAtBottomView = false

    /**
     * 自动调整底部view的高度时，额外的偏移量
     * 底部view需要调整高度 = 当前吸顶view高度总高度 + mAdjustHeightOffset
     */
    private var mAdjustHeightOffset = 0

    /**
     * 吸顶view到顶部的偏移量
     */
    private var mStickyOffset = 0
    /**
     * 获取正在吸顶的view
     *
     * @return
     */
    /**
     * 保存当前吸顶的view(普通吸顶模式中，正在吸顶的view只有一个)
     */
    private var currentStickyView: View? = null
        private set

    /**
     * 保存当前吸顶的view(常驻吸顶模式中，正在吸顶的view可能有多个)
     */
    private val mCurrentStickyViews: MutableList<View> = ArrayList()

    // 临时保存吸顶的view，用于判断吸顶view是否改变了
    private val mTempStickyViews: MutableList<View> = ArrayList()
    private var mOldScrollY = 0
    private val mViews: MutableList<View> = ArrayList()
    private var mNestedYOffset = 0
    /**
     * 普通吸顶模式,监听吸顶变化
     *
     */
    /**
     * 普通吸顶模式,监听吸顶变化
     */
    private var onStickyChangeListener: OnStickyChangeListener? = null

    /**
     * 常驻吸顶模式,监听吸顶变化
     */
    private var onPermanentStickyChangeListener: OnPermanentStickyChangeListener? = null
    private var mScrollState = SCROLL_STATE_IDLE

    /**
     * 是否触摸吸顶view并且不能触发布局滑动
     * 注意：它不仅会判断自己的吸顶view，也会判断下级ConsecutiveScrollerLayout的吸顶view
     */
    private var isTouchNotTriggerScrollStick = false

    /**
     * 在快速滑动的过程中，触摸停止滑动
     */
    private var isBrake = false
    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (params is LayoutParams) {
            LayoutParamsUtils.invalidTopAndBottomMargin(params)
        }
        super.addView(child, index, params)

        // 去掉子View的滚动条。选择在这里做这个操作，而不是在onFinishInflate方法中完成，是为了兼顾用代码add子View的情况
        if (ScrollUtils.isConsecutiveScrollerChild(child)) {
            val scrollChild: View? = ScrollUtils.getScrollChild(child)
            if (scrollChild != null) {
                disableChildScroll(scrollChild)
            }
            if (scrollChild is IConsecutiveScroller) {
                val views: List<View> = (scrollChild as IConsecutiveScroller).scrolledViews
                if (views.isNotEmpty()) {
                    val size = views.size
                    for (i in 0 until size) {
                        disableChildScroll(views[i])
                    }
                }
            }
        }
        if (child is ViewGroup) {
            child.clipToPadding = false
        }
    }

    /**
     * 禁用子view的一下滑动相关的属性
     *
     * @param child
     */
    private fun disableChildScroll(child: View) {
        child.isVerticalScrollBarEnabled = false
        child.isHorizontalScrollBarEnabled = false
        child.overScrollMode = OVER_SCROLL_NEVER
        ViewCompat.setNestedScrollingEnabled(child, false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        resetScrollToTopView()
        var contentWidth = 0
        var contentHeight = 0

        // 测量子view
        val children = nonGoneChildren
        val count = children.size
        for (i in 0 until count) {
            val child = children[i]

            // 父布局额外占用的高度空间，在测量子view时，子view的最大高度不大于父view的最大可用高度-heightUsed。

            // 测量底部view，并且需要自动调整高度时，计算吸顶部分占用的空间高度，作为测量子view的条件。
            val heightUsed: Int = getAdjustHeightForChild(child)
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, heightUsed)
            contentWidth = contentWidth.coerceAtLeast(getContentWidth(child))
            contentHeight += child.measuredHeight
        }
        setMeasuredDimension(measureSize(widthMeasureSpec,
            contentWidth + paddingLeft + paddingRight),
            measureSize(heightMeasureSpec, contentHeight + paddingTop + paddingBottom))
    }

    /**
     * 返回底部view需要调整的高度
     * 只有mAutoAdjustHeightAtBottomView=true，并且child是底部view时有值，否则返回0
     *
     * @param child
     * @return
     */
    private fun getAdjustHeightForChild(child: View): Int {
        return if (mAutoAdjustHeightAtBottomView && child === getChildAt(childCount - 1)) {
            adjustHeight
        } else 0
    }// 过滤下沉吸顶View// 普通吸顶模式// 过滤下沉吸顶View// 常驻吸顶模式

    /**
     * 返回底部view需要调整的高度
     * 普通吸顶模式：最后的吸顶view高度 + mAdjustHeightOffset
     * 常驻吸顶模式：所有吸顶view高度 + mAdjustHeightOffset
     * 需要过滤下沉吸顶
     *
     * @return
     */
    private val adjustHeight: Int
        get() {
            val children = stickyChildren
            var adjustHeight = mAdjustHeightOffset
            val count = children.size
            if (isPermanent) {
                // 常驻吸顶模式
                for (i in 0 until count) {
                    val child = children[i]
                    if (!isSink(child)) { // 过滤下沉吸顶View
                        adjustHeight += child.measuredHeight
                    }
                }
            } else {
                // 普通吸顶模式
                for (i in count - 1 downTo 0) {
                    val child = children[i]
                    if (!isSink(child)) { // 过滤下沉吸顶View
                        adjustHeight += child.measuredHeight
                        break
                    }
                }
            }
            return adjustHeight
        }

    private fun getContentWidth(child: View): Int {
        var contentWidth = child.measuredWidth
        val params: MarginLayoutParams = child.layoutParams as LayoutParams
        contentWidth += params.leftMargin
        contentWidth += params.rightMargin
        return contentWidth
    }

    private fun measureSize(measureSpec: Int, size: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = size
            if (specMode == MeasureSpec.AT_MOST) {
                result = result.coerceAtMost(specSize)
            }
        }
        result = result.coerceAtLeast(suggestedMinimumWidth)
        result = resolveSizeAndState(result, measureSpec, 0)
        return result
    }

    override fun measureChildWithMargins(
        child: View,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int,
    ) {
        LayoutParamsUtils.invalidTopAndBottomMargin(child.layoutParams as LayoutParams)
        super.measureChildWithMargins(child,
            parentWidthMeasureSpec,
            widthUsed,
            parentHeightMeasureSpec,
            heightUsed)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mScrollRange = 0
        var childTop = paddingTop
        val paddingLeft = paddingLeft
        val paddingRight = paddingRight
        val parentWidth = measuredWidth
        val children = nonGoneChildren
        val count = children.size
        for (i in 0 until count) {
            val child = children[i]
            val bottom = childTop + child.measuredHeight
            val left = getChildLeft(child, parentWidth, paddingLeft, paddingRight)
            child.layout(left, childTop, left + child.measuredWidth, bottom)
            childTop = bottom
            // 联动容器可滚动最大距离
            mScrollRange += child.height
        }
        // 联动容器可滚动range
        mScrollRange -= measuredHeight - paddingTop - paddingBottom
        // mScrollRange不能少于0
        if (mScrollRange < 0) {
            mScrollRange = 0
        }

        // 布局发生变化，检测滑动位置
        checkLayoutChange(changed, false)
        sortViews()
    }

    override fun requestLayout() {
        super.requestLayout()
    }

    private fun sortViews() {
        val list: MutableList<View> = ArrayList()
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (!isStickyView(child) || isSink(child)) {
                list.add(child)
            }
        }
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (isStickyView(child) && !isSink(child)) {
                list.add(child)
            }
        }
        mViews.clear()
        mViews.addAll(list)
    }

    /**
     * 获取子view的left
     */
    private fun getChildLeft(
        child: View,
        parentWidth: Int,
        paddingLeft: Int,
        paddingRight: Int,
    ): Int {
        val lp = child.layoutParams as LayoutParams
        return when (lp.align) {
            LayoutParams.Align.RIGHT -> parentWidth - child.measuredWidth - paddingRight - lp.rightMargin
            LayoutParams.Align.CENTER -> paddingLeft + lp.leftMargin + (parentWidth - child.measuredWidth
                    - paddingLeft - lp.leftMargin - paddingRight - lp.rightMargin) / 2
            LayoutParams.Align.LEFT -> paddingLeft + lp.leftMargin
        }
    }

    private fun resetScrollToTopView() {
        mScrollToTopView = findFirstVisibleView()
        if (mScrollToTopView != null) {
            mAdjust = scrollY - mScrollToTopView!!.top
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): LayoutParams {
        return LayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val actionIndex = ev.actionIndex
        if (scrollOrientation == SCROLL_HORIZONTAL) {
            // 如果是横向滑动，设置ev的y坐标始终为开始的坐标，避免子view自己消费了垂直滑动事件。
            if (mActivePointerId != -1 && mFixedYMap[mActivePointerId] != null) {
                val pointerIndex = ev.findPointerIndex(mActivePointerId)
                if (pointerIndex < 0 || pointerIndex >= ev.pointerCount) {
                    return false
                }
                ev.offsetLocation(0f, mFixedYMap[mActivePointerId]!! - ev.getY(pointerIndex))
            }
        }
        val vtev = MotionEvent.obtain(ev)
        if (vtev.actionMasked == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0
        }
        vtev.offsetLocation(0f, mNestedYOffset.toFloat())
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isBrake = mScrollState == SCROLL_STATE_SETTLING

                // 停止滑动
                stopScroll()
                checkTargetsScroll(isLayoutChange = false, isForce = false)
                mTouching = true
                scrollOrientation = SCROLL_NONE
                mActivePointerId = ev.getPointerId(actionIndex)
                mFixedYMap[mActivePointerId] = ev.getY(actionIndex)
                mEventY = ev.getY(actionIndex).toInt()
                mEventX = ev.getX(actionIndex).toInt()
                initOrResetAdjustVelocityTracker()
                mAdjustVelocityTracker!!.addMovement(vtev)
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH)
                mDownLocation[0] = ScrollUtils.getRawX(this, ev, actionIndex)
                mDownLocation[1] = ScrollUtils.getRawY(this, ev, actionIndex)
                isTouchNotTriggerScrollStick = ScrollUtils.isTouchNotTriggerScrollStick(this,
                    mDownLocation[0], mDownLocation[1])
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                mActivePointerId = ev.getPointerId(actionIndex)
                mFixedYMap[mActivePointerId] = ev.getY(actionIndex)
                mEventY = ev.getY(actionIndex).toInt()
                mEventX = ev.getX(actionIndex).toInt()
                // 改变滑动的手指，重新询问事件拦截
                requestDisallowInterceptTouchEvent(false)
                mDownLocation[0] = ScrollUtils.getRawX(this, ev, actionIndex)
                mDownLocation[1] = ScrollUtils.getRawY(this, ev, actionIndex)
                isTouchNotTriggerScrollStick = ScrollUtils.isTouchNotTriggerScrollStick(this,
                    mDownLocation[0], mDownLocation[1])
                initAdjustVelocityTrackerIfNotExists()
                mAdjustVelocityTracker!!.addMovement(vtev)
            }
            MotionEvent.ACTION_MOVE -> {
                val pointerIndex = ev.findPointerIndex(mActivePointerId)
                if (pointerIndex < 0 || pointerIndex >= ev.pointerCount) {
                    return false
                }
                initAdjustVelocityTrackerIfNotExists()
                mAdjustVelocityTracker!!.addMovement(vtev)
                val offsetY = ev.getY(pointerIndex).toInt() - mEventY
                val offsetX = ev.getX(pointerIndex).toInt() - mEventX
                if (scrollOrientation == SCROLL_NONE
                    && (isIntercept(ev) || isIntercept(mDownLocation[0], mDownLocation[1]))
                ) {
                    if (isDisableChildHorizontalScroll) {
                        if (abs(offsetY) >= mTouchSlop) {
                            scrollOrientation = SCROLL_VERTICAL
                        }
                    } else {
                        if (abs(offsetX) > abs(offsetY)) {
                            if (abs(offsetX) >= mTouchSlop) {
                                scrollOrientation = SCROLL_HORIZONTAL
                                // 如果是横向滑动，设置ev的y坐标始终为开始的坐标，避免子view自己消费了垂直滑动事件。
                                if (mActivePointerId != -1 && mFixedYMap[mActivePointerId] != null) {
                                    val pointerIn = ev.findPointerIndex(mActivePointerId)
                                    if (pointerIn >= 0 && pointerIndex < ev.pointerCount) {
                                        ev.offsetLocation(0f,
                                            mFixedYMap[mActivePointerId]?.minus(ev.getY(pointerIn)) ?: 0f)
                                    }
                                }
                            }
                        } else {
                            if (abs(offsetY) >= mTouchSlop) {
                                scrollOrientation = SCROLL_VERTICAL
                            }
                        }
                    }
                    if (scrollOrientation == SCROLL_NONE) {
                        return true
                    }
                }
                mEventY = ev.getY(pointerIndex).toInt()
                mEventX = ev.getX(pointerIndex).toInt()
            }
            MotionEvent.ACTION_POINTER_UP -> {
                mFixedYMap.remove(ev.getPointerId(actionIndex))
                if (mActivePointerId == ev.getPointerId(actionIndex)) { // 如果松开的是活动手指, 让还停留在屏幕上的最后一根手指作为活动手指
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    // pointerIndex都是像0, 1, 2这样连续的
                    val newPointerIndex = if (actionIndex == 0) 1 else 0
                    mActivePointerId = ev.getPointerId(newPointerIndex)
                    mFixedYMap[mActivePointerId] = ev.getY(newPointerIndex)
                    mEventY = ev.getY(newPointerIndex).toInt()
                    mEventX = ev.getX(newPointerIndex).toInt()
                    mDownLocation[0] = ScrollUtils.getRawX(this, ev, newPointerIndex)
                    mDownLocation[1] = ScrollUtils.getRawY(this, ev, newPointerIndex)
                    isTouchNotTriggerScrollStick = ScrollUtils.isTouchNotTriggerScrollStick(this,
                        mDownLocation[0], mDownLocation[1])
                }
                initAdjustVelocityTrackerIfNotExists()
                mAdjustVelocityTracker!!.addMovement(vtev)
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (mAdjustVelocityTracker != null) {
                    mAdjustVelocityTracker!!.addMovement(vtev)
                    mAdjustVelocityTracker!!.computeCurrentVelocity(1000,
                        mMaximumVelocity.toFloat())
                    val yVelocity = mAdjustVelocityTracker!!.yVelocity.toInt()
                    // 记录AdjustVelocity的fling速度
                    mAdjustYVelocity =
                        (-mMaximumVelocity).coerceAtLeast(yVelocity.coerceAtMost(mMaximumVelocity))
                    recycleAdjustVelocityTracker()
                    val touchX: Int = ScrollUtils.getRawX(this, ev, actionIndex)
                    val touchY: Int = ScrollUtils.getRawY(this, ev, actionIndex)
                    val targetView = getTouchTarget(touchX, touchY)
                    val canScrollVerticallyChild: Boolean =
                        ScrollUtils.canScrollVertically(targetView)
                    val canScrollHorizontallyChild: Boolean =
                        ScrollUtils.isHorizontalScroll(this, touchX, touchY)
                    if (scrollOrientation != SCROLL_VERTICAL && canScrollVerticallyChild
                        && abs(yVelocity) >= mMinimumVelocity && !canScrollHorizontallyChild
                    ) {
                        //如果当前是横向滑动，但是触摸的控件可以垂直滑动，并且产生垂直滑动的fling事件，
                        // 为了不让这个控件垂直fling，把事件设置为MotionEvent.ACTION_CANCEL。
                        ev.action = MotionEvent.ACTION_CANCEL
                    }
                    if (scrollOrientation != SCROLL_VERTICAL && !ScrollUtils.isConsecutiveScrollParent(
                            this)
                        && isIntercept(ev) && abs(yVelocity) >= mMinimumVelocity
                    ) {
                        if (scrollOrientation == SCROLL_NONE || !canScrollHorizontallyChild) {
                            fling(-mAdjustYVelocity)
                        }
                    }
                }
                mEventY = 0
                mEventX = 0
                mTouching = false
                mDownLocation[0] = 0
                mDownLocation[1] = 0
                isTouchNotTriggerScrollStick = false
            }
        }
        vtev.recycle()
        val dispatch = super.dispatchTouchEvent(ev)
        when (ev.actionMasked) {
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                scrollOrientation = SCROLL_NONE
                mAdjustYVelocity = 0
                mFixedYMap.clear()
                mActivePointerId = -1
                if (mScroller.isFinished) {
                    scrollState = SCROLL_STATE_IDLE
                }
            }
        }
        return dispatch
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                initOrResetVelocityTracker()
                mVelocityTracker!!.addMovement(ev)
            }
            MotionEvent.ACTION_MOVE ->
                // 需要拦截事件
                if (scrollOrientation != SCROLL_HORIZONTAL
                    && (isIntercept(ev) || isIntercept(mDownLocation[0], mDownLocation[1]))
                ) {
                    return true
                }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                stopNestedScroll(ViewCompat.TYPE_TOUCH)
                if (isBrake && scrollOrientation == SCROLL_NONE) {
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (ScrollUtils.isConsecutiveScrollParent(this) // 如果父级容器设置isConsecutive：true，则自己不消费滑动
            || isTouchNotTriggerScrollStick
        ) { // 触摸正在吸顶的view，不消费滑动
            return super.onTouchEvent(ev)
        }
        val vtev = MotionEvent.obtain(ev)
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0
        }
        vtev.offsetLocation(0f, mNestedYOffset.toFloat())
        val pointerIndex = ev.findPointerIndex(mActivePointerId)
        if (pointerIndex < 0 || pointerIndex >= ev.pointerCount) {
            return false
        }
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_TOUCH)
                mTouchY = ev.getY(pointerIndex).toInt()
            }
            MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> mTouchY =
                ev.getY(pointerIndex).toInt()
            MotionEvent.ACTION_MOVE -> {
                if (mTouchY == 0) {
                    mTouchY = ev.getY(pointerIndex).toInt()
                    return true
                }
                mScrollConsumed[1] = 0
                val y = ev.getY(pointerIndex).toInt()
                var deltaY = mTouchY - y
                mTouchY = y
                if (dispatchNestedPreScroll(0,
                        deltaY,
                        mScrollConsumed,
                        mScrollOffset,
                        ViewCompat.TYPE_TOUCH)
                ) {
                    deltaY -= mScrollConsumed[1]
                    ev.offsetLocation(0f, mScrollOffset[1].toFloat())
                    mNestedYOffset += mScrollOffset[1]
                    mTouchY -= mScrollOffset[1]
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                val oldScrollY = mSecondScrollY
                if (mScrollState != SCROLL_STATE_DRAGGING) {
                    var startScroll = false
                    if (canScrollVertically() && abs(deltaY) > 0) {
                        startScroll = true
                    }
                    if (startScroll) {
                        scrollState = SCROLL_STATE_DRAGGING
                    }
                }
                if (mScrollState == SCROLL_STATE_DRAGGING) {
                    dispatchScroll(deltaY)
                }
                val scrolledDeltaY = mSecondScrollY - oldScrollY
                if (scrolledDeltaY != 0) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                deltaY -= scrolledDeltaY
                if (dispatchNestedScroll(0, scrolledDeltaY, 0, deltaY, mScrollOffset,
                        ViewCompat.TYPE_TOUCH)
                ) {
                    deltaY += mScrollOffset[1]
                    mTouchY -= mScrollOffset[1]
                    mNestedYOffset += mScrollOffset[1]
                    ev.offsetLocation(0f, mScrollOffset[1].toFloat())
                    parent.requestDisallowInterceptTouchEvent(true)
                }

                // 判断是否显示边界阴影
                val range = scrollRange
                val overscrollMode = overScrollMode
                val canOverscroll = (overscrollMode == OVER_SCROLL_ALWAYS
                        || overscrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && range > 0)
                if (canOverscroll) {
                    ensureGlows()
                    val pulledToY = oldScrollY + deltaY
                    if (pulledToY < 0) {
                        // 滑动距离超出顶部边界，设置阴影
                        EdgeEffectCompat.onPull(mEdgeGlowTop!!, deltaY.toFloat() / height,
                            ev.getX(pointerIndex) / width)
                        if (!mEdgeGlowBottom!!.isFinished) {
                            mEdgeGlowBottom!!.onRelease()
                        }
                    } else if (pulledToY > range) {
                        // 滑动距离超出底部边界，设置阴影
                        EdgeEffectCompat.onPull(mEdgeGlowBottom!!, deltaY.toFloat() / height,
                            1f - ev.getX(pointerIndex)
                                    / width)
                        if (!mEdgeGlowTop!!.isFinished) {
                            mEdgeGlowTop!!.onRelease()
                        }
                    }
                    if (mEdgeGlowTop != null
                        && (!mEdgeGlowTop!!.isFinished || !mEdgeGlowBottom!!.isFinished)
                    ) {
                        ViewCompat.postInvalidateOnAnimation(this)
                    }
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                endDrag()
                mTouchY = 0
                recycleVelocityTracker()
                scrollState = SCROLL_STATE_IDLE
            }
            MotionEvent.ACTION_UP -> {
                endDrag()
                mTouchY = 0
                if (mVelocityTracker != null) {
                    mVelocityTracker!!.addMovement(vtev)
                    mVelocityTracker!!.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                    var yVelocity = mVelocityTracker!!.yVelocity.toInt()
                    yVelocity =
                        (-mMaximumVelocity).coerceAtLeast(yVelocity.coerceAtMost(mMaximumVelocity))
                    if (yVelocity == 0 && mAdjustYVelocity != 0) {
                        // 如果VelocityTracker没有检测到fling速度，并且mAdjustYVelocity记录到速度，就已mAdjustYVelocity为准，
                        // 避免快速上下滑动时，fling失效。
                        yVelocity = mAdjustYVelocity
                    }
                    fling(-yVelocity)
                    recycleVelocityTracker()
                }
            }
        }
        if (mVelocityTracker != null) {
            mVelocityTracker!!.addMovement(vtev)
        }
        vtev.recycle()
        return true
    }

    private fun canScrollVertically(): Boolean {
        return !isScrollTop || !isScrollBottom
    }

    override fun getChildDrawingOrder(childCount: Int, drawingPosition: Int): Int {
        if (mViews.size > drawingPosition) {
            val index = indexOfChild(mViews[drawingPosition])
            if (index != -1) {
                return index
            }
        }
        return super.getChildDrawingOrder(childCount, drawingPosition)
    }

    fun getDrawingPosition(child: View): Int {
        return mViews.indexOf(child)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (mOldScrollY != scrollY) {
            mOldScrollY = scrollY
            resetSticky()
        }

        // 绘制边界阴影
        if (mEdgeGlowTop != null) {
            val scrollY = scrollY
            if (!mEdgeGlowTop!!.isFinished) {
                val restoreCount = canvas.save()
                var width = width
                var height = height
                var xTranslation = 0
                var yTranslation = scrollY
                if (clipToPadding) {
                    width -= paddingLeft + paddingRight
                    xTranslation += paddingLeft
                }
                if (clipToPadding) {
                    height -= paddingTop + paddingBottom
                    yTranslation += paddingTop
                }
                canvas.translate(xTranslation.toFloat(), yTranslation.toFloat())
                mEdgeGlowTop!!.setSize(width, height)
                if (mEdgeGlowTop!!.draw(canvas)) {
                    ViewCompat.postInvalidateOnAnimation(this)
                }
                canvas.restoreToCount(restoreCount)
            }
            if (!mEdgeGlowBottom!!.isFinished) {
                val restoreCount = canvas.save()
                var width = width
                var height = height
                var xTranslation = 0
                var yTranslation = scrollY + height
                if (clipToPadding) {
                    width -= paddingLeft + paddingRight
                    xTranslation += paddingLeft
                }
                if (clipToPadding) {
                    height -= paddingTop + paddingBottom
                    yTranslation -= paddingBottom
                }
                canvas.translate((xTranslation - width).toFloat(), yTranslation.toFloat())
                canvas.rotate(180f, width.toFloat(), 0f)
                mEdgeGlowBottom!!.setSize(width, height)
                if (mEdgeGlowBottom!!.draw(canvas)) {
                    ViewCompat.postInvalidateOnAnimation(this)
                }
                canvas.restoreToCount(restoreCount)
            }
        }
    }

    private val scrollRange: Int
        get() {
            var scrollRange = 0
            if (childCount > 0) {
                val childSize = computeVerticalScrollRange()
                val parentSpace = height - paddingTop - paddingBottom
                scrollRange = 0.coerceAtLeast(childSize - parentSpace)
            }
            return scrollRange
        }

    private fun fling(velocityY: Int) {
        if (abs(velocityY) > mMinimumVelocity) {
            if (!dispatchNestedPreFling(0f, velocityY.toFloat())) {
                val canScroll = velocityY < 0 && !isScrollTop || velocityY > 0 && !isScrollBottom
                dispatchNestedFling(0f, velocityY.toFloat(), canScroll)
                //                if (canScroll) {
                mScroller.fling(0, mSecondScrollY,
                    1, velocityY, Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE, Int.MAX_VALUE)
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, ViewCompat.TYPE_NON_TOUCH)
                scrollState = SCROLL_STATE_SETTLING
                mLastScrollerY = mSecondScrollY
                invalidate()
                //                }
            }
        }
    }

    override fun computeScroll() {
        if (mScrollToIndex != -1 && mSmoothScrollOffset != 0) {
            if (mSmoothScrollOffset in 1..1999) {
                // 逐渐加速
                mSmoothScrollOffset += 500
            }
            if (mSmoothScrollOffset < 0 && mSmoothScrollOffset > -2000) {
                // 逐渐加速
                mSmoothScrollOffset -= 500
            }
//
//            if (mSmoothScrollOffset in 1..199) {
//                // 逐渐加速
//                mSmoothScrollOffset += 50
//            }
//            if (mSmoothScrollOffset < 0 && mSmoothScrollOffset > -200) {
//                // 逐渐加速
//                mSmoothScrollOffset -= 50
//            }

            // 正在平滑滑动到某个子view
            dispatchScroll(mSmoothScrollOffset)
            mCycleCount++
            invalidate()
        } else {

            // fling
            if (mScroller.computeScrollOffset()) {
                val y = mScroller.currY
                var unconsumed = y - mLastScrollerY
                mLastScrollerY = y
                mScrollConsumed[1] = 0
                dispatchNestedPreScroll(0, unconsumed, mScrollConsumed, null,
                    ViewCompat.TYPE_NON_TOUCH)
                unconsumed -= mScrollConsumed[1]
                val oldScrollY = mSecondScrollY
                dispatchScroll(unconsumed)
                val scrolledByMe = mSecondScrollY - oldScrollY
                unconsumed -= scrolledByMe
                if (unconsumed < 0 && isScrollTop || unconsumed > 0 && isScrollBottom) {
                    dispatchNestedScroll(0, scrolledByMe, 0, unconsumed, mScrollOffset,
                        ViewCompat.TYPE_NON_TOUCH)
                    unconsumed += mScrollOffset[1]
                }

                // 判断滑动方向和是否滑动到边界
                if (unconsumed < 0 && isScrollTop || unconsumed > 0 && isScrollBottom) {
                    val mode = overScrollMode
                    val canOverscroll = (mode == OVER_SCROLL_ALWAYS
                            || mode == OVER_SCROLL_IF_CONTENT_SCROLLS && scrollRange > 0)
                    if (canOverscroll) {
                        ensureGlows()
                        if (unconsumed < 0) {
                            // 设置上边界阴影
                            if (mEdgeGlowTop!!.isFinished) {
                                mEdgeGlowTop!!.onAbsorb(mScroller.currVelocity.toInt())
                            }
                        } else {
                            // 设置下边界阴影
                            if (mEdgeGlowBottom!!.isFinished) {
                                mEdgeGlowBottom!!.onAbsorb(mScroller.currVelocity.toInt())
                            }
                        }
                    }
                    stopScroll()
                }
                invalidate()
            }
            if (mScrollState == SCROLL_STATE_SETTLING && mScroller.isFinished) {
                // 滚动结束，校验子view内容的滚动位置
                stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)
                checkTargetsScroll(isLayoutChange = false, isForce = false)
                scrollState = SCROLL_STATE_IDLE
            }
        }
    }

    private fun endDrag() {
        if (mEdgeGlowTop != null) {
            mEdgeGlowTop!!.onRelease()
            mEdgeGlowBottom!!.onRelease()
        }
    }

    private fun ensureGlows() {
        if (overScrollMode != OVER_SCROLL_NEVER) {
            if (mEdgeGlowTop == null) {
                val context = context
                mEdgeGlowTop = EdgeEffect(context)
                mEdgeGlowBottom = EdgeEffect(context)
            }
        } else {
            mEdgeGlowTop = null
            mEdgeGlowBottom = null
        }
    }

    /**
     * 分发处理滑动
     *
     * @param offset
     */
    private fun dispatchScroll(offset: Int) {
        if (offset > 0) {
            scrollUp(offset)
        } else if (offset < 0) {
            scrollDown(offset)
        }
    }

    /**
     * 向上滑动
     *
     * @param offset
     */
    private fun scrollUp(offset: Int) {
        var scrollOffset: Int
        var remainder = offset
        val oldScrollY = computeVerticalScrollOffset()
        do {
            var scrollAnchor = 0
            var viewScrollOffset = 0
            // 如果是要滑动到指定的View，判断滑动到目标位置，就停止滑动
            if (mScrollToIndex != -1) {
                val view = getChildAt(mScrollToIndex)
                scrollAnchor = view.top - mScrollToIndexWithOffset
                scrollAnchor -= getAdjustHeightForChild(view)
                if (mScrollToIndexWithOffset < 0) {
                    viewScrollOffset = getViewsScrollOffset(mScrollToIndex)
                }
                if (mCycleCount >= MAX_CYCLE_COUNT || scrollY + paddingTop + viewScrollOffset >= scrollAnchor || isScrollBottom) {
                    mScrollToIndex = -1
                    mSmoothScrollOffset = 0
                    mScrollToIndexWithOffset = 0
                    mCycleCount = 0
                    scrollState = SCROLL_STATE_IDLE
                    break
                }
            }
            scrollOffset = 0
            if (!isScrollBottom) {
                // 找到当前显示的第一个View
                val firstVisibleView: View? = if (scrollY < mScrollRange) {
                    findFirstVisibleView()
                } else {
                    bottomView
                }
                if (firstVisibleView != null) {
                    awakenScrollBars()
                    val bottomOffset: Int = ScrollUtils.getScrollBottomOffset(firstVisibleView)
                    if (bottomOffset > 0) {
                        scrollOffset = remainder.coerceAtMost(bottomOffset)
                        if (mScrollToIndex != -1) {
                            scrollOffset =
                                scrollOffset.coerceAtMost(scrollAnchor - (scrollY + paddingTop + viewScrollOffset))
                        }
                        scrollChild(firstVisibleView, scrollOffset)
                    } else {
                        scrollOffset =
                            remainder.coerceAtMost(firstVisibleView.bottom - paddingTop - scrollY)
                        if (mScrollToIndex != -1) {
                            scrollOffset =
                                scrollOffset.coerceAtMost(scrollAnchor - (scrollY + paddingTop + viewScrollOffset))
                        }
                        scrollSelf(scrollY + scrollOffset)
                    }
                    mSecondScrollY += scrollOffset
                    remainder -= scrollOffset
                }
            }
        } while (scrollOffset > 0 && remainder > 0)
        val newScrollY = computeVerticalScrollOffset()
        if (oldScrollY != newScrollY) {
            scrollChange(newScrollY, oldScrollY)
        }
    }

    private fun scrollDown(offset: Int) {
        var scrollOffset: Int
        var remainder = offset
        val oldScrollY = computeVerticalScrollOffset()
        do {
            var scrollAnchor = 0
            var viewScrollOffset = 0
            // 如果是要滑动到指定的View，判断滑动到目标位置，就停止滑动
            if (mScrollToIndex != -1) {
                val view = getChildAt(mScrollToIndex)
                scrollAnchor = view.top - mScrollToIndexWithOffset
                scrollAnchor -= getAdjustHeightForChild(view)
                viewScrollOffset = getViewsScrollOffset(mScrollToIndex)
                if (mCycleCount >= MAX_CYCLE_COUNT || scrollY + paddingTop + viewScrollOffset <= scrollAnchor || isScrollTop) {
                    mScrollToIndex = -1
                    mSmoothScrollOffset = 0
                    mScrollToIndexWithOffset = 0
                    mCycleCount = 0
                    scrollState = SCROLL_STATE_IDLE
                    break
                }
            }
            scrollOffset = 0
            if (!isScrollTop) {
                // 找到当前显示的最后一个View
                val lastVisibleView: View? = if (scrollY < mScrollRange) {
                    findLastVisibleView()
                } else {
                    bottomView
                }
                if (lastVisibleView != null) {
                    awakenScrollBars()
                    val childScrollOffset: Int = ScrollUtils.getScrollTopOffset(lastVisibleView)
                    if (childScrollOffset < 0) {
                        scrollOffset = remainder.coerceAtLeast(childScrollOffset)
                        if (mScrollToIndex != -1) {
                            scrollOffset =
                                scrollOffset.coerceAtLeast(scrollAnchor - (scrollY + paddingTop + viewScrollOffset))
                        }
                        scrollChild(lastVisibleView, scrollOffset)
                    } else {
                        val scrollY = scrollY
                        scrollOffset =
                            remainder.coerceAtLeast(lastVisibleView.top + paddingBottom - scrollY - height)
                        scrollOffset = scrollOffset.coerceAtLeast(-scrollY)
                        if (mScrollToIndex != -1) {
                            scrollOffset =
                                scrollOffset.coerceAtLeast(scrollAnchor - (getScrollY() + paddingTop + viewScrollOffset))
                        }
                        scrollSelf(scrollY + scrollOffset)
                    }
                    mSecondScrollY += scrollOffset
                    remainder -= scrollOffset
                }
            }
        } while (scrollOffset < 0 && remainder < 0)
        val newScrollY = computeVerticalScrollOffset()
        if (oldScrollY != newScrollY) {
            scrollChange(newScrollY, oldScrollY)
        }
    }

    override fun scrollBy(x: Int, y: Int) {
        scrollTo(0, mSecondScrollY + y)
    }

    override fun scrollTo(x: Int, y: Int) {
        //所有的scroll操作都交由dispatchScroll()来分发处理
        dispatchScroll(y - mSecondScrollY)
    }

    private fun scrollChange(scrollY: Int, oldScrollY: Int) {
        if (onVerticalScrollChangeListener != null) {
            onVerticalScrollChangeListener!!.onScrollChange(this, scrollY, oldScrollY, mScrollState)
        }
    }

    private fun stickyChange(oldStickyView: View?, newStickyView: View?) {
        if (onStickyChangeListener != null) {
            onStickyChangeListener!!.onStickyChange(oldStickyView, newStickyView)
        }
    }

    private fun permanentStickyChange(mCurrentStickyViews: List<View>) {
        if (onPermanentStickyChangeListener != null) {
            onPermanentStickyChangeListener!!.onStickyChange(mCurrentStickyViews)
        }
    }

    /**
     * 滑动自己
     *
     * @param y
     */
    private fun scrollSelf(y: Int) {
        var scrollY = y

        // 边界检测
        if (scrollY < 0) {
            scrollY = 0
        } else if (scrollY > mScrollRange) {
            scrollY = mScrollRange
        }
        super.scrollTo(0, scrollY)
    }

    private fun scrollChild(child: View, y: Int) {
        val scrolledView: View? = ScrollUtils.getScrolledView(child)
        if (scrolledView is AbsListView) {
            scrolledView.scrollListBy(y)
        } else {
            var isInterceptRequestLayout = false
            if (scrolledView is RecyclerView) {
                isInterceptRequestLayout = ScrollUtils.startInterceptRequestLayout(scrolledView)
            }
            scrolledView?.scrollBy(0, y)
            if (isInterceptRequestLayout) {
                val view = scrolledView as RecyclerView
                view.postDelayed({ ScrollUtils.stopInterceptRequestLayout(view) }, 0)
            }
        }
    }

    fun checkLayoutChange() {
        postDelayed({ checkLayoutChange(changed = false, isForce = true) }, 20)
    }

    /**
     * 布局发生变化，重新检查所有子View是否正确显示
     */
    private fun checkLayoutChange(changed: Boolean, isForce: Boolean) {
        val y = mSecondScrollY
        if (mScrollToTopView != null && changed) {
            if (indexOfChild(mScrollToTopView) != -1) {
                scrollSelf(mScrollToTopView!!.top + mAdjust)
            }
        } else {
            scrollSelf(scrollY)
        }
        checkTargetsScroll(true, isForce)

        // 如果正在显示的子布局和滑动偏移量mScrollToTopView都改变了，则有可能是因为布局发生改变，并且影响到正在显示的布局部分。
        // scrollTo(0, y)把布局的滑动位置恢复为原来的mScrollToTopView，可以避免正在显示的布局显示异常。
        // 注意：由于RecyclerView的computeVerticalScrollOffset()方法计算到的不是真实的滑动偏移量，而是根据item平均高度估算的值。
        // 所以当RecyclerView的item高度不一致时，可能会导致mScrollToTopView计算的偏移量和布局的实际偏移量不一致，从而导致scrollTo(0, y)恢复
        // 原滑动位置时产生上下偏移的误差。
        if (y != mSecondScrollY && mScrollToTopView !== findFirstVisibleView()) {
            scrollTo(0, y)
        }
        mScrollToTopView = null
        mAdjust = 0
        resetChildren()
        resetSticky()
    }

    /**
     * 校验子view内容滚动位置是否正确
     */
    private fun checkTargetsScroll(isLayoutChange: Boolean, isForce: Boolean) {
        if (!isForce && (mTouching || !mScroller.isFinished || mScrollToIndex != -1)) {
            return
        }
        val oldScrollY = computeVerticalScrollOffset()
        val target = findFirstVisibleView() ?: return
        val index = indexOfChild(target)
        if (isLayoutChange) {
            while (true) {
                val bottomOffset: Int = ScrollUtils.getScrollBottomOffset(target)
                val scrollTopOffset = target.top - scrollY
                if (bottomOffset > 0 && scrollTopOffset < 0) {
                    val offset = bottomOffset.coerceAtMost(-scrollTopOffset)
                    scrollSelf(scrollY - offset)
                    scrollChild(target, offset)
                } else {
                    break
                }
            }
        }
        for (i in 0 until index) {
            val child = getChildAt(i)
            if (child.visibility == GONE) {
                continue
            }
            if (ScrollUtils.isConsecutiveScrollerChild(child)) {
                val scrollChild: View? = ScrollUtils.getScrollChild(child)
                if (scrollChild is IConsecutiveScroller) {
                    val views: List<View> = (scrollChild as IConsecutiveScroller).scrolledViews
                    if (views.isNotEmpty()) {
                        val size = views.size
                        for (c in 0 until size) {
                            scrollChildContentToBottom(views[c])
                        }
                    }
                } else {
                    if (scrollChild != null) {
                        scrollChildContentToBottom(scrollChild)
                    }
                }
            }
        }
        for (i in index + 1 until childCount) {
            val child = getChildAt(i)
            if (child.visibility == GONE) {
                continue
            }
            if (ScrollUtils.isConsecutiveScrollerChild(child)) {
                if (i == childCount - 1 && child.height < this.height && scrollY >= mScrollRange) {
                    continue
                }
                val scrollChild: View? = ScrollUtils.getScrollChild(child)
                if (scrollChild is IConsecutiveScroller) {
                    val views: List<View> = (scrollChild as IConsecutiveScroller).scrolledViews
                    if (views.isNotEmpty()) {
                        val size = views.size
                        for (c in 0 until size) {
                            scrollChildContentToTop(views[c])
                        }
                    }
                } else {
                    if (scrollChild != null) {
                        scrollChildContentToTop(scrollChild)
                    }
                }
            }
        }
        computeOwnScrollOffset()
        if (isLayoutChange) {
            val newScrollY = computeVerticalScrollOffset()
            if (oldScrollY != newScrollY) {
                scrollChange(newScrollY, oldScrollY)
            }
        }
        resetSticky()
    }

    /**
     * 滚动指定子view的内容到顶部
     *
     * @param target
     */
    fun scrollChildContentToTop(target: View) {
        var scrollY: Int
        do {
            scrollY = 0
            val offset: Int = ScrollUtils.getScrollTopOffset(target)
            if (offset < 0) {
                val childOldScrollY: Int = ScrollUtils.computeVerticalScrollOffset(target)
                scrollChild(target, offset)
                scrollY = childOldScrollY - ScrollUtils.computeVerticalScrollOffset(target)
            }
        } while (scrollY != 0)
    }

    /**
     * 滚动指定子view的内容到底部
     *
     * @param target
     */
    fun scrollChildContentToBottom(target: View) {
        var scrollY: Int
        do {
            scrollY = 0
            val offset: Int = ScrollUtils.getScrollBottomOffset(target)
            if (offset > 0) {
                val childOldScrollY: Int = ScrollUtils.computeVerticalScrollOffset(target)
                scrollChild(target, offset)
                scrollY = childOldScrollY - ScrollUtils.computeVerticalScrollOffset(target)
            }
        } while (scrollY != 0)
    }

    /**
     * 重新计算mOwnScrollY
     *
     * @return
     */
    private fun computeOwnScrollOffset() {
        mSecondScrollY = computeVerticalScrollOffset()
    }

    /**
     * 初始化VelocityTracker
     */
    private fun initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        } else {
            mVelocityTracker!!.clear()
        }
    }

    /**
     * 初始化VelocityTracker
     */
    private fun initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    /**
     * 回收VelocityTracker
     */
    private fun recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker!!.recycle()
            mVelocityTracker = null
        }
    }

    /**
     * 初始化VelocityTracker
     */
    private fun initOrResetAdjustVelocityTracker() {
        if (mAdjustVelocityTracker == null) {
            mAdjustVelocityTracker = VelocityTracker.obtain()
        } else {
            mAdjustVelocityTracker!!.clear()
        }
    }

    /**
     * 初始化VelocityTracker
     */
    private fun initAdjustVelocityTrackerIfNotExists() {
        if (mAdjustVelocityTracker == null) {
            mAdjustVelocityTracker = VelocityTracker.obtain()
        }
    }

    /**
     * 回收VelocityTracker
     */
    private fun recycleAdjustVelocityTracker() {
        if (mAdjustVelocityTracker != null) {
            mAdjustVelocityTracker!!.recycle()
            mAdjustVelocityTracker = null
        }
    }

    /**
     * 停止滑动
     */
    fun stopScroll() {
        if (!mScroller.isFinished) {
            mScroller.abortAnimation()
            stopNestedScroll(ViewCompat.TYPE_NON_TOUCH)
            if (mScrollToIndex == -1) {
                scrollState = SCROLL_STATE_IDLE
            }
        }
    }

    private val bottomView: View?
        get() {
            val views = effectiveChildren
            return if (views.isNotEmpty()) {
                views[views.size - 1]
            } else null
        }

    /**
     * 返回所有的非GONE子View
     *
     * @return
     */
    private val nonGoneChildren: List<View>
        get() {
            val children: MutableList<View> = ArrayList()
            val count = childCount
            for (i in 0 until count) {
                val child = getChildAt(i)
                if (child.visibility != GONE) {
                    children.add(child)
                }
            }
            return children
        }

    /**
     * 返回所有高度不为0的view
     */
    private val effectiveChildren: List<View>
        get() {
            val children: MutableList<View> = ArrayList()
            val count = childCount
            for (i in 0 until count) {
                val child = getChildAt(i)
                if (child.visibility != GONE && child.height > 0) {
                    children.add(child)
                }
            }
            return children
        }

    /**
     * 返回所有的吸顶子View(非GONE)
     *
     * @return
     */
    private val stickyChildren: List<View>
        get() {
            val children: MutableList<View> = ArrayList()
            val count = childCount
            for (i in 0 until count) {
                val child = getChildAt(i)
                if (child.visibility != GONE && isStickyView(child)) {
                    children.add(child)
                }
            }
            return children
        }

    /**
     * 是否是需要吸顶的View
     *
     * @param child
     * @return
     */
    fun isStickyView(child: View): Boolean {
        val lp = child.layoutParams
        return if (lp is LayoutParams) {
            lp.isSticky
        } else false
    }

    /**
     * 吸顶view是否是下沉模式
     *
     * @param stickyView
     * @return
     */
    fun isSink(stickyView: View): Boolean {
        val lp = stickyView.layoutParams
        return if (lp is LayoutParams) {
            lp.isSink
        } else false
    }

    /**
     * 布局发生变化，可能是某个吸顶布局的isSticky发生改变，需要重新重置一下所有子View的translationY、translationZ
     */
    private fun resetChildren() {
        val children = nonGoneChildren
        for (child in children) {
            child.translationY = 0f
        }
    }

    /**
     * 重置吸顶
     */
    private fun resetSticky() {
        val children = stickyChildren
        if (children.isNotEmpty()) {
            val count = children.size
            // 让所有的View恢复原来的状态
            for (i in 0 until count) {
                val child = children[i]
                child.translationY = 0f
            }
            if (isPermanent) { //常驻
                clearCurrentStickyView()
                permanentStickyChild(children)
            } else {
                clearCurrentStickyViews()

                // 需要吸顶的View
                var stickyView: View? = null
                // 下一个需要吸顶的View
                var nextStickyView: View? = null

                // 找到需要吸顶的View
                for (i in count - 1 downTo 0) {
                    val child = children[i]
                    if (child.top <= stickyY) {
                        stickyView = child
                        if (i != count - 1) {
                            nextStickyView = children[i + 1]
                        }
                        break
                    }
                }
                val oldStickyView = currentStickyView
                val newStickyView = stickyView
                if (stickyView != null) {
                    var offset = 0
                    if (nextStickyView != null && !isSink(stickyView)) {
                        offset = 0.coerceAtLeast(stickyView.height - (nextStickyView.top - stickyY))
                    }
                    stickyChild(stickyView, offset)
                }
                if (oldStickyView !== newStickyView) {
                    currentStickyView = newStickyView
                    stickyChange(oldStickyView, newStickyView)
                }
            }
        } else {
            // 没有吸顶view
            clearCurrentStickyView()
            clearCurrentStickyViews()
        }
    }

    private fun clearCurrentStickyView() {
        if (currentStickyView != null) {
            val oldStickyView = currentStickyView!!
            currentStickyView = null
            stickyChange(oldStickyView, null)
        }
    }

    private fun clearCurrentStickyViews() {
        if (mCurrentStickyViews.isNotEmpty()) {
            mCurrentStickyViews.clear()
            permanentStickyChange(mCurrentStickyViews)
        }
    }

    /**
     * 子View吸顶
     *
     * @param child
     * @param offset
     */
    private fun stickyChild(child: View, offset: Int) {
        child.y = (stickyY - offset).toFloat()

        // 把View设置为可点击的，避免吸顶View与其他子View重叠是，触摸事件透过吸顶View传递给下面的View，
        // 导致ConsecutiveScrollerLayout追踪布局的滑动出现偏差
        child.isClickable = true
    }

    /**
     * 获取吸顶的位置。
     *
     * @return
     */
    private val stickyY: Int
        get() = scrollY + paddingTop + mStickyOffset

    /**
     * 子View吸顶常驻
     *
     * @param children
     */
    private fun permanentStickyChild(children: List<View>) {
        mTempStickyViews.clear()
        for (i in children.indices) {
            val child = children[i]
            val permanentHeight = getPermanentHeight(children, i)
            if (child.top <= stickyY + permanentHeight) {
                child.y = (stickyY + permanentHeight).toFloat()
                child.isClickable = true
                mTempStickyViews.add(child)
            }
        }
        if (!isListEqual) {
            mCurrentStickyViews.clear()
            mCurrentStickyViews.addAll(mTempStickyViews)
            mTempStickyViews.clear()
            permanentStickyChange(mCurrentStickyViews)
        }
    }

    private fun getPermanentHeight(children: List<View>, currentPosition: Int): Int {
        var height = 0
        for (i in 0 until currentPosition) {
            val child = children[i]
            if (!isSink(child)) {
                height += child.measuredHeight
            }
        }
        return height
    }

    private val isListEqual: Boolean
        get() {
            if (mTempStickyViews.size == mCurrentStickyViews.size) {
                val size = mTempStickyViews.size
                for (i in 0 until size) {
                    if (mTempStickyViews[i] !== mCurrentStickyViews[i]) {
                        return false
                    }
                }
                return true
            }
            return false
        }

    /**
     * Return the current scrolling state of the RecyclerView.
     *
     * @return [.SCROLL_STATE_IDLE], [.SCROLL_STATE_DRAGGING] or
     * [.SCROLL_STATE_SETTLING]
     */
    var scrollState: Int
        get() = mScrollState
        set(state) {
            if (state == mScrollState) {
                return
            }
            mScrollState = state
            val newScrollY = computeVerticalScrollOffset()
            scrollChange(newScrollY, newScrollY)
        }

    /**
     * 使用这个方法取代View的getScrollY
     *
     * @return
     */
    val ownScrollY: Int
        get() = computeVerticalScrollOffset()

    /**
     * 找到当前显示的第一个View
     *
     * @return
     */
    fun findFirstVisibleView(): View? {
        val offset = scrollY + paddingTop
        val children = effectiveChildren
        val count = children.size
        for (i in 0 until count) {
            val child = children[i]
            if (child.top <= offset && child.bottom > offset) {
                return child
            }
        }
        return null
    }

    /**
     * 找到当前显示的最后一个View
     *
     * @return
     */
    fun findLastVisibleView(): View? {
        val offset = height - paddingBottom + scrollY
        val children = effectiveChildren
        val count = children.size
        for (i in 0 until count) {
            val child = children[i]
            if (child.top < offset && child.bottom >= offset) {
                return child
            }
        }
        return null
    }

    /**
     * 是否滑动到顶部
     *
     * @return
     */
    val isScrollTop: Boolean
        get() {
            val children = effectiveChildren
            val size = children.size
            if (size > 0) {
                val child = children[0]
                val isScrollTop = scrollY <= 0 && !ScrollUtils.canScrollVertically(child, -1)
                if (isScrollTop) {
                    for (i in size - 1 downTo 0) {
                        val view = children[i]
                        if (ScrollUtils.isConsecutiveScrollerChild(view)
                            && ScrollUtils.canScrollVertically(view, -1)
                        ) {
                            return false
                        }
                    }
                }
                return isScrollTop
            }
            return true
        }

    /**
     * 是否滑动到底部
     *
     * @return
     */
    val isScrollBottom: Boolean
        get() {
            val children = effectiveChildren
            val size = children.size
            if (size > 0) {
                val child = children[children.size - 1]
                val isScrollBottom =
                    scrollY >= mScrollRange && !ScrollUtils.canScrollVertically(child, 1)
                if (isScrollBottom) {
                    for (i in size - 1 downTo 0) {
                        val view = children[i]
                        if (ScrollUtils.isConsecutiveScrollerChild(view)
                            && ScrollUtils.canScrollVertically(view, 1)
                        ) {
                            return false
                        }
                    }
                }
                return isScrollBottom
            }
            return true
        }

    override fun canScrollVertically(direction: Int): Boolean {
        return if (direction > 0) {
            !isScrollBottom
        } else {
            !isScrollTop
        }
    }

    /**
     * 禁止设置滑动监听，因为这个监听器已无效
     * 若想监听容器的滑动，请使用
     *
     * @see .setOnVerticalScrollChangeListener
     */
    @Deprecated("")
    override fun setOnScrollChangeListener(l: View.OnScrollChangeListener) {
    }


    /**
     * 设置滑动监听
     *
     * @param l
     */
    fun setOnVerticalScrollChangeListener(l: OnScrollChangeListener) {
        this.onVerticalScrollChangeListener = l
    }

    fun getOnVerticalScrollChangeListener(): OnScrollChangeListener? {
        return onVerticalScrollChangeListener
    }

    override fun computeHorizontalScrollRange(): Int {
        return super.computeHorizontalScrollRange()
    }

    override fun computeHorizontalScrollOffset(): Int {
        return super.computeHorizontalScrollOffset()
    }

    override fun computeHorizontalScrollExtent(): Int {
        return super.computeHorizontalScrollExtent()
    }

    override fun computeVerticalScrollRange(): Int {
        var range = 0
        val children = nonGoneChildren
        val count = children.size
        for (i in 0 until count) {
            val child = children[i]
            range += if (!ScrollUtils.isConsecutiveScrollerChild(child)) {
                child.height
            } else {
                if (ScrollUtils.canScrollVertically(child)) {
                    val view: View? = ScrollUtils.getScrolledView(child)
                    ScrollUtils.computeVerticalScrollRange(view) + (view?.paddingTop ?: 0) + (view?.paddingBottom
                        ?: 0)
                } else {
                    child.height
                }
            }
        }
        return range
    }

    override fun computeVerticalScrollOffset(): Int {
        var scrollOffset = scrollY
        val children = nonGoneChildren
        val count = children.size
        for (i in 0 until count) {
            val child = children[i]
            if (ScrollUtils.isConsecutiveScrollerChild(child)) {
                scrollOffset += ScrollUtils.computeVerticalScrollOffset(child)
            }
        }
        return scrollOffset
    }

    override fun computeVerticalScrollExtent(): Int {
        return height - paddingTop - paddingBottom
    }

    //根据坐标返回触摸到的View
    private fun getTouchTarget(touchX: Int, touchY: Int): View? {
        var targetView: View? = null
        // 获取可触摸的View
        val touchableViews = nonGoneChildren
        for (touchableView in touchableViews) {
            if (ScrollUtils.isTouchPointInView(touchableView, touchX, touchY)) {
                targetView = touchableView
                break
            }
        }
        return targetView
    }

    /**
     * 判断是否需要拦截事件
     *
     * @param ev
     * @return
     */
    private fun isIntercept(ev: MotionEvent): Boolean {
        val pointerIndex = ev.findPointerIndex(mActivePointerId)
        return if (pointerIndex < 0 || pointerIndex >= ev.pointerCount) {
            // 无效的触摸，不要往下传递
            true
        } else isIntercept(ScrollUtils.getRawX(this, ev, pointerIndex),
            ScrollUtils.getRawY(this, ev, pointerIndex))
    }

    /**
     * 判断是否需要拦截事件
     *
     * @return
     */
    private fun isIntercept(touchX: Int, touchY: Int): Boolean {
        val target = getTouchTarget(touchX, touchY)
        return if (target != null) {
            ScrollUtils.isConsecutiveScrollerChild(target)
        } else false
    }

    class LayoutParams : MarginLayoutParams {
        /**
         * 是否与父布局整体滑动，设置为false时，父布局不会拦截它的事件，滑动事件将由子view处理。
         * 可以实现子view内部的垂直滑动。
         */
        var isConsecutive = true

        /**
         * 是否支持嵌套滑动，默认支持，如果子view或它内部的下级view实现了NestedScrollingChild接口，
         * 它可以与ConsecutiveScrollerLayout嵌套滑动，把isNestedScroll设置为false可以禁止它与ConsecutiveScrollerLayout嵌套滑动。
         */
        var isNestedScroll = true

        /**
         * 设置子view吸顶悬浮
         */
        var isSticky = false

        /**
         * 在View吸顶的状态下，是否可以触摸view来滑动ConsecutiveScrollerLayout布局。
         * 默认为false，则View吸顶的状态下，不能触摸它来滑动布局
         */
        var isTriggerScroll = false

        /**
         * 吸顶下沉模式
         * 默认情况下，吸顶view在吸顶状态下，会显示在布局上层，覆盖其他布局。
         * 如果设置了下沉模式，则会相反，view在吸顶时会显示在下层，被其他布局覆盖，隐藏在下面。
         */
        var isSink = false
        var scrollChild = 0

        /**
         * 子view与父布局的对齐方式
         */
        var align = Align.LEFT

        /**
         * 子view与父布局的对齐方式
         */
        enum class Align(i: Int) {
            LEFT(1),  //右对齐。
            RIGHT(2),  //中间对齐。
            CENTER(3);

            companion object {
                operator fun get(value: Int): Align {
                    when (value) {
                        1 -> return LEFT
                        2 -> return RIGHT
                        3 -> return CENTER
                    }
                    return LEFT
                }
            }
        }

        constructor(c: Context, attrs: AttributeSet?) : super(c, attrs) {
            var a: TypedArray? = null
            try {
                a = c.obtainStyledAttributes(attrs, R.styleable.ConsecutiveScrollerLayout_Layout)
                isConsecutive =
                    a.getBoolean(R.styleable.ConsecutiveScrollerLayout_Layout_layout_isConsecutive,
                        true)
                isNestedScroll =
                    a.getBoolean(R.styleable.ConsecutiveScrollerLayout_Layout_layout_isNestedScroll,
                        true)
                isSticky =
                    a.getBoolean(R.styleable.ConsecutiveScrollerLayout_Layout_layout_isSticky,
                        false)
                isTriggerScroll =
                    a.getBoolean(R.styleable.ConsecutiveScrollerLayout_Layout_layout_isTriggerScroll,
                        false)
                isSink =
                    a.getBoolean(R.styleable.ConsecutiveScrollerLayout_Layout_layout_isSink, false)
                val type = a.getInt(R.styleable.ConsecutiveScrollerLayout_Layout_layout_align, 1)
                align = Align[type]
                scrollChild =
                    a.getResourceId(R.styleable.ConsecutiveScrollerLayout_Layout_layout_scrollChild,
                        NO_ID)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                a?.recycle()
            }
        }

        constructor(width: Int, height: Int) : super(width, height) {}
        constructor(source: ViewGroup.LayoutParams?) : super(source) {}
    }

    /**
     * 滑动到指定的view
     *
     * @param view
     */
    fun scrollToChild(view: View) {
        scrollToChildWithOffset(view, 0)
    }

    fun scrollToChildWithOffset(view: View, offset: Int) {
        val scrollToIndex = indexOfChild(view)
        if (scrollToIndex != -1) {
            var scrollAnchor = view.top - offset
            scrollAnchor -= getAdjustHeightForChild(view)

            // 滑动方向。
            var scrollOrientation = 0
            if (offset >= 0) {
                when {
                    scrollY + paddingTop > scrollAnchor -> {
                        scrollOrientation = -1
                    }
                    scrollY + paddingTop < scrollAnchor -> {
                        scrollOrientation = 1
                    }
                    ScrollUtils.canScrollVertically(view, -1) -> {
                        scrollOrientation = -1
                    }
                }
            } else {
                val viewScrollOffset = getViewsScrollOffset(scrollToIndex)
                if (scrollY + paddingTop + viewScrollOffset > scrollAnchor) {
                    scrollOrientation = -1
                } else if (scrollY + paddingTop + viewScrollOffset < scrollAnchor) {
                    scrollOrientation = 1
                }
            }
            if (scrollOrientation != 0) {
                mScrollToIndex = scrollToIndex
                // 停止fling
                stopScroll()
                mScrollToIndexWithOffset = offset
                scrollState = SCROLL_STATE_SETTLING
                do {
                    if (scrollOrientation < 0) {
                        dispatchScroll(-200)
                    } else {
                        dispatchScroll(200)
                    }
                    mCycleCount++
                } while (mScrollToIndex != -1)
            }
        }
    }

    /**
     * 平滑滑动到指定的view
     *
     * @param view
     */
    fun smoothScrollToChild(view: View) {
        smoothScrollToChildWithOffset(view, 0)
    }

    fun smoothScrollToChildWithOffset(view: View, offset: Int) {
        val scrollToIndex = indexOfChild(view)
        if (scrollToIndex != -1) {
            var scrollAnchor = view.top - offset
            scrollAnchor -= getAdjustHeightForChild(view)

            // 滑动方向。
            var scrollOrientation = 0
            if (offset >= 0) {
                when {
                    scrollY + paddingTop > scrollAnchor -> {
                        scrollOrientation = -1
                    }
                    scrollY + paddingTop < scrollAnchor -> {
                        scrollOrientation = 1
                    }
                    ScrollUtils.canScrollVertically(view, -1) -> {
                        scrollOrientation = -1
                    }
                }
            } else {
                val viewScrollOffset = getViewsScrollOffset(scrollToIndex)
                if (scrollY + paddingTop + viewScrollOffset > scrollAnchor) {
                    scrollOrientation = -1
                } else if (scrollY + paddingTop + viewScrollOffset < scrollAnchor) {
                    scrollOrientation = 1
                }
            }
            if (scrollOrientation != 0) {
                mScrollToIndex = scrollToIndex
                // 停止fling
                stopScroll()
                mScrollToIndexWithOffset = offset
                scrollState = SCROLL_STATE_SETTLING
                mSmoothScrollOffset = if (scrollOrientation < 0) {
                    -50
                } else {
                    50
                }
                invalidate()
            }
        }
    }

    /**
     * 获取从index到最后view，所有view的滑动offset总量
     *
     * @param index
     */
    private fun getViewsScrollOffset(index: Int): Int {
        var offset = 0
        val count = childCount
        for (i in index until count) {
            val child = getChildAt(i)
            if (child.visibility != GONE && ScrollUtils.isConsecutiveScrollerChild(child)) {
                offset += ScrollUtils.computeVerticalScrollOffset(child)
            }
        }
        return offset
    }

    var isAutoAdjustHeightAtBottomView: Boolean
        get() = mAutoAdjustHeightAtBottomView
        set(autoAdjustHeightAtBottomView) {
            if (mAutoAdjustHeightAtBottomView != autoAdjustHeightAtBottomView) {
                mAutoAdjustHeightAtBottomView = autoAdjustHeightAtBottomView
                requestLayout()
            }
        }
    var adjustHeightOffset: Int
        get() = mAdjustHeightOffset
        set(adjustHeightOffset) {
            if (mAdjustHeightOffset != adjustHeightOffset) {
                mAdjustHeightOffset = adjustHeightOffset
                requestLayout()
            }
        }

    /**
     * 设置吸顶常驻
     *
     * @param isPermanent
     */
    fun setPermanent(isPermanent: Boolean) {
        if (this.isPermanent != isPermanent) {
            this.isPermanent = isPermanent
            if (mAutoAdjustHeightAtBottomView) {
                requestLayout()
            } else {
                resetSticky()
            }
        }
    }

    fun isPermanent(): Boolean {
        return isPermanent
    }

    /**
     * 设置吸顶view到顶部的偏移量，允许吸顶view在距离顶部offset偏移量的地方吸顶停留。
     */
    var stickyOffset: Int
        get() = mStickyOffset
        set(offset) {
            if (mStickyOffset != offset) {
                mStickyOffset = offset
                resetSticky()
            }
        }

    /**
     * 常驻模式下，获取正在吸顶的view
     *
     * @return
     */
    val currentStickyViews: List<View>
        get() = mCurrentStickyViews

    /**
     * 判断子view是否是吸顶状态
     *
     * @param child
     * @return
     */
    fun theChildIsStick(child: View): Boolean {
        return (!isPermanent && currentStickyView === child
                || isPermanent && mCurrentStickyViews.contains(child))
    }

    /**
     * 滑动监听
     */
    interface OnScrollChangeListener {
        fun onScrollChange(v: View?, scrollY: Int, oldScrollY: Int, scrollState: Int)
    }

    /**
     * 监听吸顶变化
     */
    interface OnStickyChangeListener {
        /**
         * @param oldStickyView 旧的吸顶view，可能为空
         * @param newStickyView 新的吸顶view，可能为空
         */
        fun onStickyChange(oldStickyView: View?, newStickyView: View?)
    }

    /**
     * 监听常驻吸顶变化
     */
    interface OnPermanentStickyChangeListener {
        /**
         * @param mCurrentStickyViews 正在吸顶的view
         */
        fun onStickyChange(mCurrentStickyViews: List<View>)
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mChildHelper.isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mChildHelper.isNestedScrollingEnabled
    }

    override fun dispatchNestedFling(
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean,
    ): Boolean {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return mChildHelper.startNestedScroll(axes, type)
    }

    override fun stopNestedScroll(type: Int) {
        mChildHelper.stopNestedScroll(type)
    }

    override fun stopNestedScroll() {
        stopNestedScroll(ViewCompat.TYPE_TOUCH)
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return mChildHelper.hasNestedScrollingParent(type)
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
        dyUnconsumed: Int, offsetInWindow: IntArray?,
    ): Boolean {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
            offsetInWindow)
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int,
    ): Boolean {
        return mChildHelper.dispatchNestedScroll(dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            offsetInWindow,
            type)
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
    ): Boolean {
        return dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, ViewCompat.TYPE_TOUCH)
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int,
    ): Boolean {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        var isNestedScroll = false
        val lp = child.layoutParams
        if (lp is LayoutParams) {
            isNestedScroll = lp.isNestedScroll
        }
        return if (isNestedScroll) {
            axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
        } else {
            false
        }
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        mParentHelper.onNestedScrollAccepted(child, target, axes, type)
        checkTargetsScroll(isLayoutChange = false, isForce = false)
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL, type)
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        mParentHelper.onStopNestedScroll(target, type)
        stopNestedScroll(type)
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
    ) {
        onNestedScrollInternal(dyUnconsumed, type)
    }

    private fun onNestedScrollInternal(dyUnconsumed: Int, type: Int) {
        val oldScrollY = mSecondScrollY
        dispatchScroll(dyUnconsumed)
        val myConsumed = mSecondScrollY - oldScrollY
        val myUnconsumed = dyUnconsumed - myConsumed
        mChildHelper.dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null, type)
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return onStartNestedScroll(child, target, nestedScrollAxes, ViewCompat.TYPE_TOUCH)
    }

    override fun onNestedScrollAccepted(child: View, target: View, nestedScrollAxes: Int) {
        onNestedScrollAccepted(child, target, nestedScrollAxes, ViewCompat.TYPE_TOUCH)
    }

    override fun onStopNestedScroll(target: View) {
        onStopNestedScroll(target, ViewCompat.TYPE_TOUCH)
    }

    override fun onNestedScroll(
        target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int,
        dyUnconsumed: Int,
    ) {
        onNestedScrollInternal(dyUnconsumed, ViewCompat.TYPE_TOUCH)
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        onNestedPreScroll(target, dx, dy, consumed, ViewCompat.TYPE_TOUCH)
    }

    override fun getNestedScrollAxes(): Int {
        return mParentHelper.nestedScrollAxes
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        dispatchNestedPreScroll(dx, dy, consumed, null, type)
    }

    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean,
    ): Boolean {
        if (!consumed) {
            dispatchNestedFling(0f, velocityY, true)
            fling(velocityY.toInt())
            return true
        }
        return false
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        return dispatchNestedPreFling(velocityX, velocityY)
    }

    companion object {
        private const val SCROLL_NONE = 0
        private const val SCROLL_VERTICAL = 1
        private const val SCROLL_HORIZONTAL = 2
        private const val MAX_CYCLE_COUNT = 1000

        /**
         * The RecyclerView is not currently scrolling.
         *
         * @see .getScrollState
         */
        const val SCROLL_STATE_IDLE = 0

        /**
         * The RecyclerView is currently being dragged by outside input such as user touch input.
         *
         * @see .getScrollState
         */
        const val SCROLL_STATE_DRAGGING = 1

        /**
         * The RecyclerView is currently animating to a final position while not under
         * outside control.
         *
         * @see .getScrollState
         */
        const val SCROLL_STATE_SETTLING = 2

        // 这是RecyclerView的代码，让ConsecutiveScrollerLayout的fling效果更接近于RecyclerView。
        val sQuinticInterpolator =
            Interpolator { t ->
                var t = t
                t -= 1.0f
                t * t * t * t * t + 1.0f
            }
    }

    init {
        var a: TypedArray? = null
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.ConsecutiveScrollerLayout)
            isPermanent = a!!.getBoolean(R.styleable.ConsecutiveScrollerLayout_isPermanent, false)
            isDisableChildHorizontalScroll =
                a!!.getBoolean(R.styleable.ConsecutiveScrollerLayout_disableChildHorizontalScroll,
                    false)
            mStickyOffset =
                a!!.getDimensionPixelOffset(R.styleable.ConsecutiveScrollerLayout_stickyOffset, 0)
            mAutoAdjustHeightAtBottomView =
                a!!.getBoolean(R.styleable.ConsecutiveScrollerLayout_autoAdjustHeightAtBottomView,
                    false)
            mAdjustHeightOffset =
                a!!.getDimensionPixelOffset(R.styleable.ConsecutiveScrollerLayout_adjustHeightOffset,
                    0)
        } finally {
            if (a != null) {
                a!!.recycle()
            }
        }
        mScroller = OverScroller(getContext(), sQuinticInterpolator)
        val viewConfiguration = ViewConfiguration.get(context)
        mMaximumVelocity = viewConfiguration.scaledMaximumFlingVelocity
        mMinimumVelocity = viewConfiguration.scaledMinimumFlingVelocity
        mTouchSlop = ViewConfiguration.getTouchSlop()
        // 确保联动容器调用onDraw()方法
        setWillNotDraw(false)
        // enable vertical scrollbar
        isVerticalScrollBarEnabled = true
        mParentHelper = NestedScrollingParentHelper(this)
        mChildHelper = NestedScrollingChildHelper(this)
        isNestedScrollingEnabled = true
        isChildrenDrawingOrderEnabled = true
        isMotionEventSplittingEnabled = false
    }
}