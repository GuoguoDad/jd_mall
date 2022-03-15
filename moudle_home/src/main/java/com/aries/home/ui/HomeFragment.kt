package com.aries.home.ui

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.aries.common.base.BaseFragment
import com.aries.common.dialog.LoadingDialog
import com.aries.common.util.StatusBarUtil
import com.aries.home.R
import com.aries.home.ui.constants.ActionType
import com.aries.home.ui.fragment.GoodsListFragment
import com.aries.home.ui.view.BannerView
import kotlinx.android.synthetic.main.fragment_home.*
import com.aries.home.ui.view.TopView
import com.google.android.material.tabs.TabLayoutMediator
import com.aries.common.util.PixelUtil
import com.aries.home.ui.view.AdView
import com.aries.home.ui.view.NineMenuView
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.home_goods.*

class HomeFragment : BaseFragment(R.layout.fragment_home), MavericksView {
    private val viewModel: HomeViewModel by activityViewModel()
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this.requireActivity()) }

    private val banner: BannerView by lazy { BannerView(this.requireContext()) }
    private val topView: TopView by lazy { TopView(this.requireContext()) }
    private val adView: AdView by lazy { AdView(this.requireContext()) }
    private val nineMenuView: NineMenuView by lazy { NineMenuView(this.requireContext()) }

    private val searchHeight = StatusBarUtil.getHeight() + PixelUtil.toPixelFromDIP(30f).toInt()

    override fun onDestroyView() {
        super.onDestroyView()
        refreshView.removeAllViews()
    }

    override fun initView() {
        initFloatHeader()
        refreshView.run {
            setOnRefreshListener {
                viewModel.init(true)
            }
        }
        collapsableContent.run {
            removeAllViews()
            addView(topView)
            addView(banner)
            addView(adView)
            addView(nineMenuView)
        }
        appBar.run {
            addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                if (verticalOffset <= -searchHeight) {
                    floatSearch.visibility = View.VISIBLE
                    backTop.visibility = View.VISIBLE
                } else {
                    floatSearch.visibility = View.GONE
                    backTop.visibility = View.GONE
                }
            })
        }
        backTop.setOnClickListener {
            scrollTop()
        }
        addStateChangeListener()
    }

    override fun initData() {
        viewModel.init(false)
    }

    private fun addStateChangeListener() {
        viewModel.onEach(
            HomeState::isLoading,
            HomeState::fetchType,
            HomeState::bannerList,
            HomeState::tabList,
            HomeState::adUrl,
            HomeState::nineMenuList
        ) { isLoading, fetchType, bannerList, tabList, adUrl, nineMenuList ->
            run {
                when (isLoading) {
                    true -> loadingDialog.show()
                    false -> {
                        loadingDialog.hide()
                        if (fetchType === ActionType.REFRESH) {
                            refreshView.run { finishRefresh() }
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
                            nineMenuView.setData(nineMenuList, this@HomeFragment)
                        }
                    }
                }
            }
        }
    }

    override fun invalidate() {}

    private fun showTabLayout(list: List<TabBean>) {
        viewPager.adapter = object : FragmentStateAdapter(this){
            override fun getItemCount(): Int = list.size
            override fun createFragment(position: Int): Fragment {
                return GoodsListFragment(list[position].code)
            }
        }
        viewPager.offscreenPageLimit = list.size
        TabLayoutMediator(tabLayout, viewPager) {
                tab, position -> tab.text = list[position].name
        }.attach()
    }

    private fun initFloatHeader() {
        floatSearch.setPadding(0, StatusBarUtil.getHeight(), 0, 10)
        val layoutParams = toolbar.layoutParams
        layoutParams.height = searchHeight
        toolbar.layoutParams = layoutParams
    }

    private fun scrollTop() {
        recyclerView.scrollToPosition(0)
        appBar.setExpanded(true, true)
    }
}