package com.aries.cart.ui.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aries.cart.R
import com.aries.cart.ui.StoreGoodsBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class StoreListAdapter(layoutResId: Int, data: MutableList<StoreGoodsBean>): BaseQuickAdapter<StoreGoodsBean, BaseViewHolder>(layoutResId, data) {
    private lateinit var goodsListAdapter: StoreGoodsListAdapter

    override fun convert(holder: BaseViewHolder, item: StoreGoodsBean) {
        holder.setText(R.id.storeName, item.storeName)

        goodsListAdapter = StoreGoodsListAdapter(R.layout.fragment_cart_item_goods, arrayListOf())
        holder.getView<RecyclerView>(R.id.goodsItemList).run {
            layoutManager = LinearLayoutManager(this.context)
            adapter = goodsListAdapter
        }
        goodsListAdapter.setList(item.goodsList)
    }
}