package com.example.category

import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.category.adapter.CategoryListAdapter
import com.example.category.adapter.SectionQuickAdapter
import com.example.common.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : BaseFragment(R.layout.fragment_category), MavericksView {
    private val viewModel: CategoryViewModel by activityViewModel()
    private val categoryListAdapter by lazy { CategoryListAdapter(R.layout.category_item, arrayListOf())  }
    private val categoryLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(this.context) }

    override fun initView() {
        categoryList.run {
            layoutManager = categoryLayoutManager
            adapter = categoryListAdapter
            categoryListAdapter.setOnItemClickListener{ _, _, position ->
                setSelectCategory(position)
                scrollToMiddle(position)
            }
        }
        rightContainer.run {
            setEnableRefresh(false)
            setEnableAutoLoadMore(false)
        }
    }

    override fun initData() {
        viewModel.initBrandList()
        viewModel.queryContentByCate()
    }

    override fun invalidate() {
        withState(viewModel) {
            if (it.brandList.isNotEmpty()) {
                categoryListAdapter.setList(it.brandList)
            }
            if (it.content.cateList.isNotEmpty()) {
                categoryRightView.setData(it.content)
            }
        }
    }

    private fun setSelectCategory(selectIndex: Int) {
        categoryListAdapter.run {
            var preSelectIndex = data.indexOfFirst { v -> v.isSelect == true }
            if (preSelectIndex - 1 >= 0) {
                data[preSelectIndex - 1].fillet = ""
                notifyItemChanged(preSelectIndex - 1)
            }
            if (preSelectIndex + 1 < categoryListAdapter.data.size) {
                data[preSelectIndex + 1].fillet = ""
                notifyItemChanged(preSelectIndex + 1)
            }
            data[preSelectIndex].isSelect = false
            data[selectIndex].isSelect = true
            if (selectIndex - 1 >= 0) {
                data[selectIndex - 1].fillet = "down"
                notifyItemChanged(selectIndex - 1)
            }
            if (selectIndex + 1 < categoryListAdapter.data.size) {
                data[selectIndex + 1].fillet = "up"
                notifyItemChanged(selectIndex + 1)
            }
            notifyItemChanged(preSelectIndex)
            notifyItemChanged(selectIndex)
        }
    }

    private fun scrollToMiddle(position: Int) {
        val displayHeight = categoryList.height
        categoryLayoutManager.scrollToPositionWithOffset(position, displayHeight / 2 - 48)
    }
}