package com.aries.mine.ui.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.aries.mine.R
import com.aries.mine.ui.MenuBean
import com.aries.mine.ui.adapter.FiveViewPagerAdapter
import kotlinx.android.synthetic.main.five_menu_main.view.*
import kotlin.math.ceil

class FiveMenuView(var content: Context, fragment: Fragment): FrameLayout(content) {
    private val pageSize: Int = 5
    private var points: ArrayList<ImageView> = arrayListOf()
    private var menuList: ArrayList<MenuBean> = arrayListOf()
    private var layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams(16, 16))

    private val fiveViewPagerAdapter: FiveViewPagerAdapter by lazy { FiveViewPagerAdapter(fragment, menuList, pageSize) }

    init {
        LayoutInflater.from(context).inflate(R.layout.five_menu_main, this, true)
        fiveViewPager.adapter = fiveViewPagerAdapter
    }

    fun setData(data: List<MenuBean>) {
        var totalPage = ceil(data.size * 1.0 / pageSize).toInt()
        fiveMemuLayout.visibility = View.VISIBLE
        menuList.clear()
        menuList.addAll(data)
        fiveViewPagerAdapter.notifyDataSetChanged()

        fiveViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                selectedPoints(position + 1)
            }
        })

        layoutParams.leftMargin = 5
        layoutParams.rightMargin = 5

        if (points.size != totalPage) {
            points.clear()
            for (current in 1..totalPage) {
                val imageView = ImageView(content)
                imageView.layoutParams = ViewGroup.LayoutParams(16,16)
                if(current == 1){
                    imageView.setBackgroundResource(R.drawable.selected_indicator)
                }else{
                    imageView.setBackgroundResource(R.drawable.normal_indicator)
                }
                points.add(imageView)
                pointsLayout.addView(imageView, layoutParams)
            }
        }
    }


    private fun selectedPoints(position: Int) {
        for(index in 1..points.size) {
            if (index == position) {
                points[index-1].setBackgroundResource(R.drawable.selected_indicator)
            } else {
                points[index-1].setBackgroundResource(R.drawable.normal_indicator)
            }
        }
    }
}