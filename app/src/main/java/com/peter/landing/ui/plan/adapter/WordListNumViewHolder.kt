package com.peter.landing.ui.plan.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.databinding.ItemWordListNumBinding

class WordListNumViewHolder(private val binding: ItemWordListNumBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var wordListNumEntry = ""
    var wordListNumValue = 0

    fun setListener(itemListener: View.OnClickListener?) {
        itemView.setOnClickListener(itemListener)
    }

    fun bind(data: Pair<String, Int>) {
        wordListNumEntry = data.first
        wordListNumValue = data.second
        binding.itemWordListNum.text = wordListNumEntry
    }

    init {
        itemView.tag = this
    }
}