package com.aries.cart.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aries.cart.R
import com.aries.cart.ui.bean.QuickMenuBean

class QuickEntryPopupAdapter(var context: Context, var data: ArrayList<QuickMenuBean>): BaseAdapter() {

    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View {
        var menuBean = getItem(position)

        var convertView = p1
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.quick_entry_gridvew_item, parent, false)
        }
        var holder = ViewHolder(convertView!!)
        convertView.tag = holder
        holder.itemView.findViewById<ImageView>(R.id.menuIcon).setImageResource(menuBean.menuIcon)
        holder.itemView.findViewById<TextView>(R.id.menuName).text = menuBean.menuName

        return convertView!!
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): QuickMenuBean {
        return data[p0]
    }

    override fun getItemId(p0: Int): Long {
        return  p0.toLong()
    }

    class ViewHolder(itemView: View) :  RecyclerView.ViewHolder(itemView)
}