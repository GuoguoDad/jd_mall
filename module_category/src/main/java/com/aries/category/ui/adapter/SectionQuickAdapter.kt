package com.aries.category.ui.adapter

import android.widget.ImageView
import coil.load
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.aries.category.R
import com.aries.category.ui.modal.CategoryModal
import com.aries.common.util.CoilUtil

class SectionQuickAdapter(sectionHeadResId: Int, layoutResId: Int, data: MutableList<CategoryModal> ):
    BaseSectionQuickAdapter<CategoryModal, BaseViewHolder>(sectionHeadResId, layoutResId, data) {
    private var imageLoader = CoilUtil.getImageLoader()

    override fun convertHeader(helper: BaseViewHolder, item: CategoryModal) {
        helper.setText(R.id.textHeader, item.categoryName)
    }

    override fun convert(holder: BaseViewHolder, item: CategoryModal) {
        holder.getView<ImageView>(R.id.thirdCategoryIcon).load(item.iconUrl, imageLoader ) {
            crossfade(true)
            placeholder(R.drawable.default_img)
            error(R.drawable.default_img)
        }
        holder.setText(R.id.text_content, item.categoryName)
    }
}