package com.peter.landing

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.peter.landing.domain.dict.SearchHistoryItem
import com.peter.landing.domain.note.NoteItem
import com.peter.landing.domain.wrong.WrongWordItem

class DumbListCallback : ListUpdateCallback {
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
}

class NoteItemDataDiffCallback : DiffUtil.ItemCallback<NoteItem.Data>() {
    override fun areItemsTheSame(oldItem: NoteItem.Data, newItem: NoteItem.Data): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: NoteItem.Data, newItem: NoteItem.Data): Boolean {
        return oldItem.toString() == newItem.toString()
    }
}

class SearchHistoryItemDataDiffCallback : DiffUtil.ItemCallback<SearchHistoryItem.Data>() {
    override fun areItemsTheSame(
        oldItem: SearchHistoryItem.Data,
        newItem: SearchHistoryItem.Data
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: SearchHistoryItem.Data,
        newItem: SearchHistoryItem.Data
    ): Boolean {
        return oldItem.toString() == newItem.toString()
    }
}

class WrongWordItemDataDiffCallback : DiffUtil.ItemCallback<WrongWordItem.Data>() {
    override fun areItemsTheSame(
        oldItem: WrongWordItem.Data,
        newItem: WrongWordItem.Data
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: WrongWordItem.Data,
        newItem: WrongWordItem.Data
    ): Boolean {
        return oldItem.toString() == newItem.toString()
    }
}
