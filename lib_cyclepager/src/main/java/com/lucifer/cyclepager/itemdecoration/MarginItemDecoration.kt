package com.lucifer.cyclepager.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration(private val marginPx: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val linearLayoutManager: LinearLayoutManager = requireLinearLayoutManager(parent)
        if (linearLayoutManager.getOrientation() === LinearLayoutManager.VERTICAL) {
            outRect.top = marginPx
            outRect.bottom = marginPx
        } else {
            outRect.left = marginPx
            outRect.right = marginPx
        }
    }

    private fun requireLinearLayoutManager(parent: RecyclerView): LinearLayoutManager {
        val layoutManager: RecyclerView.LayoutManager? = parent.getLayoutManager()
        if (layoutManager is LinearLayoutManager) {
            return layoutManager as LinearLayoutManager
        }
        throw IllegalStateException("The layoutManager must be LinearLayoutManager")
    }
}
