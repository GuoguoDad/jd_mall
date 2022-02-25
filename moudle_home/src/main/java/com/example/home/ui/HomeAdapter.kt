package com.example.home.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.home.view.BannerView
import com.example.home.view.GoodsListView

class HomeAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var datas: ArrayList<HomeItem<HomeItemBean>> = ArrayList()

    companion object {
        const val ITEM_TYPE_BANNER = 1
        const val ITEM_TYPE_GOODS = 2
    }

    fun setData(list: List<HomeItem<HomeItemBean>>) {
        datas.clear()
        datas.addAll(list)
        notifyDataSetChanged()
    }

    fun addData(list: List<HomeItem<HomeItemBean>>) {
        datas.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_BANNER -> ItemHolder(BannerView(parent.context))
            else -> ItemHolder(GoodsListView(parent.context))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemData = datas[position].data
        when (getItemViewType(position)) {
            ITEM_TYPE_BANNER -> (holder.itemView as BannerView).setData(itemData.bannerList!!)
            ITEM_TYPE_GOODS -> (holder.itemView as GoodsListView).setData(itemData.goodsList!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (datas[position].type) {
            "1" -> ITEM_TYPE_BANNER
            else -> ITEM_TYPE_GOODS
        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}