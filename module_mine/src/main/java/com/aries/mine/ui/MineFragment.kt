package com.aries.mine.ui

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.aries.common.adapter.GoodsListAdapter
import com.aries.common.base.BaseFragment
import com.aries.common.decoration.SpacesItemDecoration
import com.aries.common.util.DisplayUtil
import com.aries.common.util.PixelUtil
import com.aries.common.util.StatusBarUtil
import com.aries.common.widget.AnimationNestedScrollView
import com.aries.mine.R
import com.aries.mine.ui.view.FiveMenuView
import kotlinx.android.synthetic.main.floating_header.*
import kotlinx.android.synthetic.main.layout_mine.*
import kotlinx.android.synthetic.main.login_userinfo.*

class MineFragment: BaseFragment(R.layout.layout_mine), MavericksView {
    private val viewModel: MineViewModal by activityViewModel()

    private val goodsListAdapter by lazy { GoodsListAdapter(arrayListOf()) }
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private val fiveMenuView: FiveMenuView by lazy { FiveMenuView(this.requireContext(), this@MineFragment) }

    override fun initView() {
        fixPosition()

        mineRefreshLayout.run {
            setEnableRefresh(false)
            setEnableAutoLoadMore(true)
            setOnLoadMoreListener {
                viewModel.loadMoreRecommendList()
            }
        }

        functionList.run {
            removeAllViews()
            addView(fiveMenuView)
        }

        recommendGoodsList.run {
//            staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE //解决加载下一页后重新排列的问题
            addItemDecoration(SpacesItemDecoration(10))
            layoutManager = staggeredGridLayoutManager
            adapter = goodsListAdapter
        }

        val marginLayoutParams = ViewGroup.MarginLayoutParams(userInfoLinearLayout.layoutParams)
        mineNestedScrollView.setOnAnimationScrollListener(object : AnimationNestedScrollView.OnAnimationScrollChangeListener{
            override fun onScrollChanged(dy: Float) {
                onScrollChange(dy.toInt(), marginLayoutParams)
            }
        })

        backTop.setOnClickListener {
            mineNestedScrollView.smoothScrollTo(0,0)
        }
    }

    override fun initData() {
        viewModel.queryMineInfo()
        viewModel.initRecommendList()
    }

    override fun invalidate() {
        withState(viewModel) {
            if (it.fiveMenuList.isNotEmpty()) {
                fiveMenuView.setData(it.fiveMenuList)
            }
            if (it.goodsList.isNotEmpty()) {
                goodsListAdapter.setList(it.goodsList)
            }
            if (it.nextPageGoodsList.isNotEmpty()) {
                goodsListAdapter.addData(it.nextPageGoodsList)
                mineRefreshLayout.run {
                    if (it.currentPage <= it.totalPage) finishLoadMore()
                    else finishLoadMoreWithNoMoreData()
                }
            }
        }
    }

    private fun fixPosition(){
        headerLayout.setPadding(0, StatusBarUtil.getHeight(), 0, 0)

        val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        lp.height = (StatusBarUtil.getHeight() + PixelUtil.toPixelFromDIP(128f)).toInt()
        bannerBg.layoutParams = lp
    }

    private fun onScrollChange(
        scrollY: Int,
        marginLayoutParams: ViewGroup.MarginLayoutParams,
    ) {
        //处理头像layout距离顶部的距离
        val header2Top = (StatusBarUtil.getHeight() + PixelUtil.toPixelFromDIP(40f)).toInt()
        val top = header2Top - scrollY

        val layoutLP = FrameLayout.LayoutParams(marginLayoutParams)

        val fixedTop = StatusBarUtil.getHeight() + PixelUtil.toPixelFromDIP(10f).toInt()

        if (top <= fixedTop) {
            layoutLP.setMargins(0, fixedTop, 0, 0)
            userInfo.visibility = View.GONE
            headerLayout.setBackgroundColor(resources.getColor(R.color.white))
            mineTxt.setTextColor(resources.getColor(R.color.cl_000000))
            bannerBg.setBackgroundResource(R.drawable.banner_bg2)
        } else {
            userInfo.visibility = View.VISIBLE
            layoutLP.setMargins(0, top, 0, 0)
            headerLayout.setBackgroundColor(resources.getColor(R.color.transparent))
            mineTxt.setTextColor(resources.getColor(R.color.transparent))
            bannerBg.setBackgroundResource(R.drawable.banner_bg)
        }
        userInfoLinearLayout.layoutParams = layoutLP

        //处理头像大小
        val lp = LinearLayout.LayoutParams(marginLayoutParams)

        val maxHeight = PixelUtil.toPixelFromDIP(58f).toInt()
        val minHeight = PixelUtil.toPixelFromDIP(30f).toInt()

        val height =  if (maxHeight - scrollY < minHeight) minHeight else maxHeight - scrollY

        lp.height = height
        lp.width = height
        lp.leftMargin = PixelUtil.toPixelFromDIP(20f).toInt()

        profileImage.layoutParams = lp

        //设置NestedScrollView的marginTop
        val scrollLP = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        val maxMarginTop = PixelUtil.toPixelFromDIP(54f).toInt()

        val marginTop = if (scrollY < maxMarginTop) maxMarginTop - scrollY else 0
        scrollLP.setMargins(0, marginTop, 0, 0)

        mineNestedScrollView.layoutParams = scrollLP

        //显示backToTop
        if (scrollY >= DisplayUtil.getScreenHeight(this.requireContext())) {
            backTop.visibility = View.VISIBLE
        } else {
            backTop.visibility = View.GONE
        }
    }
}