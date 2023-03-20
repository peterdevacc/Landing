package com.peter.landing.ui.greeting

import com.peter.landing.data.local.terms.Terms
import com.peter.landing.data.util.DataResult

sealed interface GreetingUiState {

    object Accepted: GreetingUiState

    data class TermsDialog(
        val terms: Terms
    ): GreetingUiState

    object Default: GreetingUiState

    object Processing: GreetingUiState

    data class ErrorDialog(
        val code: DataResult.Error.Code
    ): GreetingUiState

}