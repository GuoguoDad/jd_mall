package com.aries.category.ui

import android.annotation.SuppressLint
import android.graphics.Rect
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.aries.category.R
import com.aries.category.ui.adapter.CategoryListAdapter
import com.aries.common.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_main.*

class CategoryFragment : BaseFragment(R.layout.fragment_main), MavericksView {
    private val leftViewModel: LeftCategoryViewModel by activityViewModel()
    private val rightViewModel: RightCategoryViewModel by activityViewModel()

    private val categoryListAdapter by lazy { CategoryListAdapter(R.layout.main_left_item, arrayListOf())  }
    private val categoryLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(this.activity) }

    private var visualHeight = -1 //recyclerView 可视区域高度 - 当前点击item的高度

    override fun initView() {
        categoryList.run {
            setHasFixedSize(true)
            layoutManager = categoryLayoutManager
            adapter = categoryListAdapter
            categoryListAdapter.setOnItemClickListener{ _, item, position ->
                var rect = Rect()
                categoryList.getGlobalVisibleRect(rect)
                visualHeight = rect.bottom - rect.top - item.height

                setSelectCategory(position)
                scrollToMiddle(position)
                rightViewModel.queryContentByCate(categoryListAdapter.data[position].code)
            }
        }
        rightContainer.run {
            setEnableRefresh(false)
            setEnableAutoLoadMore(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rightContainer.removeAllViews()
    }

    override fun initData() {
        leftViewModel.initBrandList()
        leftViewModel.onEach(LeftCategoryState::brandList) {
                brandList -> run {
                if (brandList.isNotEmpty()) {
                    rightViewModel.queryContentByCate(brandList[0].code)
                }
            }
        }
    }

    override fun invalidate() {
        withState(leftViewModel, rightViewModel) {
            left, right ->
            if (left.brandList.isNotEmpty()) {
                categoryListAdapter.setList(left.brandList)
            }
            if (right.content.cateList.isNotEmpty()) {
                categoryRightView.setData(right.content)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setSelectCategory(selectIndex: Int) {
        categoryListAdapter.run {
            var preSelectIndex = data.indexOfFirst { v -> v.isSelect == true }

            var preStart = preSelectIndex
            var preCount = 1
            data[preSelectIndex].isSelect = false
            if (preSelectIndex - 1 >= 0) {
                data[preSelectIndex - 1].fillet = ""
                preStart = preSelectIndex - 1
                preCount += 1
            }
            if (preSelectIndex + 1 < categoryListAdapter.data.size) {
                data[preSelectIndex + 1].fillet = ""
                preCount += 1
            }
            notifyItemRangeChanged(preStart, preCount)

            var start = selectIndex
            var count = 1
            data[selectIndex].isSelect = true
            if (selectIndex - 1 >= 0) {
                data[selectIndex - 1].fillet = "down"
                start = selectIndex - 1
                count += 1
            }
            if (selectIndex + 1 < categoryListAdapter.data.size) {
                data[selectIndex + 1].fillet = "up"
                count += 1
            }
            notifyItemRangeChanged(start, count)
        }
    }

    private fun scrollToMiddle(position: Int) {
        var firstPosition = categoryLayoutManager.findFirstVisibleItemPosition()
        //当前点击的item距离 recyclerview 顶部的距离
        var top = categoryList.getChildAt(position - firstPosition).top
        //recyclerView可视区域高度-当前点击item的高度 的一半高度
        var half = visualHeight/2

        categoryList.smoothScrollBy(0, top - half)
    }
}