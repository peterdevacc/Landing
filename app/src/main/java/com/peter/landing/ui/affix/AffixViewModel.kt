package com.peter.landing.ui.affix

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.affix.Affix
import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.data.repository.affix.AffixCatalogRepository
import com.peter.landing.data.repository.affix.AffixRepository
import com.peter.landing.data.util.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AffixViewModel @Inject constructor(
    private val affixCatalogRepository: AffixCatalogRepository,
    private val affixRepository: AffixRepository
): ViewModel() {

    private val affixUiState: MutableState<AffixUiState> = mutableStateOf(AffixUiState.Loading)
    val uiState: State<AffixUiState> = affixUiState

    fun setAffixCatalogType(affixCatalogType: AffixCatalog.Type) {
        viewModelScope.launch {
            try {
                val affixMap = mutableMapOf<AffixCatalog, List<Affix>>()
                val catalogList = affixCatalogRepository.getAffixCatalogList()
                    .filter { it.type == affixCatalogType }
                catalogList.forEach { catalog ->
                    val affixList = affixRepository.getAffixListByCatalog(catalog.id)
                    affixMap[catalog] = affixList
                }
                affixUiState.value = AffixUiState
                    .Success(affixCatalogType, affixMap)
            } catch (exception: Exception) {
                affixUiState.value = AffixUiState
                    .Error(DataResult.Error.Code.UNKNOWN)
            }
        }

    }

    init {
        viewModelScope.launch {

            try {
                val affixMap = mutableMapOf<AffixCatalog, List<Affix>>()
                val catalogList = affixCatalogRepository.getAffixCatalogList()
                    .filter { it.type == AffixCatalog.Type.PREFIX }
                catalogList.forEach { catalog ->
                    val affixList = affixRepository.getAffixListByCatalog(catalog.id)
                    affixMap[catalog] = affixList
                }
                affixUiState.value = AffixUiState
                    .Success(AffixCatalog.Type.PREFIX, affixMap)
            } catch (exception: Exception) {
                affixUiState.value = AffixUiState
                    .Error(DataResult.Error.Code.UNKNOWN)
            }

        }
    }

}