package com.peter.landing.ui.ipa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.databinding.ItemIpaBinding
import com.peter.landing.databinding.ItemIpaTypeHeaderBinding
import com.peter.landing.domain.ipa.IpaItem

sealed class IpaViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(ipaItemData: IpaItem.Data) { }

    class ItemIpaViewHolder(private val binding: ItemIpaBinding)
        : IpaViewHolder(binding.root)
    {
        fun setSoundListener(soundListener: View.OnClickListener?) {
            binding.itemIpaExampleWordPronButton.setOnClickListener(soundListener)
        }

        override fun bind(ipaItemData: IpaItem.Data) {
            val data = ipaItemData as IpaItem.Data.ItemIpa
            binding.itemIpaText.text = data.ipa.text
            binding.itemIpaExampleWordIpa.text = data.ipa.exampleWordIpa
            binding.itemIpaExampleWordSpelling.text = data.ipa.exampleWordSpelling
        }

        init {
            binding.itemIpaExampleWordPronButton.tag = this
        }
    }

    class IpaTypeHeaderViewHolder(private val binding: ItemIpaTypeHeaderBinding)
        : IpaViewHolder(binding.root)
    {
        override fun bind(ipaItemData: IpaItem.Data) {
            val data = ipaItemData as IpaItem.Data.IpaTypeHeader
            binding.ipaTypeHeader.text = data.type
        }
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int): IpaViewHolder {
            return when (IpaItem.Type.values()[viewType]) {
                IpaItem.Type.ItemIpa -> {
                    val binding = ItemIpaBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemIpaViewHolder(binding)
                }
                IpaItem.Type.IpaTypeHeader -> {
                    val binding = ItemIpaTypeHeaderBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    IpaTypeHeaderViewHolder(binding)
                }
            }
        }
    }

}