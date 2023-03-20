package com.peter.landing.ui.note.pager

import com.peter.landing.data.local.wrong.ProgressWrongWord

sealed interface WrongWordItem {

    data class ItemWrongWord(
        val progressWrongWord: ProgressWrongWord
    ): WrongWordItem

    data class NumHeader(
        val num: Long
    ) : WrongWordItem

}