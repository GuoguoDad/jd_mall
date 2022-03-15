package com.example.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.ImageLoader
import coil.load
import com.example.home.R
import com.example.home.ui.MenuBean
import android.widget.TextView

class NineGridAdapter(var context: Context, var data: MutableList<MenuBean>, var index: Int, var pageSize: Int): BaseAdapter()  {
    private var imageLoader: ImageLoader = Coil.imageLoader(context)

    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View {
        var menuBean = getItem(position)

        var convertView = p1
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.nine_memu_item, parent, false)
        }
        var holder = ViewHolder(convertView!!)
        convertView.tag = holder
        holder.itemView.findViewById<ImageView>(R.id.menuIcon).load(menuBean.menuIcon, imageLoader ) {
            crossfade(true)
            placeholder(R.drawable.default_img)
            error(R.drawable.default_img)
        }
        holder.itemView.findViewById<TextView>(R.id.menuName).text = menuBean.menuName

        return convertView!!
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