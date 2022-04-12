package com.peter.landing.ui.home.word.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.data.local.word.Word
import com.peter.landing.databinding.ItemWordBinding

class WordListAdapter : RecyclerView.Adapter<WordListViewHolder>() {

    private var wordList = emptyList<Word>()

    fun setData(wordList: List<Word>) {
        this.wordList = wordList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordListViewHolder {
        val binding = ItemWordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WordListViewHolder(binding)
    }

    override fun getItemCount() = wordList.size

    override fun onBindViewHolder(holder: WordListViewHolder, position: Int) {
        holder.bind(wordList[position])
    }

}