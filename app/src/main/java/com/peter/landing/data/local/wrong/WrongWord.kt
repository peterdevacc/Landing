package com.peter.landing.data.local.wrong

import androidx.room.Embedded
import com.peter.landing.data.local.word.Word

data class WrongWord(
    @Embedded
    val wrong: Wrong,

    @Embedded
    val word: Word
)