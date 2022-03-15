package com.example.common.util

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView

object UnreadMsgUtil {
    fun show(numView: TextView?, num: Int) {
        if (numView == null) {
            return
        }
        val lp = numView.layoutParams
        val dm = numView.resources.displayMetrics
        numView.visibility = View.VISIBLE

        when (num) {
            in 1..9 -> { //圆
                lp.width = (15 * dm.density).toInt()
                numView.text = "$num"
            }
            in 10..99 -> {
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT
                numView.setPadding((4 * dm.density).toInt(), 0, (4 * dm.density).toInt(), 0)
                numView.text = "$num"
            }
            else -> {
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT
                numView.setPadding((4 * dm.density).toInt(), 0, (4 * dm.density).toInt(), 0)
                numView.text = "99+"
            }
        }
        numView.layoutParams = lp
    }
}