package com.aries.category.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.aries.category.ui.CategoryBean
import com.example.category.R

class CategoryListAdapter(layoutResId: Int, brandList: MutableList<CategoryBean>): BaseQuickAdapter<CategoryBean, BaseViewHolder>(layoutResId, brandList) {

    @SuppressLint("ResourceAsColor")
    override fun convert(holder: BaseViewHolder, item: CategoryBean) {
        holder.setText(R.id.categoryName, item.name)
        holder.getView<LinearLayout>(R.id.categoryItemView).run {
            if (item?.isSelect == true) {
                setBackgroundColor(Color.parseColor("#FFFFFF"))
            } else {
                when(item.fillet){
                    "down"-> {
                        setBackgroundResource(R.drawable.shape_corner_bottom_right)
                    }
                    "up" -> {
                        setBackgroundResource(R.drawable.shape_corner_top_right)
                    }
                    else -> {
                        setBackgroundColor(Color.parseColor("#F2F2F2"))
                    }
                }
            }
        }
        holder.getView<TextView>(R.id.categoryName).run {
            typeface = if (item?.isSelect == true) {
                setTextColor(R.color.category_name_select_color)
                Typeface.defaultFromStyle(Typeface.BOLD)
            }else {
                setTextColor(R.color.category_name_select_color)
                Typeface.defaultFromStyle(Typeface.NORMAL)
            }
        }

    }
}