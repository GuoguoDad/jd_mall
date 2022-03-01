package com.example.main.ui.scrolltab

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.common.base.BaseActivity
import com.example.common.constants.RouterPaths
import com.example.common.util.DisplayUtil
import com.example.main.R
import com.example.main.ui.scrolltab.fragment.WaterfallFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.AlphaPageTransformer
import kotlinx.android.synthetic.main.layout_tab_waterfall.*

@Route(path = RouterPaths.TAB_WATERFALL_ACTIVITY)
class ScrollTabActivity: BaseActivity(R.layout.layout_tab_waterfall), MavericksView {
    private val viewModel: ScrollTabViewModel by viewModel()

    override fun initView() {
        tabWaterfall.setOnRefreshListener {
            viewModel.queryBanner(true)
            viewModel.queryScrollTab()
        }
        tabWaterfall.setEnableLoadMore(false)

        addStateChangeListener()
    }

    override fun initData() {
        viewModel.queryBanner(false)
        viewModel.queryScrollTab()
    }

    override fun invalidate() {}

    private fun addStateChangeListener() {
        viewModel.onEach(
            ScrollTabState::actionType,
            ScrollTabState::bannerList,
            ScrollTabState::tabs
        ) { actionType, bannerList, tabs ->
            run {
                when (actionType) {
                    "init" -> {
                        if (bannerList.isNotEmpty()) {
                            showBannerView(bannerList)
                        }
                    }
                    "refresh" -> {
                        if (bannerList.isNotEmpty()) {
                            showBannerView(bannerList)
                            tabWaterfall.run {
                                finishRefresh()
                            }
                        }
                    }
                    "scrollTab" -> {
                        if (tabs.isNotEmpty()) {
                            showTabLayout(tabs)
                        }
                    }
                }
            }
        }
    }

    private fun showBannerView(data: List<BannerBean>) {
        tabWaterfallBanner.run {
            adapter = BannerAdapter(data)
            setBannerRound(DisplayUtil.dip2px(3f).toFloat())
            setIndicator(CircleIndicator(this.context))
            setPageTransformer(AlphaPageTransformer())
            setIndicatorSelectedColorRes(com.example.home.R.color.indicator_selected_color)
            setLoopTime(6000L)
            isAutoLoop(true)
        }
    }

    private fun showTabLayout(list: List<String>) {
        tabWaterfallViewPager.adapter = object : FragmentStateAdapter(this){
            override fun getItemCount(): Int = list.size
            override fun createFragment(position: Int): Fragment {
                return WaterfallFragment()
            }
        }
        TabLayoutMediator(tabLayout, tabWaterfallViewPager) {
            tab, position -> tab.text = list[position]
        }.attach()
    }
}