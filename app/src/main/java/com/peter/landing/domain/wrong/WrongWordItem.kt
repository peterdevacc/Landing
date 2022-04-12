package com.peter.landing.domain.wrong

import com.peter.landing.data.local.wrong.WrongWord

data class WrongWordItem(
    val type: Type,
    val data: Data
) {
    enum class Type {
        ItemWrongWord, SeparatorAddDate, AlphabetHeader
    }

    sealed class Data {
        data class ItemWrongWord(
            val wrongWord: WrongWord
        ) : Data()

        data class SeparatorAddDate(
            val addDate: String
        ) : Data()

        data class AlphabetHeader(
            val alphabet: String
        ) : WrongWordItem.Data()
    }
}