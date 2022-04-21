package com.aries.common.ui.detail

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.ImageLoader
import coil.load
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.alibaba.android.arouter.facade.annotation.Route
import com.aries.common.R
import com.aries.common.adapter.GoodsListAdapter
import com.aries.common.base.BaseActivity
import com.aries.common.constants.RouterPaths
import com.aries.common.ui.detail.adapter.ColorThumbListAdapter
import com.aries.common.ui.detail.adapter.GoodsDesImgListAdapter
import com.aries.common.util.CoilUtil
import com.aries.common.util.DisplayUtil
import com.aries.common.util.StatusBarUtil
import com.aries.common.widget.AnimationNestedScrollView
import com.google.android.material.tabs.TabLayout
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.RectangleIndicator
import com.youth.banner.transformer.AlphaPageTransformer
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail_des.*
import kotlinx.android.synthetic.main.activity_detail_goods.*
import kotlinx.android.synthetic.main.activity_detail_recommend.*
import kotlinx.android.synthetic.main.detail_header.*

@Route(path = RouterPaths.GOODS_DETAIL)
class DetailActivity: BaseActivity(R.layout.activity_detail), MavericksView {
    private val tabs = arrayListOf("商品", "评价", "详情", "推荐")
    private var imageLoader: ImageLoader = CoilUtil.getImageLoader()
    private val viewModel: DetailViewModal by viewModel()

    private val colorThumbListAdapter: ColorThumbListAdapter by lazy { ColorThumbListAdapter(R.layout.color_thumb_item, arrayListOf()) }
    private val goodsDesImgListAdapter: GoodsDesImgListAdapter by lazy { GoodsDesImgListAdapter(R.layout.activity_detail_des_img, arrayListOf()) }
    private val goodsListAdapter by lazy { GoodsListAdapter(arrayListOf()) }
    private val staggeredGridLayoutManager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun initView() {
        initLayout()
        initColorThumb()
        scrollerLayout.setOnAnimationScrollListener(object : AnimationNestedScrollView.OnAnimationScrollChangeListener{
            override fun onScrollChanged(dy: Float) {
                handleScroll(dy)
            }
        })
        back.setOnClickListener {
            finish()
        }
        backTop.setOnClickListener {
            scrollerLayout.smoothScrollTo(0, 0, 1000)
        }
        addStateChangeListener()
    }

    override fun initData() {
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
                    val current = bannerList.find { m -> m.select == true }
                    showGoodsImgBanner(current!!.imgList)
                    colorThumbListAdapter.setList(bannerList)
                    "${bannerList.size}色可选".also { colorOptionTv.text = it }
                }
                showGoodsInfo(goodsInfo)
                showDetailInfo(detailInfo)
                if (goodsList.isNotEmpty()) {
                    storeGoodsTv.text = "同店好货"
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

    private fun handleScroll(dy: Float) {
        if (dy >= DisplayUtil.getScreenHeight(this)) {
            backTop.visibility = View.VISIBLE
        } else {
            backTop.visibility = View.GONE
        }

        if (dy >= StatusBarUtil.getHeight()) {
            detailHeaderLayout.setBackgroundResource(R.color.white)
            detailHeaderTabLayout.visibility = View.VISIBLE
        } else {
            detailHeaderLayout.setBackgroundResource(R.color.transparent)
            detailHeaderTabLayout.visibility = View.GONE
        }
    }

    private fun showGoodsImgBanner(data: List<String>) {
        goodsImgBanner.run {
            adapter = object : BannerImageAdapter<String>(data) {
                override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerImageHolder {
                    val imageView = ImageView(parent?.context)
                    imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    imageView.scaleType = ImageView.ScaleType.FIT_XY
                    return BannerImageHolder(imageView)
                }
                override fun onBindView(holder: BannerImageHolder?, data: String?, position: Int, size: Int) {
                    holder?.imageView?.load(data!!, imageLoader ) {
                        crossfade(true)
                        placeholder(R.drawable.default_img)
                    }
                }
            }
            indicator = RectangleIndicator(this.context)
            setPageTransformer(AlphaPageTransformer())
            setIndicatorSelectedColorRes(R.color.theme_color)
            isAutoLoop(false)
        }
    }

    private fun initLayout() {
        StatusBarUtil.setBarTextModal(this, true)

        detailHeaderLayout.setPadding(0, StatusBarUtil.getHeight(), 0 , 0)
        detailHeaderTabLayout.removeAllTabs()
        detailHeaderTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    val title: TextView =  ((detailHeaderTabLayout.getChildAt(0) as LinearLayout).getChildAt(tab.position) as LinearLayout).getChildAt(1) as TextView
                    title.textSize = 20F
                    title.setTextAppearance(R.style.TabLayoutItemBold)
                }
            }
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    val title: TextView =  ((detailHeaderTabLayout.getChildAt(0) as LinearLayout).getChildAt(tab.position) as LinearLayout).getChildAt(1) as TextView
                    title.textSize = 18F
                    title.setTextAppearance(R.style.TabLayoutItemNormal)
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        tabs.forEach { v ->
            detailHeaderTabLayout.addTab(detailHeaderTabLayout.newTab().setText(v))
        }

        val lp = LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        lp.height = DisplayUtil.getScreenWidth(this) + StatusBarUtil.getHeight()
        goodsImgBanner.layoutParams = lp
        goodsImgBanner.setPadding(0, StatusBarUtil.getHeight(), 0, 0)

        goodsDesImgList.run {
            adapter = goodsDesImgListAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

        storeGoodsRv.run {
            adapter = goodsListAdapter
            layoutManager = staggeredGridLayoutManager
        }

        goodsListAdapter.loadMoreModule.setOnLoadMoreListener{
            viewModel.loadMoreRecommendList()
        }
    }

    override fun invalidate() {}

    private fun initColorThumb() {
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
    }

    private fun showGoodsInfo(goodsInfo: GoodsInfo) {
        if (goodsInfo.originalPrice.isNotEmpty()) {
            "￥${goodsInfo.originalPrice}".also { originalPriceTv.text = it }
        }
        goodsNameTv.text = goodsInfo.goodsName
    }

    private fun showDetailInfo(detailInfo: DetailInfo) {
        hdzqTv.text = "活动专区"
        hdzqIv.load(detailInfo.hdzq, imageLoader ) {
            crossfade(true)
        }
        dnyxTv.text = "店内优选"
        dnyxIv.load(detailInfo.dnyx, imageLoader ) {
            crossfade(true)
        }
        spjsTv.text = "商品介绍"
        goodsDesImgListAdapter.setList(detailInfo.introductionList)
    }
}