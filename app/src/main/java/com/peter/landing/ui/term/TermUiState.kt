package com.peter.landing.ui.term

import com.peter.landing.data.local.terms.Terms
import com.peter.landing.data.util.DataResult

sealed interface TermUiState {

    data class Success(
        val terms: Terms
    ): TermUiState

    object Loading: TermUiState

    data class Error(
        val code: DataResult.Error.Code
    ): TermUiState

}