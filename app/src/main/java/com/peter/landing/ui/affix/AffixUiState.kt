package com.peter.landing.ui.affix

import com.peter.landing.data.local.affix.Affix
import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.data.util.DataResult

sealed interface AffixUiState {

    object Loading : AffixUiState

    data class Success(
        val affixCatalogType: AffixCatalog.Type,
        val affixMap: Map<AffixCatalog, List<Affix>>
    ) : AffixUiState

    data class Error(
        val code: DataResult.Error.Code
    ) : AffixUiState

}