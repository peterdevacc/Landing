package com.peter.landing.ui.help.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.domain.help.HelpItem

class HelpAdapter : RecyclerView.Adapter<HelpViewHolder>() {

    private var helpItemList = emptyList<HelpItem>()

    fun setData(helpItemList: List<HelpItem>) {
        this.helpItemList = helpItemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpViewHolder {
        return HelpViewHolder.create(parent, viewType)
    }

    override fun getItemCount(): Int = helpItemList.size

    override fun getItemViewType(position: Int): Int {
        return helpItemList[position].type.ordinal
    }

    override fun onBindViewHolder(holder: HelpViewHolder, position: Int) {
        holder.bind(helpItemList[position].data)
    }

}