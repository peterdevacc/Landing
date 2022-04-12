package com.peter.landing.ui.plan.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.R
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.databinding.ItemVocabularyBinding
import com.peter.landing.ui.util.getDrawableId

class VocabularyViewHolder(private val binding: ItemVocabularyBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun setListener(itemListener: View.OnClickListener?) {
        itemView.setOnClickListener(itemListener)
    }

    fun bind(vocabulary: Vocabulary) {
        itemView.resources.apply {
            binding.itemVocabularyName.text = getString(
                R.string.vocabulary_name, vocabulary.name.cnValue
            )
            binding.itemVocabularySize.text = getString(
                R.string.vocabulary_size, vocabulary.size
            )
            binding.itemVocabularyDescription.text = getString(
                R.string.vocabulary_description, vocabulary.description
            )
        }
        val imgId = getDrawableId(vocabulary.image, itemView.context)
        binding.itemVocabularyImg.setImageDrawable(
            ContextCompat.getDrawable(itemView.context, imgId)
        )
    }

    init {
        itemView.tag = this
    }
}