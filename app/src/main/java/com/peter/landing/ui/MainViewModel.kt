package com.peter.landing.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.repository.pref.PreferencesRepository
import com.peter.landing.data.util.DataResult
import com.peter.landing.ui.navigation.LandingDestination
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    preferencesRepository: PreferencesRepository
): ViewModel() {

    private val mainUiState: MutableState<MainUiState> = mutableStateOf(MainUiState.Loading)
    val uiState: State<MainUiState> = mainUiState

    init {

        val agreementFlow = preferencesRepository.getAgreementValueFlow()
        val themeFlow = preferencesRepository.getThemeValueFlow()

        viewModelScope.launch {
            agreementFlow.combine(themeFlow) { agreement, theme ->
                agreement to theme
            }.collectLatest { (agreementValue, themeValue) ->
                when (agreementValue) {
                    is DataResult.Error -> {
                        mainUiState.value = MainUiState.Error(
                            code = agreementValue.code
                        )
                    }
                    is DataResult.Success -> {
                        when (themeValue) {
                            is DataResult.Error -> {
                                mainUiState.value = MainUiState.Error(
                                    code = themeValue.code
                                )
                            }
                            is DataResult.Success -> {
                                if (agreementValue.data) {
                                    mainUiState.value = MainUiState.Success(
                                        startDestination = LandingDestination.Main.Home.route,
                                        themeMode = themeValue.data
                                    )
                                } else {
                                    mainUiState.value = MainUiState.Success(
                                        startDestination = LandingDestination.General.Greeting.route,
                                        themeMode = themeValue.data
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}