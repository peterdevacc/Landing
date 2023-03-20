package com.peter.landing.ui

import com.peter.landing.data.util.DataResult
import com.peter.landing.data.util.ThemeMode

sealed interface MainUiState {

    data class Success(
        val startDestination: String,
        val themeMode: ThemeMode
    ): MainUiState

    object Loading: MainUiState

    data class Error(
        val code: DataResult.Error.Code
    ): MainUiState

}