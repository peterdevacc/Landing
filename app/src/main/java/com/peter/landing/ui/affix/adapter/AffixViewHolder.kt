package com.peter.landing.ui.affix.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.databinding.ItemAffixBinding
import com.peter.landing.databinding.ItemAffixCatalogBinding
import com.peter.landing.databinding.ItemAffixCatalogWithoutMeaningBinding
import com.peter.landing.databinding.ItemAffixWithoutMeaningBinding
import com.peter.landing.domain.affix.AffixItem

sealed class AffixViewHolder (val view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(affixItemData: AffixItem.Data) {}

    class ItemAffixViewHolder (
        private val binding: ItemAffixBinding
    ) : AffixViewHolder(binding.root) {

        override fun bind(affixItemData: AffixItem.Data) {
            val data = affixItemData as AffixItem.Data.ItemAffix
            binding.itemAffixText.text = data.affix.text
            binding.itemAffixMeaning.text = data.affix.meaning
            binding.itemAffixExample.text = data.affix.example
        }

    }

    class ItemAffixCatalogViewHolder (
        private val binding: ItemAffixCatalogBinding
    ) : AffixViewHolder(binding.root) {

        override fun bind(affixItemData: AffixItem.Data) {
            val data = affixItemData as AffixItem.Data.ItemAffixCatalog
            binding.itemAffixCatalogDescription.text = data.affixCatalog.description
        }

    }

    class ItemAffixWithOutMeaningViewHolder (
        private val binding: ItemAffixWithoutMeaningBinding
    ) : AffixViewHolder(binding.root) {

        override fun bind(affixItemData: AffixItem.Data) {
            val data = affixItemData as AffixItem.Data.ItemAffix
            binding.itemAffixText.text = data.affix.text
            binding.itemAffixExample.text = data.affix.example
        }

    }

    class ItemAffixCatalogWithOutMeaningViewHolder (
        private val binding: ItemAffixCatalogWithoutMeaningBinding
    ) : AffixViewHolder(binding.root) {

        override fun bind(affixItemData: AffixItem.Data) {
            val data = affixItemData as AffixItem.Data.ItemAffixCatalog
            binding.itemAffixCatalogDescription.text = data.affixCatalog.description
        }

    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int)
            : AffixViewHolder {
            return when (AffixItem.Type.values()[viewType]) {
                AffixItem.Type.CATALOG -> {
                    val binding = ItemAffixCatalogBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemAffixCatalogViewHolder(binding)
                }
                AffixItem.Type.ITEM -> {
                    val binding = ItemAffixBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemAffixViewHolder(binding)
                }
                AffixItem.Type.CATALOG_WITHOUT_MEANING -> {
                    val binding = ItemAffixCatalogWithoutMeaningBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemAffixCatalogWithOutMeaningViewHolder(binding)
                }
                AffixItem.Type.ITEM_WITHOUT_MEANING -> {
                    val binding = ItemAffixWithoutMeaningBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemAffixWithOutMeaningViewHolder(binding)
                }
            }
        }
    }
}