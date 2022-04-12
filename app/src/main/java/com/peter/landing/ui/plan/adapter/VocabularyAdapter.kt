package com.peter.landing.ui.plan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.databinding.ItemVocabularyBinding

class VocabularyAdapter : RecyclerView.Adapter<VocabularyViewHolder>() {

    private var itemListener: View.OnClickListener? = null
    private var vocabularyItemList = emptyList<Vocabulary>()

    fun setItemListener(listener: View.OnClickListener) {
        this.itemListener = listener
    }

    fun setData(vocabularyList: List<Vocabulary>) {
        vocabularyItemList = vocabularyList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabularyViewHolder {
        val binding = ItemVocabularyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VocabularyViewHolder(binding)
    }

    override fun getItemCount() = vocabularyItemList.size

    override fun onBindViewHolder(holder: VocabularyViewHolder, position: Int) {
        holder.bind(vocabularyItemList[position])
        holder.setListener(itemListener)
    }

}