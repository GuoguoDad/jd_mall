package com.example.home.ui

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.example.common.base.BaseFragment
import com.example.common.dialog.LoadingDialog
import com.example.common.util.DisplayUtil
import com.example.common.util.StatusBarUtil
import com.example.home.R
import com.example.home.ui.constants.ActionType
import com.example.home.ui.fragment.GoodsListFragment
import com.example.home.ui.state.HomeState
import com.example.home.ui.view.BannerView
import com.example.home.ui.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import com.example.home.ui.view.TopView
import com.google.android.material.tabs.TabLayoutMediator
import com.example.common.util.PixelUtil
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.home_goods.*

class HomeFragment : BaseFragment(R.layout.fragment_home), MavericksView {
    private val viewModel: HomeViewModel by activityViewModel()
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this.requireActivity()) }

    private val banner: BannerView by lazy { BannerView(this.requireContext()) }
    private val topView: TopView by lazy { TopView(this.requireContext()) }

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
            HomeState::tabList
        ) { isLoading, fetchType, bannerList, tabList ->
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