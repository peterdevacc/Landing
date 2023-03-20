package com.peter.landing.ui.help

import com.peter.landing.data.local.help.Help
import com.peter.landing.data.local.help.HelpCatalog
import com.peter.landing.data.util.DataResult

sealed interface HelpUiState {

    object Loading : HelpUiState

    data class Success(
        val helpMap: Map<HelpCatalog, List<Help>>
    ) : HelpUiState

    data class Error(
        val code: DataResult.Error.Code
    ) : HelpUiState

}