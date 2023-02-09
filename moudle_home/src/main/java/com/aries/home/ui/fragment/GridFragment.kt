package com.aries.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.fragment.app.Fragment
import com.aries.home.databinding.NineMemuGridviewBinding
import com.aries.home.ui.MenuBean
import com.aries.home.ui.adapter.NineGridAdapter

class GridFragment(data: MutableList<MenuBean>, index: Int, pageSize: Int): Fragment() {
    private lateinit var binding: NineMemuGridviewBinding

    private val nineGridAdapter: NineGridAdapter by lazy { NineGridAdapter(this.requireContext(), data, index, pageSize) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = NineMemuGridviewBinding.inflate(LayoutInflater.from(this.context))
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