package com.example.home.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.example.common.base.BaseFragment
import com.example.common.dialog.LoadingDialog
import com.example.home.R
import com.example.home.ui.fragment.GoodsListFragment
import com.example.home.ui.view.BannerView
import com.example.home.ui.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import com.example.home.ui.view.TopView
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseFragment(R.layout.fragment_home), MavericksView {
    private val viewModel: HomeViewModel by activityViewModel()
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this.requireActivity()) }

    private val banner: BannerView by lazy { BannerView(this.requireContext()) }
    private val topView: TopView by lazy { TopView(this.requireContext()) }

    override fun onDestroyView() {
        super.onDestroyView()
        refreshView.removeAllViews()
    }

    override fun initView() {
        collapsableContent.run {
            removeAllViews()
            addView(topView)
            addView(banner)
        }
    }

    override fun initData() {
        viewModel.init(false)
    }

    override fun invalidate() {
        withState(viewModel) {
            when (it.isLoading) {
                true -> loadingDialog.show()
                false -> {
                    loadingDialog.hide()
                    if (it.bannerList.isNotEmpty()) {
                        banner.setData(it.bannerList)
                    }
                    if (it.tabList.isNotEmpty() && it.goodsList.isNotEmpty()) {
                        showTabLayout(it.tabList, it.goodsList)
                    }
                }
            }
        }
    }

    private fun showTabLayout(list: List<TabBean>, goodsList: List<GoodsBean>) {
        viewPager.adapter = object : FragmentStateAdapter(this){
            override fun getItemCount(): Int = list.size
            override fun createFragment(position: Int): Fragment {
                return GoodsListFragment(goodsList.toMutableList())
            }
        }
        viewPager.offscreenPageLimit = list.size
        TabLayoutMediator(tabLayout, viewPager) {
                tab, position -> tab.text = list[position].name
        }.attach()
    }
}