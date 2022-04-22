package com.aries.common.ui.detail.adapter

import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.aries.common.R
import com.aries.common.ui.detail.AppraiseBean
import com.aries.common.util.CoilUtil
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class AppraiseListSectionAdapter(sectionHeadResId: Int, layoutResId: Int, data: MutableList<AppraiseBean> ):
    BaseSectionQuickAdapter<AppraiseBean, BaseViewHolder>(sectionHeadResId, layoutResId, data) {
    private var imageLoader = CoilUtil.getImageLoader()


    override fun convertHeader(helper: BaseViewHolder, item: AppraiseBean) {
        helper.getView<ImageView>(R.id.commentatorImg).load(item.headerUrl, imageLoader ) {
            crossfade(true)
            placeholder(R.drawable.default_img)
            error(R.drawable.default_img)
        }
        helper.setText(R.id.commentatorTxt, item.userName)
        helper.setText(R.id.contentTxt, item.content)
        helper.setText(R.id.modelTxt, "${item.color};${item.size}")
    }

    override fun convert(holder: BaseViewHolder, item: AppraiseBean) {
        holder.getView<ImageView>(R.id.appraiseImg).load(item.url, imageLoader ) {
            crossfade(true)
            placeholder(R.drawable.default_img)
            error(R.drawable.default_img)
        }
    }
}