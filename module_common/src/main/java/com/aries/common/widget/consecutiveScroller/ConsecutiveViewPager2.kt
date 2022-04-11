package com.aries.common.widget.consecutiveScroller

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OffscreenPageLimit
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.aries.common.BaseApplication
import java.lang.ref.WeakReference

/**
 * @Description Viewpager2的包装类，它不是ViewPager2的子类，而且一个包含ViewPager2的容器，提供了ViewPager2的所有功能，
 * 同时实现IConsecutiveScroller接口，使它能配合ConsecutiveScrollerLayout的滑动处理。
 * 因为Viewpager2不能被继承，所有使用包装类的方式来解决Viewpager2和ConsecutiveScrollerLayout的滑动冲突。
 */
open class ConsecutiveViewPager2 : FrameLayout, IConsecutiveScroller {
    companion object {
        private const val TAG_KEY = -123

    }
    var viewPager2: ViewPager2? = null
    private var mRecyclerView: RecyclerView? = null
    private var mAdjustHeight = 0

    constructor(context: Context) : super(context) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        initialize(context)
    }

    private fun initialize(context: Context) {
        viewPager2 = ViewPager2(context)
        addView(viewPager2, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        mRecyclerView = viewPager2!!.getChildAt(0) as RecyclerView
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isConsecutiveParentAndBottom && mAdjustHeight > 0) {
            val height = getDefaultSize(0, heightMeasureSpec) - mAdjustHeight
            super.onMeasure(widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.getMode(heightMeasureSpec)))
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    /**
     * 是否在ConsecutiveScrollerLayout的底部
     */
    private val isConsecutiveParentAndBottom: Boolean
        get() {
            val parent = parent
            if (parent is ConsecutiveScrollerLayout) {
                return parent.indexOfChild(this) == parent.childCount - 1
            }
            return false
        }

    @get:Deprecated("")
    @set:Deprecated("""如果你想调整ViewPager的高度，使它不被顶部吸顶view或者其他布局覆盖，
      请使用ConsecutiveScrollerLayout的autoAdjustHeightAtBottomView和adjustHeightOffset属性。""")
    var adjustHeight: Int
        get() = mAdjustHeight
        set(adjustHeight) {
            if (mAdjustHeight != adjustHeight) {
                mAdjustHeight = adjustHeight
                requestLayout()
            }
        }

    /**
     * 返回当前需要滑动的view。
     *
     * @return
     */
    override val currentScrollerView: View?
        get() {
            var scrollerView: View? = null
            val currentItem = currentItem
            val adapter = mRecyclerView!!.adapter
            val layoutManager = mRecyclerView!!.layoutManager
            if (adapter != null && layoutManager != null) {
                if (currentItem >= 0 && currentItem < adapter.itemCount) {
                    val itemView = layoutManager.findViewByPosition(currentItem)
                    scrollerView = findScrolledItemView(itemView)
                    scrollerView?.let { setAttachListener(it) }
                }
            }
            if (scrollerView == null) {
                scrollerView = mRecyclerView
            }
            return scrollerView
        }

    /**
     * 给scrollerView添加OnAttachStateChange监听，在item添加到屏幕是检查scrollerView滑动位置，放在布局显示断层。
     *
     * @param scrollerView
     */
    private fun setAttachListener(scrollerView: View) {
        if (scrollerView.getTag(TAG_KEY) != null) {
            val listener = scrollerView.getTag(TAG_KEY) as AttachListener
            if (listener.reference.get() == null) {
                // 情况原来的监听器
                scrollerView.removeOnAttachStateChangeListener(listener)
                scrollerView.setTag(TAG_KEY, null)
            }
        }
        if (scrollerView.getTag(TAG_KEY) == null) {
            val lp = layoutParams
            if (lp is ConsecutiveScrollerLayout.LayoutParams) {
                if (lp.isConsecutive) {
                    val l = AttachListener(this, scrollerView)
                    scrollerView.addOnAttachStateChangeListener(l)
                    scrollerView.setTag(TAG_KEY, l)
                }
            }
        }
    }

    private fun scrollChildContent(v: View?) {
        if (v != null && parent is ConsecutiveScrollerLayout) {
            val parent = parent as ConsecutiveScrollerLayout
            val thisIndex = parent.indexOfChild(this)

            // 判断是否需要滑动内容到底部或顶部
            if (thisIndex == parent.childCount - 1 && height < parent.height && parent.scrollY >= parent.mScrollRange) {
                return
            }
            val firstVisibleView = parent.findFirstVisibleView() ?: return
            val firstIndex = parent.indexOfChild(firstVisibleView)
            if (thisIndex < firstIndex) {
                parent.scrollChildContentToBottom(v)
            } else if (thisIndex > firstIndex) {
                parent.scrollChildContentToTop(v)
            }
        }
    }

    /**
     * 返回全部需要滑动的下级view
     *
     * @return
     */
    override val scrolledViews: List<View>
        get() {
            val views: ArrayList<View> = ArrayList()
            val count = mRecyclerView!!.childCount
            if (count > 0) {
                for (i in 0 until count) {
                    findScrolledItemView(mRecyclerView!!.getChildAt(i))?.let { views.add(it) }
                }
            }
            return views
        }

    private fun findScrolledItemView(view: View?): View? {
        if (mRecyclerView!!.adapter is FragmentStateAdapter
            && view is FrameLayout
        ) {
            if (view.childCount > 0) {
                return view.getChildAt(0)
            }
        }
        return view
    }

    var getViewPager2: ViewPager2? = null
        get() = viewPager2!!

    var adapter: RecyclerView.Adapter<*>?
        get() = viewPager2!!.adapter
        set(adapter) {
            viewPager2!!.adapter = adapter
        }

    @get:ViewPager2.Orientation
    var orientation: Int
        get() = viewPager2!!.orientation
        set(orientation) {
            viewPager2!!.orientation = orientation
        }

    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        viewPager2!!.setCurrentItem(item, smoothScroll)
    }

    var currentItem: Int
        get() = viewPager2!!.currentItem
        set(item) {
            viewPager2!!.currentItem = item
        }

    @get:OffscreenPageLimit
    var offscreenPageLimit: Int
        get() = viewPager2!!.offscreenPageLimit
        set(limit) {
            viewPager2!!.offscreenPageLimit = limit
        }

    fun registerOnPageChangeCallback(callback: OnPageChangeCallback) {
        viewPager2!!.registerOnPageChangeCallback(callback)
    }

    fun unregisterOnPageChangeCallback(callback: OnPageChangeCallback) {
        viewPager2!!.unregisterOnPageChangeCallback(callback)
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        return viewPager2!!.canScrollHorizontally(direction)
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return viewPager2!!.canScrollVertically(direction)
    }

    private class AttachListener(parent: ConsecutiveViewPager2?, view: View) :
        OnAttachStateChangeListener {
        var reference: WeakReference<ConsecutiveViewPager2?> = WeakReference(parent)
        var view: View = view
        override fun onViewAttachedToWindow(v: View) {
            if (reference.get() != null) {
                reference.get()!!.scrollChildContent(view)
            }
        }

        override fun onViewDetachedFromWindow(v: View) {}
    }
}
