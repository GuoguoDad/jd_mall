package com.lucifer.cyclepager.util

object CyclePositionUtil {
    fun getRealPosition(position: Int, realItemCount: Int): Int {
        val fixPosition: Int
        fixPosition = if (position == 0) {
            realItemCount - 1
        } else if (position == realItemCount + 1) {
            0
        } else {
            position - 1
        }
        return fixPosition
    }
}

