package com.peter.landing.ui.ipa

import com.peter.landing.data.local.ipa.Ipa
import com.peter.landing.data.util.DataResult

sealed interface IpaUiState {

    object Loading : IpaUiState

    data class Success(
        val ipaType: Ipa.Type,
        val ipaList: List<Ipa>
    ) : IpaUiState

    data class Error(
        val code: DataResult.Error.Code
    ) : IpaUiState

}