package com.aries.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import com.aries.home.R
import com.aries.home.ui.MenuBean
import android.widget.TextView
import com.aries.common.util.CoilUtil
import com.aries.home.ui.listener.OnGridItemClickListener

open class NineGridAdapter(var context: Context, var data: MutableList<MenuBean>, var index: Int, var pageSize: Int): BaseAdapter()  {
    private var imageLoader: ImageLoader = CoilUtil.getImageLoader()
    private var mOnGridItemClickListener: OnGridItemClickListener? = null

    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View {
        var menuBean = getItem(position)

        var convertView = p1
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.nine_memu_item, parent, false)
        }
        val holder = ViewHolder(convertView!!)
        convertView.tag = holder
        holder.itemView.findViewById<ImageView>(R.id.menuIcon).load(menuBean.menuIcon, imageLoader ) {
            crossfade(true)
            placeholder(R.drawable.default_img)
            error(R.drawable.default_img)
        }
        holder.itemView.findViewById<TextView>(R.id.menuName).text = menuBean.menuName
        holder.itemView.findViewById<LinearLayout>(R.id.nineMenuLayout).setOnClickListener{
            this.mOnGridItemClickListener?.onItemClick(menuBean)
        }
        return convertView
    }

    open fun setOnGridItemClickListener(onGridItemClickListener: OnGridItemClickListener) {
        this.mOnGridItemClickListener = onGridItemClickListener
    }

    override fun getCount(): Int {
        return if (data.size > (index + 1) * pageSize)
            pageSize
        else
            data.size - index * pageSize
    }

    override fun getItem(position: Int): MenuBean {
        return data[position + index * pageSize]
    }

    override fun getItemId(position: Int): Long {
        return (position + index * pageSize).toLong()
    }

    class ViewHolder(itemView: View) :  RecyclerView.ViewHolder(itemView)
}