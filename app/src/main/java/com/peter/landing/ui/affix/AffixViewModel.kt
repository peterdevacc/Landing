package com.peter.landing.ui.affix

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.domain.affix.AffixUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AffixViewModel @Inject constructor(
    private val useCase: AffixUseCase
): ViewModel() {

    val affixData = useCase
        .affixData
        .asLiveData()

    fun setAffixCatalogType(type: AffixCatalog.Type) =
        useCase.setAffixCatalogType(type, viewModelScope)

    init {
        useCase.initAffix(viewModelScope)
    }

}