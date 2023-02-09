package com.aries.mine.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.fragment.app.Fragment
import com.aries.mine.databinding.FiveMenuGridviewBinding
import com.aries.mine.ui.MenuBean
import com.aries.mine.ui.adapter.FiveGridAdapter

class GridFragment(data: MutableList<MenuBean>, index: Int, pageSize: Int): Fragment() {
    private lateinit var binding: FiveMenuGridviewBinding

    private val nineGridAdapter: FiveGridAdapter by lazy { FiveGridAdapter(this.requireContext(), data, index, pageSize) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FiveMenuGridviewBinding.inflate(LayoutInflater.from(this.context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView() {
        binding.gridView.run {
            adapter = nineGridAdapter as ListAdapter
        }
    }
}