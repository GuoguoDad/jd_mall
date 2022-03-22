package com.aries.cart.ui.adapter

import android.view.View
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aries.cart.R
import com.aries.cart.ui.StoreGoodsBean
import com.aries.cart.ui.listener.OnChildItemChildClickListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

open class StoreListAdapter(layoutResId: Int, data: MutableList<StoreGoodsBean>): BaseQuickAdapter<StoreGoodsBean, BaseViewHolder>(layoutResId, data) {
    private lateinit var goodsListAdapter: StoreGoodsListAdapter

    private var mOnChildItemChildClickListener: OnChildItemChildClickListener? = null

    override fun convert(holder: BaseViewHolder, item: StoreGoodsBean) {
        goodsListAdapter = StoreGoodsListAdapter(R.layout.fragment_cart_item_goods, arrayListOf())
        goodsListAdapter.addChildClickViewIds(R.id.goodsCheckBox)
        goodsListAdapter.setOnItemChildClickListener  { _, view, position ->
            var index = this.data.indexOfFirst { v -> v.storeCode == item.storeCode }

            this.setOnChildItemChildClick(view, index, position)
        }

        holder.setText(R.id.storeName, item.storeName)
        holder.getView<CheckBox>(R.id.storeCheckBox).isChecked = item.check!!
        holder.getView<RecyclerView>(R.id.goodsItemList).run {
            layoutManager = LinearLayoutManager(this.context)
            adapter = goodsListAdapter
            isNestedScrollingEnabled = false
        }
        goodsListAdapter.setList(item.goodsList)
    }

    protected open fun setOnChildItemChildClick(v: View, parent: Int, position: Int) {
        mOnChildItemChildClickListener?.onItemChildClick(this, v, parent, position)
    }


    open fun setOnChildItemChildClickListener(listener: OnChildItemChildClickListener) {
        this.mOnChildItemChildClickListener = listener
    }
}