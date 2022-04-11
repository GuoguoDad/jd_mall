package com.aries.cart.ui

import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.aries.common.base.BaseFragment
import com.aries.cart.R
import com.aries.cart.ui.ConvertUtil.convertCartData
import com.aries.cart.ui.adapter.CartGoodsAdapter
import com.aries.cart.ui.listener.OnCartItemChangeListener
import com.aries.cart.ui.listener.OnStepperChangeListener
import com.aries.cart.ui.view.QuickEntryPopup
import com.aries.common.adapter.GoodsListAdapter
import com.aries.common.decoration.SpacesItemDecoration
import com.aries.common.util.DisplayUtil
import com.aries.common.util.PixelUtil
import com.aries.common.util.StatusBarUtil
import com.aries.common.util.UnreadMsgUtil
import com.aries.common.widget.consecutiveScroller.ConsecutiveScrollerLayout
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import kotlinx.android.synthetic.main.bottom_all_select.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart_content.view.*
import kotlinx.android.synthetic.main.top_address.*
import kotlinx.android.synthetic.main.top_filter.*
import java.math.BigDecimal

class CartFragment : BaseFragment(R.layout.fragment_cart), MavericksView {
    private val viewModel: CartViewModel by activityViewModel()

    //购物车中店铺商品列表adapter
    private val cartGoodsAdapter: CartGoodsAdapter by lazy { CartGoodsAdapter(arrayListOf())  }
    private var cartGoodsListCopy: List<StoreGoodsBean> = arrayListOf()
    //你可能还喜欢 或者 快点来看看 商品列表adapter
    private val goodsListAdapter by lazy { GoodsListAdapter(arrayListOf()) }
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun initView() {
        initStatusBarPlaceholder()
        //快捷菜单点点点 角标
        UnreadMsgUtil.show(threePointsBadgeNum, 2)
        //快捷菜单点击事件
        threePointsLayout.setOnClickListener { showQuickEntry() }

        //SmartRefreshLayout
        cartRefreshLayout.run {
            setEnableLoadMore(false)
            setOnRefreshListener {
                viewModel.queryCartGoodsList(true)
                viewModel.initMaybeLikeList()
            }
        }
        goodsListAdapter.loadMoreModule.setOnLoadMoreListener {
            viewModel.loadMoreMaybeLikeList()
        }
        //购物车中的商品列表
        cartGoodsList.run {
            layoutManager = LinearLayoutManager(this.context)
            adapter = cartGoodsAdapter
        }
        cartGoodsAdapter.run {
            addChildClickViewIds(R.id.storeCheckBox, R.id.goodsCheckBox)
            //监听店铺选中、商品选中事件
            setOnItemChildClickListener { adapter, view, position ->
                val data: CartBean = adapter.data[position] as CartBean
                val storeIndex = cartGoodsListCopy.indexOfFirst { m -> m.storeCode == data.storeCode }
                val goodsIndex = if (data.code != null) cartGoodsListCopy[storeIndex].goodsList.indexOfFirst { n -> n.code == data.code } else 0

                when (view.id) {
                    R.id.storeCheckBox -> checkAllByStore(storeIndex)
                    R.id.goodsCheckBox -> checkGoods(storeIndex, goodsIndex)
                }
            }
            //监听购物车中商品数量变化
            setOnStepperChangeListener(object: OnStepperChangeListener {
                override fun onStepperChange(bean: CartBean, value: Int) {
                    setGoodsNum(bean, value)
                }
            })
            //监听商品侧滑菜单(看相似，删除)
            setOnCartItemChangeListener(object: OnCartItemChangeListener{
                override fun onItemStateChange(storeCode: String, goodsCode: String, type: String) {
                    val storeIndex = cartGoodsListCopy.indexOfFirst { m -> m.storeCode == storeCode }
                    val goodsIndex = cartGoodsListCopy[storeIndex].goodsList.indexOfFirst { n -> n.code == goodsCode }

                    when (type) {
                        "similar" -> Toast.makeText(context, "TODO", Toast.LENGTH_SHORT).show()
                        "delete" -> deleteGoods(storeIndex, goodsIndex)
                    }
                }
            })
        }
        //监听页面滚动显示与隐藏backTop按钮
        consecutiveScrollerLayout.setOnVerticalScrollChangeListener(object : ConsecutiveScrollerLayout.OnScrollChangeListener{
            override fun onScrollChange(v: View?, scrollY: Int, oldScrollY: Int, scrollState: Int) {
                if (scrollY >= DisplayUtil.getScreenHeight(requireContext())) {
                    backTop.visibility = View.VISIBLE
                } else {
                    backTop.visibility = View.GONE
                }
            }
        })

        //你可能还喜欢 或者 快点来看看 商品列表
        goodsList.run {
            addItemDecoration(SpacesItemDecoration(10))
            layoutManager = staggeredGridLayoutManager
            adapter = goodsListAdapter
        }
        //返回顶部
        backTop.setOnClickListener {
            consecutiveScrollerLayout.smoothScrollToChild(consecutiveScrollerLayout.getChildAt(0))
        }
        //全选
        totalCheckBox.setOnClickListener { checkAll() }
    }

    override fun initData() {
        viewModel.queryCartGoodsList(false)
        viewModel.initMaybeLikeList()
    }

    override fun invalidate() {
        withState(viewModel) {
            setFilterInfo(it.cartGoodsList)
            calcTotalInfo(it.cartGoodsList)
            cartGoodsListCopy = it.cartGoodsList
            cartGoodsAdapter.setList(convertCartData(it.cartGoodsList))
            if (it.fetchType === "refresh") {
                cartRefreshLayout.run { finishRefresh() }
                goodsListAdapter.loadMoreModule.loadMoreComplete()
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

    //设置占位符高度
    private fun initStatusBarPlaceholder() {
        val layoutParams = statusBarPlaceholder.layoutParams
        layoutParams.height = StatusBarUtil.getHeight()
        statusBarPlaceholder.layoutParams = layoutParams
    }

    //顶部快捷入口
    private fun showQuickEntry() {
        XPopup.Builder(this.requireContext())
            .popupAnimation(PopupAnimation.TranslateFromTop)
//            .hasShadowBg(false)
            .isLightStatusBar(true)
            .asCustom(QuickEntryPopup(this.requireContext()))
            .show()
    }

    //设置顶部搜索信息
    private fun setFilterInfo(list: List<StoreGoodsBean>) {
        val totalList = list.map(StoreGoodsBean::goodsList).flatMap { element -> element.asIterable() }
        filterAll.run {
            "全部 ${totalList.size}".also { text = it }
            setTextColor(Color.parseColor("#D8433F"))
        }
        discountTxt.text = "降价 0"
    }

    //点击店铺前面的checkbox
    private fun checkAllByStore(position: Int) {
        val dataList = cartGoodsListCopy
        val storeCheck = dataList[position].check!!
        if (!storeCheck) {
            dataList[position].check = true
            dataList[position].goodsList.forEach{v -> v.check = true}
        } else {
            dataList[position].check = false
            dataList[position].goodsList.forEach{v -> v.check = false}
        }

        viewModel.updateCartGoodsList(dataList)

        val totalFlag = dataList.indexOfFirst { v -> v.check == false }
        totalCheckBox.isChecked = totalFlag == -1
    }

    //点击每个商品前面的checkbox
    private fun checkGoods(parent: Int, position: Int) {
        val parentList = cartGoodsListCopy
        val childList = parentList[parent].goodsList

        childList[position].check = !(childList[position].check!!)

        val falseFLag = childList.indexOfFirst { v -> v.check == false }
        parentList[parent].check = falseFLag == -1
        parentList[parent].goodsList = childList

        viewModel.updateCartGoodsList(parentList)

        val totalFlag = parentList.indexOfFirst { v -> v.check == false }
        totalCheckBox.isChecked = totalFlag == -1
    }

    //点击最下面的全选按钮
    private fun checkAll() {
        val dataList = cartGoodsListCopy
        val isAllChecked = dataList.indexOfFirst { v -> v.check == false } == -1

        totalCheckBox.isChecked = !isAllChecked
        dataList.forEach { v -> v.check = !isAllChecked; v.goodsList.forEach { m -> m.check = !isAllChecked } }

        viewModel.updateCartGoodsList(dataList)
    }

    // 设置下面的全选 总价
    private fun calcTotalInfo(dataList: List<StoreGoodsBean>) {
        totalCheckBox.isChecked = dataList.indexOfFirst { v -> v.check == false } == -1

        var totalPrice: BigDecimal = BigDecimal.ZERO
        var totalNum = 0
        dataList.forEach { v ->
            v.goodsList.forEach { m -> if (m.check == true){
                totalNum += m.num
                totalPrice = totalPrice.add(m.price.toBigDecimal().multiply(m.num.toBigDecimal()))
            }}
        }

        "￥${totalPrice}".also { totalPriceTxt.text = it }
        "去结算(${totalNum})".also { btnGoOrder.text = it }
    }

    //删除购物车中的商品
    private fun deleteGoods(parentIndex: Int, index: Int) {
        val dataList = cartGoodsListCopy.toMutableList()
        val goodsList = dataList[parentIndex].goodsList
        goodsList.removeAt(index)

        if (goodsList.size == 0) {
            dataList.removeAt(parentIndex)
        }
        viewModel.updateCartGoodsList(dataList)
    }

    //修改购物车中的商品数量
    private fun setGoodsNum(bean: CartBean, value: Int) {
        val dataList = cartGoodsListCopy
        dataList.forEach{ v-> v.goodsList.forEach { m -> if (m.code == bean.code) m.num = value }}

        viewModel.updateCartGoodsList(dataList)
    }
}