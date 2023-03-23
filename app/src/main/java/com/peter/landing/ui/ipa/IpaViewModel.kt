package com.peter.landing.ui.ipa

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.ipa.Ipa
import com.peter.landing.data.repository.ipa.IpaRepository
import com.peter.landing.data.util.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IpaViewModel @Inject constructor(
    private val ipaRepository: IpaRepository
): ViewModel() {

    private val ipaUiState: MutableState<IpaUiState> = mutableStateOf(IpaUiState.Loading)
    val uiState: State<IpaUiState> = ipaUiState

    fun setIpaType(ipaType: Ipa.Type) {
        viewModelScope.launch {
            val ipaList = ipaRepository.getIpaList().filter { it.type == ipaType }
            ipaUiState.value = IpaUiState
                .Success(ipaType, ipaList)
        }
    }

    init {
        viewModelScope.launch {
            val ipaList = ipaRepository.getIpaList()
                .filter { it.type == Ipa.Type.CONSONANTS }
            ipaUiState.value = IpaUiState
                .Success(Ipa.Type.CONSONANTS, ipaList)
        }
    }

}