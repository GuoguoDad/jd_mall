package com.aries.home.ui

import android.annotation.SuppressLint
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.aries.common.base.BaseFragment
import com.aries.common.dialog.LoadingDialog
import com.aries.common.util.DisplayUtil
import com.aries.common.util.PixelUtil
import com.aries.common.util.StatusBarUtil
import com.aries.common.widget.consecutiveScroller.ConsecutiveScrollerLayout
import com.aries.home.R
import com.aries.home.ui.constants.ActionType
import com.aries.home.ui.fragment.goods.GoodsListFragment
import com.aries.home.ui.view.BannerView
import kotlinx.android.synthetic.main.fragment_home.*
import com.google.android.material.tabs.TabLayoutMediator
import com.aries.home.ui.view.AdView
import com.aries.home.ui.view.NineMenuView
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.home_top_view.*

class HomeFragment : BaseFragment(R.layout.fragment_home), MavericksView {
    private val viewModel: HomeViewModel by activityViewModel()
    //loading 对话框
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this.requireActivity()) }
    //分类tabLayout
    private var tabList: ArrayList<TabBean> = arrayListOf()
    private lateinit var tabViewPagerAdapter: FragmentStateAdapter

    //顶部滚动banner
    private val banner: BannerView by lazy { BannerView(this.requireContext()) }
    //顶部banner下面的广告
    private val adView: AdView by lazy { AdView(this.requireContext()) }
    //顶部九格宫功能菜单
    private val nineMenuView: NineMenuView by lazy { NineMenuView(this.requireContext(), this@HomeFragment) }

    override fun onDestroyView() {
        super.onDestroyView()
        loadingDialog.dismiss()
        refreshView.removeAllViews()
    }

    override fun initView() {
        initHeader()
        //下拉刷新
        refreshView.run {
            setEnableLoadMore(false)
            setOnRefreshListener {
                viewModel.init(true)
            }
        }
        collapsableContent.run {
            removeAllViews()
            addView(banner)
            addView(adView)
            addView(nineMenuView)
        }
        initTabViewPagerAdapter()
        //监听页面滚动
        consecutiveScrollerLayout.setOnVerticalScrollChangeListener(object: ConsecutiveScrollerLayout.OnScrollChangeListener{
            override fun onScrollChange(v: View?, scrollY: Int, oldScrollY: Int, scrollState: Int) {
                searchHeaderOnScroll(scrollY)
            }
        })

        backTop.setOnClickListener {
            consecutiveScrollerLayout.smoothScrollToChild(consecutiveScrollerLayout.getChildAt(0))
        }
        addStateChangeListener()
    }

    override fun initData() {
        viewModel.init(false)
    }

    override fun onResume() {
        super.onResume()
        ImmersionBar.with(this).transparentStatusBar().init()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden) {
            ImmersionBar.with(this).transparentStatusBar().init()
        }
    }

    private fun addStateChangeListener() {
        viewModel.onEach(
            HomeState::isLoading,
            HomeState::fetchType,
            HomeState::bannerList,
            HomeState::tabList,
            HomeState::adUrl,
            HomeState::nineMenuList,
            deliveryMode = uniqueOnly()
        ) { isLoading, fetchType, bannerList, tabList, adUrl, nineMenuList ->
            run {
                when (isLoading) {
                    true -> loadingDialog.show()
                    false -> {
                        loadingDialog.hide()
                        if (fetchType === ActionType.REFRESH) {
                            refreshView.run { finishRefresh() }
                            initTabViewPagerAdapter()
                        }
                        if (bannerList.isNotEmpty()) {
                            banner.setData(bannerList)
                        }
                        if (tabList.isNotEmpty()) {
                            showTabLayout(tabList)
                        }
                        if (adUrl.isNotBlank()) {
                            adView.setData(adUrl)
                        }
                        if (nineMenuList.isNotEmpty()) {
                            nineMenuView.setData(nineMenuList)
                        }
                    }
                }
            }
        }
    }

    override fun invalidate() {}

    @SuppressLint("NotifyDataSetChanged")
    private fun showTabLayout(list: List<TabBean>) {
        tabList.clear()
        tabList.addAll(list)
        tabViewPagerAdapter.notifyDataSetChanged()

        viewPager.offscreenPageLimit = list.size
        viewPager.getViewPager2?.let {
            TabLayoutMediator(tabLayout, it) { tab, position -> tab.text = list[position].name }.attach()
        }
    }

    private fun initHeader() {
        searchLinearLayout.setPadding(0, StatusBarUtil.getHeight(), 0, 0)
    }

    private fun initTabViewPagerAdapter() {
        tabViewPagerAdapter = object : FragmentStateAdapter(this){
            override fun getItemCount(): Int = tabList.size
            override fun createFragment(position: Int): Fragment {
                return GoodsListFragment(tabList[position].code)
            }
        }
        viewPager.adapter = tabViewPagerAdapter
    }


    private val searchHeaderMaxHeight = PixelUtil.toPixelFromDIP(80f)
    private val searchHeaderMinHeight = PixelUtil.toPixelFromDIP(40f)
    private val searchMaxMarginTop = PixelUtil.toPixelFromDIP(40f)
    private val searchMinMarginTop = PixelUtil.toPixelFromDIP(5f)
    private val searchMaxMarginLeft = PixelUtil.toPixelFromDIP(55f)
    private val searchMaxMarginRight = PixelUtil.toPixelFromDIP(90f)
    private val searchMinMargin = PixelUtil.toPixelFromDIP(15f)
    private val searchHeight = PixelUtil.toPixelFromDIP(30f)

    private fun searchHeaderOnScroll(scrollY: Int) {
        val containerNewHeight = searchHeaderMaxHeight - scrollY * 0.5
        val searchNewMarginTop = searchMaxMarginTop - scrollY * 0.5
        val searchNewMarginLeft = searchMinMargin + scrollY * 1.3
        val searchNewMarginRight = searchMinMargin + scrollY * 1.6

        if (scrollY >= DisplayUtil.getScreenHeight(requireContext())) {
            backTop.visibility = View.VISIBLE
        } else {
            backTop.visibility = View.GONE
        }

        val containerLp = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        val searchLp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        searchLp.height = searchHeight.toInt()
        if (searchNewMarginTop <= searchMinMarginTop) {
            searchLp.topMargin = searchMinMarginTop.toInt()
            containerLp.height = searchHeaderMinHeight.toInt()
        } else {
            searchLp.topMargin = searchNewMarginTop.toInt()
            containerLp.height = containerNewHeight.toInt()
        }
        searchHeader.layoutParams = containerLp
        if (searchNewMarginLeft >= searchMaxMarginLeft)
            searchLp.leftMargin = searchMaxMarginLeft.toInt()
        else
            searchLp.leftMargin = searchNewMarginLeft.toInt()

        if (searchNewMarginRight >= searchMaxMarginRight)
            searchLp.rightMargin = searchMaxMarginRight.toInt()
        else
            searchLp.rightMargin = searchNewMarginRight.toInt()

        search.layoutParams = searchLp
    }
}