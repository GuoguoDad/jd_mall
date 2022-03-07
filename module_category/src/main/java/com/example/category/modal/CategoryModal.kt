package com.example.category.modal

import com.chad.library.adapter.base.entity.SectionEntity

data class CategoryModal(var iconUrl: String?, var categoryName: String,var categoryCode: String, var isTitle: Boolean): SectionEntity {
    override val isHeader: Boolean
        get() = this.isTitle
}