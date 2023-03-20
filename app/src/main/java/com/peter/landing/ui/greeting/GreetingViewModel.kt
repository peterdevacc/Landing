package com.peter.landing.ui.greeting

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.terms.Terms
import com.peter.landing.data.repository.pref.PreferencesRepository
import com.peter.landing.data.repository.terms.TermsRepository
import com.peter.landing.data.util.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GreetingViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val termsRepository: TermsRepository
): ViewModel() {

    private val greetingUiState: MutableState<GreetingUiState> =
        mutableStateOf(GreetingUiState.Default)
    val uiState: State<GreetingUiState> = greetingUiState

    fun loadTerms(type: Terms.Type) {
        greetingUiState.value = GreetingUiState.Processing
        viewModelScope.launch {
            when (val result = termsRepository.getTerms(type)) {
                is DataResult.Error -> {
                    greetingUiState.value = GreetingUiState.ErrorDialog(
                        code = result.code
                    )
                }
                is DataResult.Success -> {
                    greetingUiState.value = GreetingUiState.TermsDialog(
                        terms = result.data
                    )
                }
            }
        }
    }

    fun acceptedTerms() {
        viewModelScope.launch {
            when (val result = preferencesRepository.acceptAgreement()) {
                is DataResult.Error -> {
                    greetingUiState.value = GreetingUiState.ErrorDialog(
                        code = result.code
                    )
                }
                is DataResult.Success -> {
                    greetingUiState.value = GreetingUiState.Accepted
                }
            }
        }
    }

    fun dismissDialog() {
        greetingUiState.value = GreetingUiState.Default
    }

}