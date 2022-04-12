package com.peter.landing.ui.dict.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.peter.landing.domain.dict.SearchHistoryItem

class SearchHistoryAdapter
    : PagingDataAdapter<SearchHistoryItem.Data, SearchHistoryViewHolder>(Comparator)
{

    private var itemListener: View.OnClickListener? = null

    fun setItemOnClickListener(listener: View.OnClickListener) {
        this.itemListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        return SearchHistoryViewHolder.create(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SearchHistoryItem.Data.ItemSearchHistory ->
                SearchHistoryItem.Type.ItemSearchHistory.ordinal
            is SearchHistoryItem.Data.SeparatorSearchDate ->
                SearchHistoryItem.Type.SeparatorSearchDate.ordinal
            else -> throw UnsupportedOperationException("Unknown Search History Item view")
        }
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
            if (it is SearchHistoryItem.Data.ItemSearchHistory) {
                (holder as SearchHistoryViewHolder.ItemSearchHistoryViewHolder)
                    .setItemOnClickListener(itemListener)
            }
        }
    }

    companion object {
        private val Comparator = object : DiffUtil.ItemCallback<SearchHistoryItem.Data>() {
            override fun areItemsTheSame(
                oldItem: SearchHistoryItem.Data, newItem: SearchHistoryItem.Data
            ): Boolean {
                val input = oldItem is SearchHistoryItem.Data.ItemSearchHistory &&
                        newItem is SearchHistoryItem.Data.ItemSearchHistory &&
                        oldItem.searchHistory.input == newItem.searchHistory.input
                val separator = oldItem is SearchHistoryItem.Data.SeparatorSearchDate &&
                        newItem is SearchHistoryItem.Data.SeparatorSearchDate &&
                        oldItem.searchDate == newItem.searchDate
                return input || separator
            }

            override fun areContentsTheSame(
                oldItem: SearchHistoryItem.Data, newItem: SearchHistoryItem.Data
            ): Boolean = oldItem == newItem
        }
    }
}