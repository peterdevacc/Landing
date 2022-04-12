package com.peter.landing.ui

import androidx.lifecycle.ViewModel
import com.peter.landing.domain.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainUseCase: MainUseCase
): ViewModel() {

    suspend fun getCurrentAgreementValue() = mainUseCase
        .agreementData.first()

    suspend fun getCurrentTheme() = mainUseCase
        .themeData.first()

}