package com.aries.common.ui.detail

object ConvertUtil {
    fun setDefaultSelect(data: List<BannerBean>): List<BannerBean> {
        for (i in data.indices) {
            data[i].select = i == 0
        }
        return data
    }
}