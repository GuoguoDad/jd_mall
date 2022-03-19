package com.aries.cart.ui

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.aries.common.base.BaseFragment
import com.aries.cart.R
import com.aries.cart.ui.adapter.StoreGoodsListAdapter
import com.aries.cart.ui.adapter.StoreListAdapter
import com.aries.cart.ui.view.QuickEntryPopup
import com.aries.common.adapter.GoodsListAdapter
import com.aries.common.decoration.SpacesItemDecoration
import com.aries.common.util.PixelUtil
import com.aries.common.util.StatusBarUtil
import com.aries.common.util.UnreadMsgUtil
import com.google.android.material.appbar.AppBarLayout
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.top_address.*

class CartFragment : BaseFragment(R.layout.fragment_cart), MavericksView {
    private val viewModel: CartViewModel by activityViewModel()
    //购物车中店铺商品列表adapter
    private val cartGoodsListAdapter: StoreListAdapter by lazy {
        StoreListAdapter(R.layout.fragment_cart_item, arrayListOf())
    }
    //你可能还喜欢 或者 快点来看看 商品列表adapter
    private val goodsListAdapter by lazy { GoodsListAdapter(arrayListOf()) }
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    private val topViewHeight = StatusBarUtil.getHeight() + PixelUtil.toPixelFromDIP(40f).toInt()

    override fun initView() {
        initStatusBarPlaceholder()

        UnreadMsgUtil.show(threePointsBadgeNum, 2)

        threePointsLayout.setOnClickListener {
            showQuickEntry()
        }

        cartAppBarLayout.run {
            addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                if (verticalOffset <= -topViewHeight) {
                    statusBarPlaceholder.visibility = View.VISIBLE
                } else {
                    statusBarPlaceholder.visibility = View.GONE
                }
            })
        }

        contentLayout.run {
            setEnableRefresh(false)
            setEnableLoadMore(false)
        }
        //购物车中的商品列表
        cartGoodsList.run {
            layoutManager = LinearLayoutManager(this.context)
            adapter = cartGoodsListAdapter
        }
        //你可能还喜欢 或者 快点来看看 商品列表
        goodsList.run {
            addItemDecoration(SpacesItemDecoration(10))
            layoutManager = staggeredGridLayoutManager
            adapter = goodsListAdapter
        }
    }

    override fun initData() {
        viewModel.queryCartGoodsList()
        viewModel.initMaybeLikeList()
    }

    private fun initStatusBarPlaceholder() {
        topAddressLayout.setPadding(0, StatusBarUtil.getHeight(), 0 , 0)
        val layoutParams = statusBarPlaceholder.layoutParams
        layoutParams.height = StatusBarUtil.getHeight()
        statusBarPlaceholder.layoutParams = layoutParams
        statusBarPlaceholder.visibility = View.GONE

//        bottomAllSelectLayout.visibility = View.GONE
    }

    //顶部快捷入口
    private fun showQuickEntry() {
        XPopup.Builder(this.requireContext())
            .popupAnimation(PopupAnimation.TranslateFromTop)
            .hasShadowBg(false)
            .isLightStatusBar(true)
            .asCustom(QuickEntryPopup(this.requireContext()))
            .show()
    }

    override fun invalidate() {
        withState(viewModel) {
            if (it.cartGoodsList.isNotEmpty()) {
                cartGoodsListAdapter.setList(it.cartGoodsList)
            }
            if (it.goodsList.isNotEmpty()) {
                goodsListAdapter.setList(it.goodsList)
            }
        }
    }
}