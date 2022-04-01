package com.aries.cart.ui.adapter

import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import coil.load
import com.aries.cart.R
import com.aries.cart.ui.CartBean
import com.aries.cart.ui.listener.OnStepperChangeListener
import com.aries.common.util.CoilUtil
import com.aries.common.widget.Stepper
import com.aries.common.widget.SwipeMenuLayout
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder

open class CartGoodsAdapter(data: MutableList<CartBean>): BaseMultiItemQuickAdapter<CartBean, BaseViewHolder>(data) {
    private var imageLoader = CoilUtil.getImageLoader()
    private var onStepperChangeListener: OnStepperChangeListener? = null

    init {
        addItemType(1, R.layout.fragment_cart_item_store)
        addItemType(2, R.layout.fragment_cart_item_goods)
    }

    override fun convert(holder: BaseViewHolder, item: CartBean) {
        when (holder.itemViewType) {
            1 -> {
                holder.setText(R.id.storeName, item.storeName)
                holder.getView<CheckBox>(R.id.storeCheckBox).isChecked = item.check
            }
            2 -> {
                holder.getView<ImageView>(R.id.cartGoodsImg).load(item.imgUrl, imageLoader) {
                    crossfade(true)
                    placeholder(R.drawable.default_img)
                    error(R.drawable.default_img)
                }

                holder.getView<CheckBox>(R.id.goodsCheckBox).isChecked = item.check
                holder.setText(R.id.cartGoodsDes, item.description)
                holder.setText(R.id.cartGoodsPrice, "￥${item.price}")

                holder.getView<Stepper>(R.id.buyNum).run {
                    setInputValue(item.num!!)
                    setData(item)
                }
            }
        }
    }

    override fun bindViewClickListener(viewHolder: BaseViewHolder, viewType: Int) {
        super.bindViewClickListener(viewHolder, viewType)
        when (viewType) {
            2 -> {
                viewHolder.itemView.findViewById<Stepper>(R.id.buyNum).run {
                    setOnChangeValueListener(object : Stepper.OnChangeValueListener {
                        override fun onChangeValue(value: String) {
                            val data = getData()
                            if (data != null && (data as CartBean).num != value.toInt()) {
                                onStepperChangeListener?.onStepperChange(data, value.toInt())
                            }
                        }
                    })
                }
            }
        }
    }

    open fun setOnStepperChangeListener(onStepperChangeListener: OnStepperChangeListener) {
        this.onStepperChangeListener = onStepperChangeListener
    }
}