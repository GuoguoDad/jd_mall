package com.aries.home.ui.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.aries.home.R
import com.aries.home.ui.MenuBean
import com.aries.home.ui.fragment.GridFragment
import kotlinx.android.synthetic.main.nine_memu_main.view.*
import kotlin.math.ceil

class NineMenuView(var content: Context): FrameLayout(content) {
    private val pageSize: Int = 10
    private var points: ArrayList<ImageView> = arrayListOf()

    init {
        LayoutInflater.from(context).inflate(R.layout.nine_memu_main, this, true)
    }

    fun setData(data: List<MenuBean>, fragment: Fragment) {
        var totalPage = ceil(data.size * 1.0 / pageSize).toInt()

        nineViewPager.adapter = object : FragmentStateAdapter(fragment){
            override fun getItemCount(): Int = totalPage
            override fun createFragment(position: Int): Fragment {
                return GridFragment(data.toMutableList(), position, pageSize)
            }
        }
        nineViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                selectedPoints(position + 1)
            }
        })

        points.clear()
        pointsLayout.removeAllViews()
        for (current in 1..totalPage) {
            val imageView = ImageView(content)
            imageView.layoutParams = ViewGroup.LayoutParams(16,16)
            if(current == 1){
                imageView.setBackgroundResource(R.drawable.selected_indicator)
            }else{
                imageView.setBackgroundResource(R.drawable.normal_indicator)
            }
            points.add(imageView)

            var layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams(16, 16))
            layoutParams.leftMargin = 5
            layoutParams.rightMargin = 5
            pointsLayout.addView(imageView, layoutParams)
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