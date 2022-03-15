package com.aries.main.ui.mine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.main.R
import com.aries.main.ui.mine.bean.ModuleBean
import com.aries.main.ui.mine.bean.ModuleBeanWrapper
import com.aries.main.ui.mine.holder.ModuleItemHolder
import com.aries.main.ui.mine.holder.ModuleTitleHolder

open class ModuleSelectionAdapter(wrapper: ModuleBeanWrapper): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val titleItem = 0
    private val galleryItem = 1
    private var list: MutableList<ModuleBean> = arrayListOf()
    private lateinit var onItemClickListener: OnItemClickListener

    init {
        convert(wrapper)
    }

    private fun convert(wrapper: ModuleBeanWrapper) {
        for(moduleBean in wrapper.modules) {
            list.add(moduleBean)
            for (bean in wrapper.data) {
                if (bean.moduleName.equals(moduleBean.moduleName)) {
                    list.add(bean)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            titleItem -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_dashboard_module_title, parent, false)
                ModuleTitleHolder(view)
            }
            else -> {
                val view1: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.fragment_dashboard_module_item, parent, false)
                ModuleItemHolder(view1)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var viewType = getItemViewType(position)
        when (viewType) {
            titleItem -> {
                val moduleTitleHolder = holder as ModuleTitleHolder
                val textView: TextView =  moduleTitleHolder.itemView.findViewById(R.id.tv_module_title)
                textView.text = list[position].moduleName
            }
            galleryItem -> {
                val moduleItemHolder = holder as ModuleItemHolder
                val textView1: TextView = moduleItemHolder.itemView.findViewById(R.id.tv_module_name)
                textView1.text = list[position].name
                if (onItemClickListener != null) {
                    moduleItemHolder.itemView.setOnClickListener {
                        onItemClickListener.onClick(holder.itemView, list[position])
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(list[position].showType) {
            titleItem -> titleItem
            galleryItem -> galleryItem
            else -> -1
        }
    }

    open fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onClick(view: View?, bean: ModuleBean?)
    }
}