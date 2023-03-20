package com.peter.landing.ui.home

import com.peter.landing.data.util.DataResult

sealed interface HomeUiState {

    object Loading : HomeUiState

    data class Success(
        val studyState: StudyState
    ): HomeUiState

    data class Error(
        val code: DataResult.Error.Code
    ) : HomeUiState

}