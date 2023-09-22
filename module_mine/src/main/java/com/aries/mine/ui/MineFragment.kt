package com.aries.mine.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.alibaba.android.arouter.launcher.ARouter
import com.aries.common.adapter.GoodsListAdapter
import com.aries.common.base.BaseFragment
import com.aries.common.constants.RouterPaths
import com.aries.common.decoration.SpacesItemDecoration
import com.aries.common.util.DisplayUtil
import com.aries.common.util.PixelUtil
import com.aries.common.util.StatusBarUtil
import com.donkingliang.consecutivescroller.ConsecutiveScrollerLayout
import com.aries.mine.R
import com.aries.mine.databinding.LayoutMineBinding
import com.aries.mine.ui.view.FiveMenuView
import com.gyf.immersionbar.ImmersionBar

class MineFragment: BaseFragment<LayoutMineBinding>(), MavericksView {
    private val viewModel: MineViewModal by activityViewModel()

    private val goodsListAdapter by lazy { GoodsListAdapter(arrayListOf()) }
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private val fiveMenuView: FiveMenuView by lazy { FiveMenuView(this.requireContext(), this@MineFragment) }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): LayoutMineBinding {
        return LayoutMineBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        initStatusHeight()

        binding.mineRefreshLayout.run {
            setEnableRefresh(false)
            setEnableLoadMore(false)
        }
        goodsListAdapter.loadMoreModule.setOnLoadMoreListener{
            viewModel.loadMoreRecommendList()
        }
        binding.functionList.run {
            removeAllViews()
            addView(fiveMenuView)
        }

        binding.consecutiveLayout.onVerticalScrollChangeListener =
            ConsecutiveScrollerLayout.OnScrollChangeListener { _, scrollY, _, _ -> handleScroll((scrollY * 0.65).toInt()) }

        binding.recommendGoodsList.run {
//            staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE //解决加载下一页后重新排列的问题
            addItemDecoration(SpacesItemDecoration(10))
            layoutManager = staggeredGridLayoutManager
            adapter = goodsListAdapter
        }
        goodsListAdapter.setOnItemClickListener { _, _, _ ->
            ARouter.getInstance().build(RouterPaths.GOODS_DETAIL).navigation()
        }
        binding.backTop.setOnClickListener {
            binding.consecutiveLayout.scrollToChild(binding.consecutiveLayout.getChildAt(0))
        }
        binding.includeFloatingHeader.setting.setOnClickListener {
            ARouter.getInstance().build(RouterPaths.RN_PAGE)
                .withString("bundleName", "app")
                .withString("initRouteUrl","https://com.aries.com?pageCode=rn&bundleName=app&initRouteName=UserSetting")
                .withString("url","rn://app/index.android.jsbundle")
                .navigation()
        }
        binding.includeFloatingHeader.message.setOnClickListener{
            ARouter.getInstance().build(RouterPaths.DEMO_ACTIVITY).navigation()
        }
    }

    override fun initData() {
        viewModel.queryMineInfo()
        viewModel.initRecommendList()
    }

    override fun onResume() {
        super.onResume()
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).init()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden) {
            ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).init()
        }
    }

    override fun invalidate() {
        withState(viewModel) {
            if (it.fiveMenuList.isNotEmpty()) {
                fiveMenuView.setData(it.fiveMenuList)
            }
            if (it.tabList.isNotEmpty()) {
                binding.tabLayout.removeAllTabs()
                it.tabList.forEach { v ->
                    binding.tabLayout.addTab(binding.tabLayout.newTab().setText(v.name))
                }
            }
            if (it.goodsList.isNotEmpty()) {
                goodsListAdapter.setList(it.goodsList)
                goodsListAdapter.loadMoreModule.loadMoreComplete()
            }
            if (it.nextPageGoodsList.isNotEmpty()) {
                goodsListAdapter.addData(it.nextPageGoodsList)
                if (it.currentPage <= it.totalPage)
                    goodsListAdapter.loadMoreModule.loadMoreComplete()
                else
                    goodsListAdapter.loadMoreModule.loadMoreEnd()
            }
        }
    }

    private fun initStatusHeight(){
        binding.headerLayout.setPadding(0, StatusBarUtil.getHeight(), 0, 0)
    }

    private val userInfoLayoutMaxMarginTop = PixelUtil.toPixelFromDIP(40f)
    private val userInfoLayoutMinMarginTop = PixelUtil.toPixelFromDIP(10f)
    private val maxHeight = PixelUtil.toPixelFromDIP(58f).toInt()
    private val minHeight = PixelUtil.toPixelFromDIP(25f).toInt()

    private fun handleScroll(
        scrollY: Int,
    ) {
        //处理背景色、用户信息显示与隐藏
        if (scrollY >= StatusBarUtil.getHeight()) {
            binding.includeFloatingHeader.userInfo.visibility = View.GONE
            binding.headerLayout.setBackgroundColor(resources.getColor(R.color.white))
            binding.includeFloatingHeader.mineTxt.setTextColor(resources.getColor(R.color.cl_000000))
            binding.bannerBg.setBackgroundResource(R.drawable.banner_bg)
        } else {
            binding.includeFloatingHeader.userInfo.visibility = View.VISIBLE
            binding.headerLayout.setBackgroundColor(resources.getColor(R.color.transparent))
            binding.includeFloatingHeader.mineTxt.setTextColor(resources.getColor(R.color.transparent))
            binding.bannerBg.setBackgroundResource(R.drawable.mine_top_bg)
        }
        //用户信息Layout距离顶部距离
        val userInfoLayoutNewMarginTop = userInfoLayoutMaxMarginTop - scrollY
        val userInfoLp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        if (userInfoLayoutNewMarginTop <= userInfoLayoutMinMarginTop){
            userInfoLp.topMargin = userInfoLayoutMinMarginTop.toInt()
        } else {
            userInfoLp.topMargin = userInfoLayoutNewMarginTop.toInt()
        }
        binding.includeFloatingHeader.userInfoLinearLayout.layoutParams = userInfoLp

        //处理头像大小
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val height =  if (maxHeight - scrollY < minHeight) minHeight else maxHeight - scrollY

        lp.height = height
        lp.width = height
        lp.leftMargin = PixelUtil.toPixelFromDIP(20f).toInt()

        binding.includeFloatingHeader.profileImage.layoutParams = lp
        //改变tabLayout背景色
        if (binding.consecutiveLayout.currentStickyViews.indexOfFirst { v -> v.id == R.id.tabLayout } == -1) {
            binding.tabLayout.setBackgroundResource(R.color.color_F5F5F5)
        } else {
            binding.tabLayout.setBackgroundResource(R.color.white)
        }
        if (scrollY == 0) {
            binding.tabLayout.setBackgroundResource(R.color.color_F5F5F5)
        }

        //显示backToTop
        if (scrollY >= DisplayUtil.getScreenHeight(this.requireContext())) {
            binding.backTop.visibility = View.VISIBLE
        } else {
            binding.backTop.visibility = View.GONE
        }
    }
}