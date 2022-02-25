package com.lucifer.cyclepager.util

import android.content.res.Resources
import android.util.TypedValue

object DisplayUtil {
    fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            dp,
            Resources.getSystem().getDisplayMetrics())
    }
}