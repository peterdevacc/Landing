package com.peter.landing.ui.affix.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.data.local.affix.Affix
import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.domain.affix.AffixItem

class AffixAdapter : RecyclerView.Adapter<AffixViewHolder>() {

    private var affixList = emptyList<AffixItem>()

    fun setData(affixData: Map<AffixCatalog, List<Affix>>) {
        val list = mutableListOf<AffixItem>()
        for (affixItem in affixData) {
            if (affixItem.value.first().meaning == " ") {
                list.add(
                    AffixItem(
                        AffixItem.Type.CATALOG_WITHOUT_MEANING,
                        AffixItem.Data.ItemAffixCatalog(affixItem.key)
                    )
                )
                for (affix in affixItem.value) {
                    list.add(
                        AffixItem(
                            AffixItem.Type.ITEM_WITHOUT_MEANING,
                            AffixItem.Data.ItemAffix(affix)
                        )
                    )
                }
            } else {
                list.add(
                    AffixItem(
                        AffixItem.Type.CATALOG,
                        AffixItem.Data.ItemAffixCatalog(affixItem.key)
                    )
                )
                for (affix in affixItem.value) {
                    list.add(
                        AffixItem(
                            AffixItem.Type.ITEM,
                            AffixItem.Data.ItemAffix(affix)
                        )
                    )
                }
            }
        }
        this.affixList = list
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AffixViewHolder {
        return AffixViewHolder.create(parent, viewType)
    }

    override fun onBindViewHolder(holder: AffixViewHolder, position: Int) {
        holder.bind(affixList[position].data)
    }

    override fun getItemCount(): Int {
        return affixList.size
    }

    override fun getItemViewType(position: Int): Int {
        return affixList[position].type.ordinal
    }

}