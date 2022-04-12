package com.peter.landing.domain.note

import com.peter.landing.data.local.word.Word

data class NoteItem(
    val type: Type,
    val data: Data
) {
    enum class Type {
        ItemNote, AlphabetHeader
    }

    sealed class Data {
        data class ItemNote(
            val word: Word
        ) : Data()

        data class AlphabetHeader(
            val alphabet: String
        ) : Data()
    }
}