package com.peter.landing.ui.home.word.adapter

import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.data.local.word.Word
import com.peter.landing.databinding.ItemWordBinding

class WordListViewHolder(private val binding: ItemWordBinding)
    : RecyclerView.ViewHolder(binding.root)
{

    fun bind(word: Word) {
        binding.itemWordSpelling.text = word.spelling
    }

}