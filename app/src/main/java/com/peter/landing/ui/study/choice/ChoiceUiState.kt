package com.peter.landing.ui.study.choice

import com.peter.landing.data.local.word.Word
import com.peter.landing.data.util.DataResult

sealed interface ChoiceUiState {

    object Loading: ChoiceUiState

    data class Success(
        val current: Int,
        val totalNum: Int,
        val wordId: Long,
        val spelling: String,
        val pronName: String,
        val ipa: String,
        val optionList: List<Pair<Map<String, List<String>>, Map<String, List<String>>>>,
        val correctIndex: Int,
        val chosenIndex: Int = -2,
        val submitted: Boolean = false,
        val showWrongList: Boolean = false,
        val wrongList: List<Word> = emptyList(),
        val clickedWrongWord: Word? = null,
        val dialog: Dialog = Dialog.None,
    ) : ChoiceUiState {

        enum class Dialog {
            Processing, Dictionary, None
        }

    }

        data class Error(
        val code: DataResult.Error.Code
    ) : ChoiceUiState

}