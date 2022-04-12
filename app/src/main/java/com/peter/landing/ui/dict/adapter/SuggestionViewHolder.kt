package com.peter.landing.ui.dict.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.databinding.ItemSuggestionBinding

class SuggestionViewHolder(private val binding: ItemSuggestionBinding)
    : RecyclerView.ViewHolder(binding.root)
{
    fun setItemListener(itemListener: View.OnClickListener?) {
        itemView.setOnClickListener(itemListener)
    }

    fun bind(item: String) {
        binding.itemSuggestionWordSpelling.text = item
    }

    init {
        itemView.tag = this
    }
}