package com.example.home.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.home.R
import com.example.home.adapter.GoodsListAdapter
import com.example.home.ui.GoodsBean
import kotlinx.android.synthetic.main.fragment_list.view.*

class GoodsListView: FrameLayout {

    private val goodsListAdapter: GoodsListAdapter by lazy { GoodsListAdapter(R.layout.fragment_list_item, arrayListOf()) }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.fragment_list, this, true)
        recyclerView.run {
            layoutManager = LinearLayoutManager(context)
            goodsListAdapter.addChildClickViewIds(R.id.home_recycle_list_item)
            goodsListAdapter.setOnItemClickListener{ _, view, position ->
                if (view.id == R.id.home_recycle_list_item) {
                    Toast.makeText(this.context, goodsListAdapter.data[position].name, Toast.LENGTH_SHORT).show()
                }
            }
            recyclerView.adapter = goodsListAdapter
        }
    }

    fun setData(data: List<GoodsBean>) {
        goodsListAdapter.setList(data)
    }
}