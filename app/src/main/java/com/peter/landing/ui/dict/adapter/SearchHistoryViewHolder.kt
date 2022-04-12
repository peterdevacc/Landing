package com.peter.landing.ui.dict.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.databinding.ItemSearchHistoryDateBinding
import com.peter.landing.databinding.ItemSearchHistoryInputBinding
import com.peter.landing.domain.dict.SearchHistoryItem

sealed class SearchHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(searchHistoryItemData: SearchHistoryItem.Data?) {}

    class ItemSearchHistoryViewHolder(private val binding: ItemSearchHistoryInputBinding)
        : SearchHistoryViewHolder(binding.root)
    {
        fun setItemOnClickListener(listener: View.OnClickListener?) {
            itemView.setOnClickListener(listener)
        }

        fun getInput() = binding.searchHistoryInput.text.toString()

        override fun bind(searchHistoryItemData: SearchHistoryItem.Data?) {
            val data = searchHistoryItemData as SearchHistoryItem.Data.ItemSearchHistory
            binding.searchHistoryInput.text = data.searchHistory.input
        }

        init {
            itemView.tag = this
        }
    }

    class SeparatorSearchDateViewHolder(private val binding: ItemSearchHistoryDateBinding)
        : SearchHistoryViewHolder(binding.root)
    {
        override fun bind(searchHistoryItemData: SearchHistoryItem.Data?) {
            val data = searchHistoryItemData as SearchHistoryItem.Data.SeparatorSearchDate
            binding.searchHistorySearchDate.text = data.searchDate
        }
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
            return when (SearchHistoryItem.Type.values()[viewType]) {
                SearchHistoryItem.Type.ItemSearchHistory -> {
                    val binding = ItemSearchHistoryInputBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemSearchHistoryViewHolder(binding)
                }
                SearchHistoryItem.Type.SeparatorSearchDate -> {
                    val binding = ItemSearchHistoryDateBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    SeparatorSearchDateViewHolder(binding)
                }
            }
        }
    }
}