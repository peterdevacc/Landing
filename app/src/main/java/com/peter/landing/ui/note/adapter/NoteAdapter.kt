package com.peter.landing.ui.note.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.peter.landing.domain.note.NoteItem

class NoteAdapter : PagingDataAdapter<NoteItem.Data, NoteViewHolder>(Comparator) {

    private var itemListener: View.OnClickListener? = null
    private var soundListener: View.OnClickListener? = null
    private var callback: SwipeCallback? = null

    fun setItemOnClickListener(listener: View.OnClickListener) {
        this.itemListener = listener
    }

    fun setSoundOnClickListener(listener: View.OnClickListener) {
        this.soundListener = listener
    }

    fun setItemSwipeCallback(callback: SwipeCallback) {
        this.callback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.create(parent, viewType)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
            if (holder is NoteViewHolder.ItemNoteViewHolder) {
                holder.setSwipeCallback(callback)
                holder.setListener(itemListener, soundListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is NoteItem.Data.AlphabetHeader -> NoteItem.Type.AlphabetHeader.ordinal
            is NoteItem.Data.ItemNote -> NoteItem.Type.ItemNote.ordinal
            null -> throw UnsupportedOperationException("Unknown Note Item view")
        }
    }

    companion object {
        private val Comparator = object : DiffUtil.ItemCallback<NoteItem.Data>() {
            override fun areItemsTheSame(
                oldItem: NoteItem.Data, newItem: NoteItem.Data
            ): Boolean {
                val itemNote = oldItem is NoteItem.Data.ItemNote &&
                        newItem is NoteItem.Data.ItemNote &&
                        oldItem.word == newItem.word
                val alphabetHeader = oldItem is NoteItem.Data.AlphabetHeader &&
                        newItem is NoteItem.Data.AlphabetHeader &&
                        oldItem.alphabet == newItem.alphabet
                return itemNote || alphabetHeader
            }

            override fun areContentsTheSame(
                oldItem: NoteItem.Data, newItem: NoteItem.Data
            ): Boolean = oldItem == newItem
        }
    }
}