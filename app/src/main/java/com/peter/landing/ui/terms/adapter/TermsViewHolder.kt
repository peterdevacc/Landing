package com.peter.landing.ui.terms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.databinding.ItemTermsSignatureBinding
import com.peter.landing.databinding.ItemTermsSubTitleBinding
import com.peter.landing.databinding.ItemTermsTextBinding
import com.peter.landing.domain.terms.TermsItem

sealed class TermsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(data: TermsItem) {}

    class ItemSubTitleViewHolder(private val binding: ItemTermsSubTitleBinding)
        : TermsViewHolder(binding.root)
    {
        override fun bind(data: TermsItem) {
            binding.itemTermsSubTitle.text = data.text
        }
    }

    class ItemTextViewHolder(private val binding: ItemTermsTextBinding)
        : TermsViewHolder(binding.root) {
        override fun bind(data: TermsItem) {
            binding.itemTermsText.text = data.text
        }
    }

    class ItemSignatureViewHolder(private val binding: ItemTermsSignatureBinding)
        : TermsViewHolder(binding.root) {
        override fun bind(data: TermsItem) {
            binding.itemTermsSignature.text = data.text
        }
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int)
            : TermsViewHolder {
            return when (TermsItem.Type.values()[viewType]) {
                TermsItem.Type.SUBTITLE -> {
                    val binding = ItemTermsSubTitleBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemSubTitleViewHolder(binding)
                }
                TermsItem.Type.TEXT -> {
                    val binding = ItemTermsTextBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemTextViewHolder(binding)
                }
                TermsItem.Type.SIGNATURE -> {
                    val binding = ItemTermsSignatureBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemSignatureViewHolder(binding)
                }
            }
        }
    }

}