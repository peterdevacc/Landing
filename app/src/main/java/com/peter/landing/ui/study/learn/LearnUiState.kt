package com.peter.landing.ui.study.learn

import com.peter.landing.data.local.word.Word
import com.peter.landing.data.util.DataResult

sealed interface LearnUiState {

    object Loading: LearnUiState

    data class Success(
        val current: Int = 0,
        val totalNum: Int,
        val word: Word,
        val quiz: String,
        val input: String = "",
        val submitted: Boolean = false,
        val correct: Boolean = false,
        val learned: Boolean = false,
        val finished: Boolean = false,
        val dialog: Dialog = Dialog.None,
    ) : LearnUiState {

        enum class Dialog {
            Processing, None
        }

    }

    data class Error(
        val code: DataResult.Error.Code
    ) : LearnUiState

}