package com.aries.common.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class SpacesItemDecoration(space: Int): RecyclerView.ItemDecoration() {
    private var space = 0

    init {
       this.space = space
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State ) {
        outRect.left = this.space
        outRect.right = this.space
        outRect.bottom = this.space
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = this.space
        }
    }
}