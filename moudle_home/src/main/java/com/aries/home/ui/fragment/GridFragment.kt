package com.aries.home.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import com.alibaba.android.arouter.launcher.ARouter
import com.aries.common.base.BaseFragment
import com.aries.common.constants.RouterPaths
import com.aries.home.databinding.NineMemuGridviewBinding
import com.aries.home.ui.MenuBean
import com.aries.home.ui.adapter.NineGridAdapter
import com.aries.home.ui.listener.OnGridItemClickListener

class GridFragment(data: MutableList<MenuBean>, index: Int, pageSize: Int): BaseFragment<NineMemuGridviewBinding>() {
    private val nineGridAdapter: NineGridAdapter by lazy { NineGridAdapter(this.requireContext(), data, index, pageSize) }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): NineMemuGridviewBinding {
        return NineMemuGridviewBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        nineGridAdapter.setOnGridItemClickListener(object : OnGridItemClickListener {
            override fun onItemClick(item: MenuBean) {
                ARouter.getInstance().build(RouterPaths.WebView_ACTIVITY)
                    .withBoolean("isDarkTheme", true)
                    .withString("title",item.menuName)
                    .withString("url",item.h5url)
                    .navigation()
            }
        })
        binding.gridView.run {
            adapter = nineGridAdapter as ListAdapter
        }
    }

    override fun initData() {}
}