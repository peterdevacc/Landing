package com.peter.landing.ui.study.spelling

import com.peter.landing.data.local.word.Word
import com.peter.landing.data.util.DataResult

sealed interface SpellingUiState {

    object Loading : SpellingUiState

    data class Success(
        val current: Int,
        val totalNum: Int,
        val word: Word,
        val input: String = "",
        val submitted: Boolean = false,
        val showWrongList: Boolean = false,
        val wrongList: List<Word> = emptyList(),
        val clickedWrongWord: Word? = null,
        val dialog: Dialog = Dialog.None,
    ) : SpellingUiState {

        enum class Dialog {
            Processing, Dictionary, None
        }

    }

    data class Error(
        val code: DataResult.Error.Code
    ) : SpellingUiState

}