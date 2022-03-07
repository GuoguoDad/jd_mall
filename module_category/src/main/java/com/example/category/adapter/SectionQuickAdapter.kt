package com.example.category.adapter

import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.category.R
import com.example.category.modal.CategoryModal

class SectionQuickAdapter(sectionHeadResId: Int, layoutResId: Int, data: MutableList<CategoryModal> ):
    BaseSectionQuickAdapter<CategoryModal, BaseViewHolder>(sectionHeadResId, layoutResId, data) {

    override fun convertHeader(helper: BaseViewHolder, item: CategoryModal) {
        helper.setText(R.id.textHeader, item.categoryName)
    }

    override fun convert(holder: BaseViewHolder, item: CategoryModal) {
        holder.setText(R.id.text_content, item.categoryName)
    }
}