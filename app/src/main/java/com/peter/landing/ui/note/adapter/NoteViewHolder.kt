package com.peter.landing.ui.note.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.data.local.word.Word
import com.peter.landing.databinding.ItemAlphabetHeaderBinding
import com.peter.landing.databinding.ItemNoteBinding
import com.peter.landing.domain.note.NoteItem

sealed class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(noteItemData: NoteItem.Data) { }

    class ItemNoteViewHolder(private val binding: ItemNoteBinding) :
        NoteViewHolder(binding.root), SwipeDeleteCallback.Callback {

        private lateinit var word: Word
        private var callback: SwipeCallback? = null

        fun setListener(itemListener: View.OnClickListener?, soundListener: View.OnClickListener?) {
            itemView.setOnClickListener(itemListener)
            binding.itemNotePronButton.setOnClickListener(soundListener)
        }

        fun setSwipeCallback(callback: SwipeCallback?) {
            this.callback = callback
        }

        override fun bind(noteItemData: NoteItem.Data) {
            val data = noteItemData as NoteItem.Data.ItemNote
            this.word = data.word
            binding.itemNoteSpelling.text = word.spelling
            binding.itemNoteIpa.text = word.ipa
        }

        fun getData() = word

        override fun onSwipe() {
            callback?.onSwipe(word)
        }

        init {
            itemView.tag = this
            binding.itemNotePronButton.tag = this
        }
    }

    class ItemAlphabetHeaderViewHolder(private val binding: ItemAlphabetHeaderBinding)
        : NoteViewHolder(binding.root) {
        override fun bind(noteItemData: NoteItem.Data) {
            val data = noteItemData as NoteItem.Data.AlphabetHeader
            binding.ipaTypeHeader.text = data.alphabet
        }
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int): NoteViewHolder {
            return when(NoteItem.Type.values()[viewType]) {
                NoteItem.Type.ItemNote -> {
                    val binding = ItemNoteBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemNoteViewHolder(binding)
                }
                NoteItem.Type.AlphabetHeader -> {
                    val binding = ItemAlphabetHeaderBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    ItemAlphabetHeaderViewHolder(binding)
                }
            }
        }
    }
}