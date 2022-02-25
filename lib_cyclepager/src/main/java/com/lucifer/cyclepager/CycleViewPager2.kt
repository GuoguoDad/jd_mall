package com.lucifer.cyclepager

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi

import androidx.viewpager2.widget.ViewPager2

import com.lucifer.cyclepager.indicator.Indicator

import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback

import com.lucifer.cyclepager.adapter.CyclePagerFragmentAdapter

import com.lucifer.cyclepager.adapter.CyclePagerAdapter
import com.lucifer.cyclepager.util.Logger
import java.lang.IllegalArgumentException
import java.lang.ref.WeakReference
import java.util.*

class CycleViewPager2 : FrameLayout {
    private var mViewPager2: ViewPager2? = null
    private var canAutoTurning = false
    private var autoTurningTime: Long = 0
    private var isTurning = false
    private var mAutoTurningRunnable: AutoTurningRunnable? = null
    private var mPendingCurrentItem: Int = NO_POSITION
    private val mAdapterDataObserver: RecyclerView.AdapterDataObserver =
        object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                val itemCount: Int = Objects.requireNonNull(adapter)!!.getItemCount()
                if (itemCount <= 1) {
                    if (isTurning) {
                        stopAutoTurning()
                    }
                } else {
                    if (!isTurning) {
                        startAutoTurning()
                    }
                }
                if (mIndicator != null) {
                    mIndicator!!.onChanged(pagerRealCount, realCurrentItem)
                }
            }
        }
    private var mIndicator: Indicator? = null

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
        mViewPager2 = ViewPager2(context)
        mViewPager2!!.layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        mViewPager2!!.offscreenPageLimit = 1
        val mCycleOnPageChangeCallback: CycleOnPageChangeCallback = CycleOnPageChangeCallback()
        mViewPager2!!.registerOnPageChangeCallback(mCycleOnPageChangeCallback)
        mAutoTurningRunnable = AutoTurningRunnable(this)
        addView(mViewPager2)
    }

    fun setAutoTurning(autoTurningTime: Long) {
        setAutoTurning(true, autoTurningTime)
    }

    fun setAutoTurning(canAutoTurning: Boolean, autoTurningTime: Long) {
        this.canAutoTurning = canAutoTurning
        this.autoTurningTime = autoTurningTime
        stopAutoTurning()
        startAutoTurning()
    }

    fun startAutoTurning() {
        if (!canAutoTurning || autoTurningTime <= 0 || isTurning) return
        isTurning = true
        postDelayed(mAutoTurningRunnable, autoTurningTime)
    }

    fun stopAutoTurning() {
        isTurning = false
        removeCallbacks(mAutoTurningRunnable)
    }

    @get:Nullable
    var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?
        get() = mViewPager2?.adapter
        set(adapter) {
            if (adapter is CyclePagerAdapter<*> || adapter is CyclePagerFragmentAdapter) {
                if (mViewPager2!!.adapter === adapter) return
                adapter.registerAdapterDataObserver(mAdapterDataObserver)
                mViewPager2!!.adapter = adapter
                setCurrentItem(1, false)
                initIndicator()
                return
            }
            throw IllegalArgumentException("adapter must be an instance of CyclePagerAdapter or CyclePagerFragmentAdapter")
        }
    private val pagerRealCount: Int
        private get() {
            val adapter = adapter
            if (adapter is CyclePagerAdapter<*>) {
                return (adapter as CyclePagerAdapter<*>?)!!.realItemCount
            }
            return if (adapter is CyclePagerFragmentAdapter) {
                (adapter as CyclePagerFragmentAdapter?)!!.realItemCount
            } else 0
        }

    @get:ViewPager2.Orientation
    var orientation: Int
        get() = mViewPager2!!.orientation
        set(orientation) {
            mViewPager2!!.orientation = orientation
        }

    fun setPageTransformer(@Nullable transformer: ViewPager2.PageTransformer?) {
        mViewPager2!!.setPageTransformer(transformer)
    }

    fun addItemDecoration(decor: RecyclerView.ItemDecoration) {
        mViewPager2!!.addItemDecoration(decor)
    }

    fun addItemDecoration(decor: RecyclerView.ItemDecoration, index: Int) {
        mViewPager2!!.addItemDecoration(decor, index)
    }

    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        Logger.d("setCurrentItem $item")
        mViewPager2!!.setCurrentItem(item, smoothScroll)
        if (!smoothScroll && mIndicator != null) {
            mIndicator!!.onPageSelected(realCurrentItem)
        }
    }

    var currentItem: Int
        get() = mViewPager2!!.currentItem
        set(item) {
            setCurrentItem(item, true)
        }
    val realCurrentItem: Int
        get() = if (currentItem >= 1) currentItem - 1 else currentItem
    var offscreenPageLimit: Int
        get() = mViewPager2!!.offscreenPageLimit
        set(limit) {
            mViewPager2!!.offscreenPageLimit = limit
        }

    fun registerOnPageChangeCallback(callback: OnPageChangeCallback) {
        mViewPager2!!.registerOnPageChangeCallback(callback)
    }

    fun unregisterOnPageChangeCallback(callback: OnPageChangeCallback) {
        mViewPager2!!.unregisterOnPageChangeCallback(callback)
    }

    val viewPager2: ViewPager2
        get() = mViewPager2!!

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val action: Int = ev.getActionMasked()
        if (action == MotionEvent.ACTION_DOWN) {
            if (canAutoTurning && isTurning) {
                stopAutoTurning()
            }
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            if (canAutoTurning) startAutoTurning()
        }
        return super.dispatchTouchEvent(ev)
    }

    protected override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAutoTurning()
    }

    protected override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAutoTurning()
    }

    @Nullable
    protected override fun onSaveInstanceState(): Parcelable {
        val superState: Parcelable = super.onSaveInstanceState()!!
        val ss: SavedState = SavedState(superState)
        ss.mCurrentItem = currentItem
        Logger.d("onSaveInstanceState: " + ss.mCurrentItem)
        return ss
    }

    protected override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.getSuperState())
        mPendingCurrentItem = ss.mCurrentItem
        Logger.d("onRestoreInstanceState: $mPendingCurrentItem")
        restorePendingState()
    }

    private fun restorePendingState() {
        if (mPendingCurrentItem == NO_POSITION) {
            // No state to restore, or state is already restored
            return
        }
        val currentItem = Math.max(0, Math.min(mPendingCurrentItem, Objects.requireNonNull(
            adapter)!!.getItemCount() - 1))
        Logger.d("restorePendingState: $currentItem")
        mPendingCurrentItem = NO_POSITION
        setCurrentItem(currentItem, false)
    }

    fun setIndicator(@Nullable indicator: Indicator) {
        if (mIndicator === indicator) return
        removeIndicatorView()
        mIndicator = indicator
        initIndicator()
    }

    private fun initIndicator() {
        if (mIndicator == null || adapter == null) return
        addView(mIndicator!!.indicatorView)
        mIndicator!!.onChanged(pagerRealCount, realCurrentItem)
    }

    private fun removeIndicatorView() {
        if (mIndicator == null) return
        removeView(mIndicator!!.indicatorView)
    }

    //1.normal:
    //onPageScrollStateChanged(state=1) -> onPageScrolled... -> onPageScrollStateChanged(state=2)
    // -> onPageSelected -> onPageScrolled... -> onPageScrollStateChanged(state=0)
    //2.setCurrentItem(,true):
    //onPageScrollStateChanged(state=2) -> onPageSelected -> onPageScrolled... -> onPageScrollStateChanged(state=0)
    //3.other: no call onPageSelected()
    //onPageScrollStateChanged(state=1) -> onPageScrolled... -> onPageScrollStateChanged(state=2)
    // -> onPageScrolled... -> onPageScrollStateChanged(state=0)
    private inner class CycleOnPageChangeCallback : OnPageChangeCallback() {
        val INVALID_ITEM_POSITION = -1

        private var isBeginPagerChange = false
        private var mTempPosition = INVALID_ITEM_POSITION
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int,
        ) {
            Logger.d("onPageScrolled: " + position + " positionOffset: " + positionOffset
                    + " positionOffsetPixels: " + positionOffsetPixels)
            if (mIndicator != null) {
                mIndicator!!.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        }

        override fun onPageSelected(position: Int) {
            Logger.d("onPageSelected: $position")
            if (isBeginPagerChange) {
                mTempPosition = position
            }
            if (mIndicator != null) {
                mIndicator!!.onPageSelected(realCurrentItem)
            }
        }

        override fun onPageScrollStateChanged(state: Int) {
            Logger.d("onPageScrollStateChanged: state $state")
            if (state == ViewPager2.SCROLL_STATE_DRAGGING ||
                isTurning && state == ViewPager2.SCROLL_STATE_SETTLING
            ) {
                isBeginPagerChange = true
            } else if (state == ViewPager2.SCROLL_STATE_IDLE) {
                isBeginPagerChange = false
                val fixCurrentItem = getFixCurrentItem(mTempPosition)
                if (fixCurrentItem != INVALID_ITEM_POSITION && fixCurrentItem != mTempPosition) {
                    mTempPosition = INVALID_ITEM_POSITION
                    Logger.d("onPageScrollStateChanged: fixCurrentItem $fixCurrentItem")
                    setCurrentItem(fixCurrentItem, false)
                }
            }
            if (mIndicator != null) {
                mIndicator!!.onPageScrollStateChanged(state)
            }
        }

        private fun getFixCurrentItem(position: Int): Int {
            if (position == INVALID_ITEM_POSITION) return INVALID_ITEM_POSITION
            val lastPosition: Int = Objects.requireNonNull(adapter)!!.getItemCount() - 1
            var fixPosition = INVALID_ITEM_POSITION
            if (position == 0) {
                fixPosition = if (lastPosition == 0) 0 else lastPosition - 1
            } else if (position == lastPosition) {
                fixPosition = 1
            }
            return fixPosition
        }


    }

    internal class AutoTurningRunnable(cycleViewPager2: CycleViewPager2?):  Runnable {
        private val reference: WeakReference<CycleViewPager2>

        init {
            reference = WeakReference(cycleViewPager2)
        }
        override fun run() {
            val cycleViewPager2: CycleViewPager2 = reference.get()!!
            if (cycleViewPager2 != null && cycleViewPager2.canAutoTurning && cycleViewPager2.isTurning) {
                val itemCount: Int = Objects.requireNonNull(cycleViewPager2.adapter)!!.getItemCount()
                if (itemCount == 0) return
                val nextItem = (cycleViewPager2.currentItem + 1) % itemCount
                cycleViewPager2.setCurrentItem(nextItem, true)
                cycleViewPager2.postDelayed(cycleViewPager2.mAutoTurningRunnable,
                    cycleViewPager2.autoTurningTime)
            }
        }
    }

    internal class SavedState : BaseSavedState {
        var mCurrentItem = 0

        constructor(source: Parcel) : super(source) {
            readValues(source, null)
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        constructor(source: Parcel, loader: ClassLoader?) : super(source, loader) {
            readValues(source, loader)
        }

        constructor(superState: Parcelable?) : super(superState) {}

        private fun readValues(source: Parcel, loader: ClassLoader?) {
            mCurrentItem = source.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(mCurrentItem)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.ClassLoaderCreator<SavedState?> = object :
                Parcelable.ClassLoaderCreator<SavedState?> {
                override fun createFromParcel(source: Parcel, loader: ClassLoader?): SavedState {
                    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) SavedState(source,
                        loader) else SavedState(source)
                }

                override fun createFromParcel(source: Parcel): SavedState {
                    return createFromParcel(source, null)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}
