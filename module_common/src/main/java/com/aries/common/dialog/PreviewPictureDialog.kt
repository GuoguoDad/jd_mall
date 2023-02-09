package com.aries.common.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.viewpager.widget.ViewPager
import com.aries.common.R
import com.aries.common.adapter.PreviewPictureAdapter
import com.aries.common.databinding.PreviewPictureBinding

class PreviewPictureDialog(context: Context): Dialog(context, R.style.transparentBgDialog) {
    private var binding: PreviewPictureBinding = PreviewPictureBinding.inflate(LayoutInflater.from(context))

    init {
        setContentView(R.layout.preview_picture)

        val window: Window? = window
        val params: WindowManager.LayoutParams? = window?.attributes
        if (params != null) {
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.horizontalMargin = 0F
        }
        window?.attributes = params
    }

    fun show(urls: List<String>, position: Int) {
        super.show()

        "${(position + 1)}/${urls.size}".also { binding.index.text = it }
        binding.viewPager.run {
            adapter = PreviewPictureAdapter(context, urls)
            currentItem = position
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) { }
                override fun onPageScrollStateChanged(state: Int) { }
                override fun onPageSelected(position: Int) {
                    "${(position + 1)}/${adapter?.count}".also { binding.index.text = it }
                }
            })
        }
    }


    override fun onBackPressed() {
        cancel()
        dismiss()
    }
}