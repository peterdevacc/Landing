package com.peter.landing.ui.help.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.databinding.ItemHelpBinding
import com.peter.landing.databinding.ItemHelpCatalogBinding
import com.peter.landing.databinding.ItemHelpFooterBinding
import com.peter.landing.domain.help.HelpItem

sealed class HelpViewHolder(view: View) : RecyclerView.ViewHolder(view)
{
    
    open fun bind(helpItemData: HelpItem.Data) { }
    
    class ItemHelpCatalogViewHolder(private val binding: ItemHelpCatalogBinding)
        : HelpViewHolder(binding.root) 
    {

        override fun bind(helpItemData: HelpItem.Data) {
            val data = helpItemData as HelpItem.Data.ItemHelpCatalog
            binding.itemHelpCatalogTitle.text = data.helpCatalog.name
            binding.itemHelpCatalogDescription.text = data.helpCatalog.description
        }

    }

    class ItemHelpViewHolder(val binding: ItemHelpBinding)
        : HelpViewHolder(binding.root) 
    {
        override fun bind(helpItemData: HelpItem.Data) {
            val data = helpItemData as HelpItem.Data.ItemHelp
            binding.itemHelpTitle.text = data.help.title
            if (data.help.content.contains("@")) {
                val lines = data.help.content.split("@")
                val content = lines[0] + "\n" + lines[1]
                binding.itemHelpContent.text = content
            } else {
                binding.itemHelpContent.text = data.help.content
            }
        }

        init {
            itemView.tag = this
        }
    }

    class ItemHelpFooterViewHolder(val binding: ItemHelpFooterBinding)
        : HelpViewHolder(binding.root)

    companion object {
        fun create(parent: ViewGroup, viewType: Int): HelpViewHolder {
            return when(HelpItem.Type.values()[viewType]) {
                HelpItem.Type.ItemHelpCatalog -> {
                    val binding = ItemHelpCatalogBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemHelpCatalogViewHolder(binding)
                }
                HelpItem.Type.ItemHelp -> {
                    val binding = ItemHelpBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemHelpViewHolder(binding)
                }
                HelpItem.Type.Footer -> {
                    val binding = ItemHelpFooterBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemHelpFooterViewHolder(binding)
                }
            }
        }
    }
}