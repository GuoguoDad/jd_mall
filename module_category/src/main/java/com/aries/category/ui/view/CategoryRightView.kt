package com.aries.category.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.ImageLoader
import coil.load
import com.aries.category.ui.ContentCateResponse
import com.example.category.R
import com.aries.category.ui.adapter.SectionQuickAdapter
import com.aries.category.ui.modal.CategoryModal
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.main_right.view.*

class CategoryRightView: FrameLayout {
    private lateinit var imageLoader: ImageLoader
    private lateinit var sectionQuickAdapter: SectionQuickAdapter
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var dataCopy: ContentCateResponse

    //记录上一次位置，防止在同一内容块里滑动 重复定位到tabLayout
    private var lastPos: Int = -1
    private var isRecyclerScroll: Boolean = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init(context)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(context: Context) {
        imageLoader = Coil.imageLoader(context)
        gridLayoutManager = GridLayoutManager(context, 3)
        LayoutInflater.from(context).inflate(R.layout.main_right, this, true)

        sectionQuickAdapter = SectionQuickAdapter(R.layout.main_right_grid_header, R.layout.main_right_grid, arrayListOf())
        var _tabLayout = tabLayout
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
                        var position = findCateIndex(gridLayoutManager.findFirstVisibleItemPosition())
                        if (position != lastPos) {
                            _tabLayout.setScrollPosition(position, 0F, true)
                        }
                        lastPos = position
                    }
                }
            })
        }
        tabLayout.run {
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    var position = tab?.position
                    if (position != null) {
                        scrollCycleViewByTabPosition(position)
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
        topImg.load(data.bannerUrl, imageLoader)  {
            crossfade(true)
        }
        tabLayout.removeAllTabs()
        data.cateList.forEach { v -> tabLayout.addTab(tabLayout.newTab().setText(v.categoryName)) }

        var list: ArrayList<CategoryModal> = arrayListOf()
        data.cateList.forEach { v ->
            run {
                list.add(CategoryModal("", v.categoryName, v.categoryCode, true))
                v.cateList?.forEach { m ->
                    run {
                        list.add(CategoryModal("", m.categoryName, m.categoryCode, false))
                    }
                }
            }
        }
        sectionQuickAdapter.setList(list)
    }

    private fun findCateIndex(position: Int): Int {
        var currentItem = sectionQuickAdapter.data[position]
        var index = dataCopy.cateList.indexOfFirst { group -> group.categoryCode == currentItem.categoryCode }

        if(index != -1){
            return index
        } else {
            var innerIndex: Int
            return dataCopy.cateList.indexOfFirst { group ->
                innerIndex =
                    group.cateList?.indexOfFirst { interGroup -> interGroup.categoryCode == currentItem.categoryCode }!!

                return@indexOfFirst innerIndex != -1
            }
        }
    }

    private fun scrollCycleViewByTabPosition(position: Int) {
        if (sectionQuickAdapter.data.size > 0) {
            var parent = dataCopy.cateList[position]
            var index = sectionQuickAdapter.data.indexOfFirst { v -> v.categoryCode == parent.categoryCode }

            gridLayoutManager.scrollToPositionWithOffset(index, 0)
        }
    }
}
