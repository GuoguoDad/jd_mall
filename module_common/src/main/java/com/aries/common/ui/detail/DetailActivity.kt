package com.aries.common.ui.detail

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.ImageLoader
import coil.load
import com.airbnb.mvrx.DeliveryMode
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.aries.common.R
import com.aries.common.adapter.GoodsListAdapter
import com.aries.common.base.BaseActivity
import com.aries.common.constants.RouterPaths
import com.aries.common.dialog.LoadingDialog
import com.aries.common.ui.detail.adapter.AppraiseListSectionAdapter
import com.aries.common.ui.detail.adapter.ColorThumbListAdapter
import com.aries.common.ui.detail.adapter.GoodsDesImgListAdapter
import com.aries.common.util.CoilUtil
import com.aries.common.util.DisplayUtil
import com.aries.common.util.PixelUtil
import com.aries.common.util.StatusBarUtil
import com.google.android.material.tabs.TabLayout
import com.gyf.immersionbar.ImmersionBar
import com.stx.xhb.androidx.XBanner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.RectangleIndicator
import com.youth.banner.transformer.AlphaPageTransformer
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail_appraise.*
import kotlinx.android.synthetic.main.activity_detail_des.*
import kotlinx.android.synthetic.main.activity_detail_footer.*
import kotlinx.android.synthetic.main.activity_detail_goods.*
import kotlinx.android.synthetic.main.activity_detail_recommend.*
import kotlinx.android.synthetic.main.detail_header.*

@Route(path = RouterPaths.GOODS_DETAIL)
class DetailActivity: BaseActivity(R.layout.activity_detail), MavericksView {
    private val tabs = arrayListOf("??????", "??????", "??????", "??????")
    private var imageLoader: ImageLoader = CoilUtil.getImageLoader()
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this) }

    private var statusBarHeight: Int = 0
    private val viewModel: DetailViewModal by viewModel()
    //?????????????????????adapter
    private val colorThumbListAdapter: ColorThumbListAdapter by lazy { ColorThumbListAdapter(R.layout.color_thumb_item, arrayListOf()) }
    //????????????adapter
    private val appraiseListSectionAdapter: AppraiseListSectionAdapter by lazy {
        AppraiseListSectionAdapter(R.layout.activity_detail_appraise_header, R.layout.activity_detail_appraise_item,  arrayListOf())
    }
    private val gridLayoutManager: GridLayoutManager by lazy { GridLayoutManager(this, 4) }
    //????????????adapter
    private val goodsDesImgListAdapter: GoodsDesImgListAdapter by lazy { GoodsDesImgListAdapter(R.layout.activity_detail_des_img, arrayListOf()) }
    //??????????????????adapter
    private val goodsListAdapter by lazy { GoodsListAdapter(arrayListOf()) }
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun initView() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(true).init()
        statusBarHeight = StatusBarUtil.getHeight()
        //??????????????????????????????
        StatusBarUtil.setBarTextModal(this, true)
        //??????tabLayout
        detailHeaderLayout.setPadding(0, StatusBarUtil.getHeight(), 0 , 0)
        detailHeaderTabLayout.removeAllTabs()
        detailHeaderTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    // ???????????????tab?????? ????????????????????????
                    scrollToPositionByTab(tab.position)
                }
            }
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onTabUnselected(tab: TabLayout.Tab?) { }
            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    // ???????????????tab?????? ????????????????????????
                    scrollToPositionByTab(tab.position)
                }
            }
        })
        tabs.forEach { v ->
            detailHeaderTabLayout.addTab(detailHeaderTabLayout.newTab().setText(v))
        }

        //?????????????????? banner
        val lp = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        lp.height = DisplayUtil.getScreenWidth(this) + StatusBarUtil.getHeight()
        goodsImgBanner.run {
            layoutParams = lp
            setPadding(0, StatusBarUtil.getHeight(), 0, 0)
        }

        //???????????????????????????
        colorThumbRv.run {
            adapter = colorThumbListAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        }
        colorThumbListAdapter.setOnItemClickListener { adapter, _, position ->
            val list: MutableList<BannerBean> = adapter.data as MutableList<BannerBean>
            val banner = list[position]
            val flag = banner.select!!

            for (i in list.indices) {
                list[i].select = false
            }
            list[position].select = true

            if (!flag) {
                viewModel.updateSelectColorThumb(list)
                colorThumbListAdapter.setList(list)
                showGoodsImgBanner(banner.imgList)
            }
        }

        //????????????
        appraiseRecyclerView.run {
            layoutManager = gridLayoutManager
            adapter = appraiseListSectionAdapter
        }

        //??????????????????????????????(????????????)
        goodsDesImgList.run {
            adapter = goodsDesImgListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        //????????????
        storeGoodsRv.run {
            adapter = goodsListAdapter
            layoutManager = staggeredGridLayoutManager
        }
        //???????????? ??????????????????
        goodsListAdapter.loadMoreModule.setOnLoadMoreListener{
            viewModel.loadMoreRecommendList()
        }

        //????????????????????????tab??????
        scrollerLayout.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            handleScroll(scrollY)
        })
        //??????
        back.setOnClickListener {
            finish()
        }
        //????????????
        backTop.setOnClickListener {
            scrollerLayout.smoothScrollTo(0, 0, 500)
        }
        //?????????????????????
        detailCartLayout.setOnClickListener {
            ARouter.getInstance().build(RouterPaths.CART_ACTIVITY).navigation()
        }
        addStateChangeListener()
    }

    override fun initData() {
        loadingDialog.show()
        viewModel.queryGoodsDetail()
        viewModel.initRecommendList()
    }

    private fun addStateChangeListener() {
        viewModel.onEach(
            DetailState::bannerList,
            DetailState::goodsInfo,
            DetailState::detailInfo,
            DetailState::goodsList,
            DetailState::nextPageGoodsList,
            DetailState::currentPage,
            DetailState::totalPage
        ) { bannerList, goodsInfo, detailInfo, goodsList, nextPageGoodsList, currentPage, totalPage ->
            run {
                if (bannerList.isNotEmpty()) {
                    loadingDialog.dismiss()
                    val current = bannerList.find { m -> m.select == true }
                    showGoodsImgBanner(current!!.imgList)
                    colorThumbListAdapter.setList(bannerList)
                    "${bannerList.size}?????????".also { colorOptionTv.text = it }
                }
                showGoodsInfo(goodsInfo)
                if (goodsInfo.appraiseList.isNotEmpty()) {
                    detailAppraiseLayout.visibility = View.VISIBLE
                    appraiseListSectionAdapter.setList(goodsInfo.appraiseList)
                }
                showDetailInfo(detailInfo)
                if (goodsList.isNotEmpty()) {
                    detailRecommend.visibility = View.VISIBLE
                    storeGoodsTv.text = "????????????"
                    goodsListAdapter.setList(goodsList)
                }
                if (nextPageGoodsList.isNotEmpty()) {
                    goodsListAdapter.addData(nextPageGoodsList)
                    if (currentPage <= totalPage)
                        goodsListAdapter.loadMoreModule.loadMoreComplete()
                    else
                        goodsListAdapter.loadMoreModule.loadMoreEnd()
                }
            }
        }
    }

    private var currentTabPosition: Int = 0

    private fun handleScroll(dy: Int) {
        val delayDy = dy * 0.65f
        if (delayDy >= DisplayUtil.getScreenHeight(this)) {
            backTop.visibility = View.VISIBLE
        } else {
            backTop.visibility = View.GONE
        }

        if (delayDy >= StatusBarUtil.getHeight()) {
            detailHeaderLayout.setBackgroundResource(R.color.white)
            detailHeaderTabLayout.visibility = View.VISIBLE
        } else {
            detailHeaderLayout.setBackgroundResource(R.color.transparent)
            detailHeaderTabLayout.visibility = View.INVISIBLE
        }
        val appraiseToTop = detailAppraiseLayout.top - statusBarHeight - PixelUtil.toPixelFromDIP(50f)
        val goodsDesToTop = detailDesLayout.top - statusBarHeight - PixelUtil.toPixelFromDIP(50f)
        val recommendToTop = detailRecommend.top - statusBarHeight - PixelUtil.toPixelFromDIP(50f)

        val tabPosition = when {
            dy in 0 until appraiseToTop.toInt() -> 0
            dy < goodsDesToTop -> 1
            dy < recommendToTop -> 2
            else -> 3
        }
        if (currentTabPosition != tabPosition) {
            currentTabPosition = tabPosition
            detailHeaderTabLayout.setScrollPosition(tabPosition, 0f, true)
        }
    }

    private fun showGoodsImgBanner(data: List<String>) {
        goodsImgBanner.run {
            val list = data.map { v -> TopBanner(v,"") }
            setBannerData(list)
            loadImage { _, _, view, position ->
                (view as ImageView).load(data[position], imageLoader) {
                    crossfade(true)
                }
            }
        }
    }

    override fun invalidate() {}

    private fun showGoodsInfo(goodsInfo: GoodsInfo) {
        if (goodsInfo.originalPrice.isNotEmpty()) {
            detailGoodsLayout.visibility = View.VISIBLE
            "???${goodsInfo.originalPrice}".also { originalPriceTv.text = it }
        }
        goodsNameTv.text = goodsInfo.goodsName
    }

    private fun showDetailInfo(detailInfo: DetailInfo) {
        hdzqTv.text = "????????????"
        hdzqIv.load(detailInfo.hdzq, imageLoader ) {
            crossfade(true)
        }
        dnyxTv.text = "????????????"
        dnyxIv.load(detailInfo.dnyx, imageLoader ) {
            crossfade(true)
        }
        spjsTv.text = "????????????"
        if (detailInfo.introductionList.isNotEmpty()) {
            detailDesLayout.visibility = View.VISIBLE
            goodsDesImgListAdapter.setList(detailInfo.introductionList)
            appraiseTxt.text = "??????"
        }
    }

    /**
     * ????????????tab???????????????????????????
     */
    private fun scrollToPositionByTab(position: Int) {
        when (position) {
            0 -> scrollerLayout.smoothScrollTo(0, 0, 200)
            1 -> scrollerLayout.smoothScrollTo(0, detailAppraiseLayout.top - statusBarHeight - PixelUtil.toPixelFromDIP(50f).toInt() , 200)
            2 -> scrollerLayout.smoothScrollTo(0, detailDesLayout.top - statusBarHeight - PixelUtil.toPixelFromDIP(50f).toInt(), 200)
            3 -> scrollerLayout.smoothScrollTo(0, detailRecommend.top - statusBarHeight - PixelUtil.toPixelFromDIP(50f).toInt(), 200)
        }
    }
}