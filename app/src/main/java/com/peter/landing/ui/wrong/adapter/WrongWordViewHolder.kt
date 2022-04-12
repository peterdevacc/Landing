package com.peter.landing.ui.wrong.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.R
import com.peter.landing.data.local.wrong.WrongWord
import com.peter.landing.databinding.ItemAlphabetHeaderBinding
import com.peter.landing.databinding.ItemWrongWordBinding
import com.peter.landing.databinding.ItemWrongWordDateBinding
import com.peter.landing.domain.wrong.WrongWordItem

sealed class WrongWordViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(wrongWordItemData: WrongWordItem.Data?) {}

    class ItemWrongWordViewHolder(private val binding: ItemWrongWordBinding)
        : WrongWordViewHolder(binding.root)
    {
        var wrongAndWord: WrongWord? = null

        fun setItemListener(
            noteListener: View.OnClickListener?, soundListener: View.OnClickListener?,
            enListener: View.OnClickListener?, cnListener: View.OnClickListener?
        ) {
            binding.wrongWordContainer.setOnClickListener(noteListener)
            binding.wrongWordCnZoomButton.setOnClickListener(cnListener)
            binding.wrongWordEnZoomButton.setOnClickListener(enListener)
            binding.wrongWordPronButton.setOnClickListener(soundListener)
        }

        override fun bind(wrongWordItemData: WrongWordItem.Data?) {
            val data = wrongWordItemData as WrongWordItem.Data.ItemWrongWord
            this.wrongAndWord = data.wrongWord
            binding.wrongWordSpelling.text = data.wrongWord.word.spelling
            binding.wrongWordIpa.text = data.wrongWord.word.ipa
            val notedColor = itemView.context.getColor(R.color.color_correct)
            val unNotedColor = itemView.context.getColor(R.color.color_wrong)
            if (data.wrongWord.wrong.isNoted) {
                binding.wrongWordSpelling.setTextColor(notedColor)
            } else {
                binding.wrongWordSpelling.setTextColor(unNotedColor)
            }
        }

        init {
            itemView.tag = this
            binding.wrongWordCnZoomButton.tag = this
            binding.wrongWordEnZoomButton.tag = this
            binding.wrongWordPronButton.tag = this
        }

    }

    class ItemWrongWordDateViewHolder(private val binding: ItemWrongWordDateBinding)
        : WrongWordViewHolder(binding.root)
    {
        override fun bind(wrongWordItemData: WrongWordItem.Data?) {
            val data = wrongWordItemData as WrongWordItem.Data.SeparatorAddDate
            binding.wrongWordDate.text = data.addDate
        }
    }

    class ItemAlphabetHeaderViewHolder(private val binding: ItemAlphabetHeaderBinding)
        : WrongWordViewHolder(binding.root) {
        override fun bind(wrongWordItemData: WrongWordItem.Data?) {
            val data = wrongWordItemData as WrongWordItem.Data.AlphabetHeader
            binding.ipaTypeHeader.text = data.alphabet
        }
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int): WrongWordViewHolder {
            return when (WrongWordItem.Type.values()[viewType]) {
                WrongWordItem.Type.ItemWrongWord -> {
                    val binding = ItemWrongWordBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemWrongWordViewHolder(binding)
                }
                WrongWordItem.Type.SeparatorAddDate -> {
                    val binding = ItemWrongWordDateBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemWrongWordDateViewHolder(binding)
                }
                WrongWordItem.Type.AlphabetHeader -> {
                    val binding = ItemAlphabetHeaderBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemAlphabetHeaderViewHolder(binding)
                }
            }
        }
    }

}