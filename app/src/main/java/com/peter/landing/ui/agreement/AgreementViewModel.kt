package com.peter.landing.ui.agreement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.domain.agreement.AgreementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AgreementViewModel @Inject constructor(
    private val useCase: AgreementUseCase
): ViewModel() {

    fun acceptAgreement() =
        useCase.acceptAgreement(viewModelScope)

}