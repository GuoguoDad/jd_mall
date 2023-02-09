package com.aries.home.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.aries.home.R
import com.aries.home.databinding.NineMemuMainBinding
import com.aries.home.ui.MenuBean
import com.aries.home.ui.adapter.NineViewPagerAdapter
import kotlin.math.ceil

class NineMenuView(var content: Context, fragment: Fragment): FrameLayout(content) {
    private var binding: NineMemuMainBinding =
        NineMemuMainBinding.inflate(LayoutInflater.from(context), this, true)

    private val pageSize: Int = 10
    private var points: ArrayList<ImageView> = arrayListOf()
    private var menuList: ArrayList<MenuBean> = arrayListOf()
    private var layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams(16, 16))

    private val nineViewPagerAdapter: NineViewPagerAdapter by lazy { NineViewPagerAdapter(fragment, menuList, pageSize) }

    init {
        binding.nineViewPager.adapter = nineViewPagerAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<MenuBean>) {
        var totalPage = ceil(data.size * 1.0 / pageSize).toInt()

        menuList.clear()
        menuList.addAll(data)
        nineViewPagerAdapter.notifyDataSetChanged()

        binding.nineViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
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
                binding.pointsLayout.addView(imageView, layoutParams)
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