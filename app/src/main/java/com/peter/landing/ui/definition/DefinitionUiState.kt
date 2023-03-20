package com.peter.landing.ui.definition

import com.peter.landing.data.local.word.Word
import com.peter.landing.data.util.DataResult

sealed interface DefinitionUiState {

    object Loading: DefinitionUiState

    data class WordExisted(
        val word: Word,
        val isNoted: Boolean,
        val dialog: Dialog = Dialog.None
    ): DefinitionUiState {

        enum class Dialog {
            Processing, None
        }

    }

    data class WordNotExisted(
        val spelling: String
    ): DefinitionUiState

    data class Error(
        val code: DataResult.Error.Code
    ) : DefinitionUiState

}