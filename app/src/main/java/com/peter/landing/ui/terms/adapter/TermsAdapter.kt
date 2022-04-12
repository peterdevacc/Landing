package com.peter.landing.ui.terms.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.domain.terms.TermsItem

class TermsAdapter : RecyclerView.Adapter<TermsViewHolder>() {

    private var termsList = emptyList<TermsItem>()

    fun setData(termsList: List<TermsItem>) {
        this.termsList = termsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermsViewHolder {
        return TermsViewHolder.create(parent, viewType)
    }

    override fun getItemCount() = termsList.size

    override fun getItemViewType(position: Int): Int {
        return when (termsList[position].type) {
            TermsItem.Type.SUBTITLE.cnValue -> TermsItem.Type.SUBTITLE.ordinal
            TermsItem.Type.TEXT.cnValue -> TermsItem.Type.TEXT.ordinal
            else -> TermsItem.Type.SIGNATURE.ordinal
        }
    }

    override fun onBindViewHolder(holder: TermsViewHolder, position: Int) {
        holder.bind(termsList[position])
    }

}