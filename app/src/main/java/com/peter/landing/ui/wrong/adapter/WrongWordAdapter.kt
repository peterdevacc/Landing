package com.peter.landing.ui.wrong.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.peter.landing.domain.wrong.WrongWordItem

class WrongWordAdapter
    : PagingDataAdapter<WrongWordItem.Data, WrongWordViewHolder>(Comparator)
{
    private var noteListener: View.OnClickListener? = null
    private var soundListener: View.OnClickListener? = null
    private var enListener: View.OnClickListener? = null
    private var cnListener: View.OnClickListener? = null

    fun setItemListener(
        noteListener: View.OnClickListener, soundListener: View.OnClickListener,
        enListener: View.OnClickListener, cnListener: View.OnClickListener
    ) {
        this.noteListener = noteListener
        this.soundListener = soundListener
        this.enListener = enListener
        this.cnListener = cnListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WrongWordViewHolder {
        return WrongWordViewHolder.create(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is WrongWordItem.Data.ItemWrongWord ->
                WrongWordItem.Type.ItemWrongWord.ordinal
            is WrongWordItem.Data.SeparatorAddDate ->
                WrongWordItem.Type.SeparatorAddDate.ordinal
            is WrongWordItem.Data.AlphabetHeader ->
                WrongWordItem.Type.AlphabetHeader.ordinal
            else -> throw UnsupportedOperationException("Unknown WrongAndWord Item view")
        }
    }

    override fun onBindViewHolder(holder: WrongWordViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
            if (it is WrongWordItem.Data.ItemWrongWord) {
                (holder as WrongWordViewHolder.ItemWrongWordViewHolder).apply {
                    setItemListener(noteListener, soundListener, enListener, cnListener)
                }
            }
        }
    }

    companion object {
        private val Comparator = object : DiffUtil.ItemCallback<WrongWordItem.Data>() {
            override fun areItemsTheSame(
                oldItem: WrongWordItem.Data, newItem: WrongWordItem.Data
            ): Boolean {
                val wrongAndWord = oldItem is WrongWordItem.Data.ItemWrongWord &&
                        newItem is WrongWordItem.Data.ItemWrongWord &&
                        oldItem.wrongWord == newItem.wrongWord
                val separator = oldItem is WrongWordItem.Data.SeparatorAddDate &&
                        newItem is WrongWordItem.Data.SeparatorAddDate &&
                        oldItem.addDate == newItem.addDate
                val alphabetHeader = oldItem is WrongWordItem.Data.AlphabetHeader &&
                        newItem is WrongWordItem.Data.AlphabetHeader &&
                        oldItem.alphabet == newItem.alphabet
                return wrongAndWord || separator || alphabetHeader
            }

            override fun areContentsTheSame(
                oldItem: WrongWordItem.Data,
                newItem: WrongWordItem.Data
            ): Boolean {
                val wrongAndWord = oldItem is WrongWordItem.Data.ItemWrongWord &&
                        newItem is WrongWordItem.Data.ItemWrongWord &&
                        oldItem.wrongWord.word.id == newItem.wrongWord.word.id &&
                        oldItem.wrongWord.wrong.wordId == newItem.wrongWord.wrong.wordId &&
                        oldItem.wrongWord.wrong.isNoted == newItem.wrongWord.wrong.isNoted
                val separator = oldItem is WrongWordItem.Data.SeparatorAddDate &&
                        newItem is WrongWordItem.Data.SeparatorAddDate &&
                        oldItem.addDate == newItem.addDate
                val alphabetHeader = oldItem is WrongWordItem.Data.AlphabetHeader &&
                        newItem is WrongWordItem.Data.AlphabetHeader &&
                        oldItem.alphabet == newItem.alphabet
                return wrongAndWord || separator || alphabetHeader
            }
        }
    }

}