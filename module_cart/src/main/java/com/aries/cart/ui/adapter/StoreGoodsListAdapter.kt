package com.aries.cart.ui.adapter

import android.widget.CheckBox
import android.widget.ImageView
import coil.ImageLoader
import coil.load
import com.aries.cart.R
import com.aries.cart.ui.CartGoodsBean
import com.aries.cart.ui.listener.OnStepperChangeListener
import com.aries.common.util.CoilUtil
import com.aries.common.widget.Stepper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.orhanobut.logger.Logger

open class StoreGoodsListAdapter(layoutResId: Int, data: MutableList<CartGoodsBean>): BaseQuickAdapter<CartGoodsBean, BaseViewHolder>(layoutResId, data) {
    private var imageLoader: ImageLoader = CoilUtil.getImageLoader()
    private var onStepperChangeListener: OnStepperChangeListener? = null

    override fun convert(holder: BaseViewHolder, item: CartGoodsBean) {
        holder.getView<ImageView>(R.id.cartGoodsImg).load(item.imgUrl, imageLoader ) {
            crossfade(true)
            placeholder(R.drawable.default_img)
            error(R.drawable.default_img)
        }

        holder.getView<CheckBox>(R.id.goodsCheckBox).isChecked = item.check!!
        holder.setText(R.id.cartGoodsDes, item.description)
        holder.setText(R.id.cartGoodsPrice, "￥${item.price}")

        holder.getView<Stepper>(R.id.buyNum).run {
            setInputValue(item.num)
            setOnChangeValueListener(object: Stepper.OnChangeValueListener {
                override fun onChangeValue(value: String) {
                    onStepperChangeListener?.onStepperChange(item, value.toInt())
                }
            })
        }
    }

    open fun setOnStepperChangeListener(onStepperChangeListener: OnStepperChangeListener) {
        this.onStepperChangeListener = onStepperChangeListener
    }
}