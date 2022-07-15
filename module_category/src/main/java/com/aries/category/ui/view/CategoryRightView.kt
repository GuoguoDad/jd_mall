package com.aries.category.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aries.category.ui.ContentCateResponse
import com.aries.category.R
import com.aries.category.ui.adapter.SectionQuickAdapter
import com.aries.category.ui.modal.CategoryModal
import com.aries.common.util.CoilUtil
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.main_right.view.*

class CategoryRightView: FrameLayout {
    private val imageLoader = CoilUtil.getImageLoader()
    private lateinit var sectionQuickAdapter: SectionQuickAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var dataCopy: ContentCateResponse

    //记录上一次位置，防止在同一内容块里滑动 重复定位到tabLayout
    private var lastPos: Int = -1
    private var isRecyclerScroll: Boolean = false

    private var mToPosition: Int = 0 //记录目标位置
    private var mShouldScroll: Boolean = false //目标项是否在最后一个可见项之后

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        initView(context)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.main_right, this, true)

        gridLayoutManager = GridLayoutManager(context, 3)
        sectionQuickAdapter = SectionQuickAdapter(R.layout.main_right_grid_header, R.layout.main_right_grid, arrayListOf())

        val thisTabLayout = tabLayout
        categoryRecyclerView.run {
            layoutManager = gridLayoutManager
            adapter = sectionQuickAdapter

            setOnTouchListener { _, p1 ->
                if (p1.action == MotionEvent.ACTION_DOWN) {
                    isRecyclerScroll = true
                }
                false
            }
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (isRecyclerScroll) {
                        val position = findHeaderPositionByTab(gridLayoutManager.findFirstVisibleItemPosition())
                        if (position != lastPos) {
                            thisTabLayout.setScrollPosition(position, 0F, true)
                        }
                        lastPos = position
                    }
                }
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (mShouldScroll) {
                        mShouldScroll = false
                        scrollTop(mToPosition)
                    }
                }
            })
        }
        tabLayout.run {
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val position = tab?.position
                    if (position != null) {
                        scrollRecycleView2Top(position)
                    }
                    isRecyclerScroll = false
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    fun setData(data: ContentCateResponse) {
        dataCopy = data
        topImg.visibility = View.VISIBLE
        topImg.load(data.bannerUrl, imageLoader)  {
            crossfade(true)
        }
        tabLayout.removeAllTabs()
        data.cateList.forEach { v -> tabLayout.addTab(tabLayout.newTab().setText(v.categoryName)) }

        val list: ArrayList<CategoryModal> = arrayListOf()
        data.cateList.forEach { v ->
            run {
                list.add(CategoryModal(v.iconUrl, v.categoryName, v.categoryCode, true))
                v.cateList?.forEach { m -> list.add(CategoryModal(m.iconUrl, m.categoryName, m.categoryCode, false))}
            }
        }
        sectionQuickAdapter.setList(list)
    }


    private fun scrollRecycleView2Top(position: Int) {
        val currentItem = dataCopy.cateList[position]
        val currentPosition = sectionQuickAdapter.data.indexOfFirst { v -> v.categoryCode == currentItem.categoryCode }

        scrollTop(currentPosition)
    }

    private fun scrollTop(position: Int) {
        val firstPosition = gridLayoutManager.findFirstVisibleItemPosition()
        val lastPosition = gridLayoutManager.findLastVisibleItemPosition()

        if (position < firstPosition) {
            // 如果跳转位置在第一个可见位置之前，就smoothScrollToPosition可以直接跳转
            categoryRecyclerView.smoothScrollToPosition(position)
        } else if (position <= lastPosition){
            // 跳转位置在第一个可见项之后，最后一个可见项之前
            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
            val movePosition = position - firstPosition
            if (movePosition >=0 && movePosition < categoryRecyclerView.childCount) {
                val scrollY = categoryRecyclerView.getChildAt(position - firstPosition).top
                categoryRecyclerView.smoothScrollBy(0, scrollY)
            }
        } else {
            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            categoryRecyclerView.smoothScrollToPosition(position)
            mToPosition = position
            mShouldScroll = true
        }
    }

    private fun findHeaderPositionByTab(position: Int): Int {
        val currentItem = sectionQuickAdapter.data[position]
        val index = dataCopy.cateList.indexOfFirst { group -> group.categoryCode == currentItem.categoryCode }

        if(index != -1){
            return index
        } else {
            var innerIndex: Int
            return dataCopy.cateList.indexOfFirst {
                    group -> innerIndex = group.cateList?.indexOfFirst { interGroup -> interGroup.categoryCode == currentItem.categoryCode }!!

                return@indexOfFirst innerIndex != -1
            }
        }
    }
}
