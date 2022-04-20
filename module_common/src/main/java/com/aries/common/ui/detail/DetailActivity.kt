package com.aries.common.ui.detail

import android.os.Build
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.LinearLayoutCompat
import coil.ImageLoader
import coil.load
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.alibaba.android.arouter.facade.annotation.Route
import com.aries.common.R
import com.aries.common.base.BaseActivity
import com.aries.common.constants.RouterPaths
import com.aries.common.util.CoilUtil
import com.aries.common.util.DisplayUtil
import com.aries.common.util.StatusBarUtil
import com.google.android.material.tabs.TabLayout
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.indicator.RectangleIndicator
import com.youth.banner.indicator.RoundLinesIndicator
import com.youth.banner.transformer.AlphaPageTransformer
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_detail_goods.*
import kotlinx.android.synthetic.main.detail_header.*

@Route(path = RouterPaths.GOODS_DETAIL)
class DetailActivity: BaseActivity(R.layout.activity_detail), MavericksView {
    private val tabs = arrayListOf("商品", "评价", "详情", "推荐")
    private var imageLoader: ImageLoader = CoilUtil.getImageLoader()

    private val viewModel: DetailViewModal by viewModel()

    override fun initView() {
        initLayout()
        back.setOnClickListener {
            finish()
        }
        addStateChangeListener()
    }

    override fun initData() {
        viewModel.queryGoodsDetail()
    }


    private fun addStateChangeListener() {
        viewModel.onEach(
            DetailState::currentBanner
        ) { currentBanner ->
            run {
                if (currentBanner.imgList.isNotEmpty()) {
                    showGoodsImgBanner(currentBanner.imgList)
                }
            }
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
        lp.height = DisplayUtil.getScreenWidth(this)
        lp.topMargin = StatusBarUtil.getHeight()
        goodsImgBanner.layoutParams = lp
    }

    override fun invalidate() {}
}