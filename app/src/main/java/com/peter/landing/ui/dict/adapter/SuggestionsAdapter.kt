package com.peter.landing.ui.dict.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.databinding.ItemSuggestionBinding

class SuggestionsAdapter : RecyclerView.Adapter<SuggestionViewHolder>() {

    private var suggestions = emptyList<String>()
    private var itemListener: View.OnClickListener? = null

    fun setData(suggestions: List<String>) {
        this.suggestions = suggestions
        notifyDataSetChanged()
    }

    fun getDataItem(position: Int): String {
        return suggestions[position]
    }

    fun setItemOnClickListener(itemListener: View.OnClickListener) {
        this.itemListener = itemListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val binding = ItemSuggestionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SuggestionViewHolder(binding)
    }

    override fun getItemCount(): Int = suggestions.size

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        holder.bind(suggestions[position])
        holder.setItemListener(itemListener)
    }

}