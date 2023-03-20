package com.peter.landing.ui.help

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.help.Help
import com.peter.landing.data.local.help.HelpCatalog
import com.peter.landing.data.repository.help.HelpCatalogRepository
import com.peter.landing.data.repository.help.HelpRepository
import com.peter.landing.data.util.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(
    private val helpCatalogRepository: HelpCatalogRepository,
    private val helpRepository: HelpRepository
) : ViewModel() {

    private val helpUiState: MutableState<HelpUiState> = mutableStateOf(HelpUiState.Loading)
    val uiState: State<HelpUiState> = helpUiState

    init {
        viewModelScope.launch {

            try {
                val helpMap = mutableMapOf<HelpCatalog, List<Help>>()
                val catalogList = helpCatalogRepository.getHelpCatalogList()
                catalogList.forEach { catalog ->
                    val helpList = helpRepository.getHelpListByCatalog(catalog.id)
                    helpMap[catalog] = helpList
                }

                helpUiState.value = HelpUiState
                    .Success(helpMap)
            } catch (exception: Exception) {
                helpUiState.value = HelpUiState
                    .Error(DataResult.Error.Code.UNKNOWN)
            }

        }
    }

}