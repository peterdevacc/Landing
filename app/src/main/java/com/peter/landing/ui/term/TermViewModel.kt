package com.peter.landing.ui.term

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.terms.Terms
import com.peter.landing.data.repository.terms.TermsRepository
import com.peter.landing.data.util.DataResult
import com.peter.landing.data.util.TERMS_TYPE_SAVED_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TermViewModel @Inject constructor(
    private val termsRepository: TermsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val termUiState: MutableState<TermUiState> = mutableStateOf(TermUiState.Loading)
    val uiState: State<TermUiState> = termUiState

    init {
        val termType: String = checkNotNull(savedStateHandle[TERMS_TYPE_SAVED_KEY])
        val type = Terms.Type.valueOf(termType)
        viewModelScope.launch {
            when (val termResult = termsRepository.getTerms(type)) {
                is DataResult.Error -> {
                    termUiState.value = TermUiState
                        .Error(termResult.code)
                }
                is DataResult.Success -> {
                    termUiState.value = TermUiState
                        .Success(termResult.data)
                }
            }
        }
    }

}