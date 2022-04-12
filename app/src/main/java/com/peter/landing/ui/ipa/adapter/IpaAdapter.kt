package com.peter.landing.ui.ipa.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.domain.ipa.IpaItem

class IpaAdapter : RecyclerView.Adapter<IpaViewHolder>() {

    private var soundListener: View.OnClickListener? = null
    private var ipaList = emptyList<IpaItem>()

    fun setData(ipaList: List<IpaItem>) {
        this.ipaList = ipaList
        notifyDataSetChanged()
    }

    fun getDataItem(position: Int): IpaItem {
        return ipaList[position]
    }

    fun setSoundListener(listener: View.OnClickListener) {
        this.soundListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IpaViewHolder {
        return IpaViewHolder.create(parent, viewType)
    }

    override fun getItemCount(): Int = ipaList.size

    override fun getItemViewType(position: Int): Int {
        return ipaList[position].type.ordinal
    }

    override fun onBindViewHolder(holder: IpaViewHolder, position: Int) {
        holder.bind(ipaList[position].data)
        if (holder is IpaViewHolder.ItemIpaViewHolder) {
            holder.setSoundListener(soundListener)
        }
    }

}