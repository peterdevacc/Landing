package com.peter.landing.ui.note.pager

import com.peter.landing.data.local.word.Word

sealed interface NoteItem {
    data class ItemNote(
        val word: Word
    ) : NoteItem

    data class AlphabetHeader(
        val alphabet: String
    ) : NoteItem

}