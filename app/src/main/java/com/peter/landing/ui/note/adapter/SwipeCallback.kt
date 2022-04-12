package com.peter.landing.ui.note.adapter

import com.peter.landing.data.local.word.Word

interface SwipeCallback {
    fun onSwipe(word: Word)
}