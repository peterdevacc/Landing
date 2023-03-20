package com.peter.landing.ui.study.list

import com.peter.landing.data.local.word.Word
import com.peter.landing.data.util.DataResult

sealed interface WordListUiState {

    object Loading : WordListUiState

    data class Success(
        val wordList: List<Word>,
        val start: Boolean = false
    ) : WordListUiState

    data class Error(
        val code: DataResult.Error.Code
    ) : WordListUiState

}