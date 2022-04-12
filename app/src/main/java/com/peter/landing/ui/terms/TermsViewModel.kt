package com.peter.landing.ui.terms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.domain.terms.TermsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor(
    private val useCase: TermsUseCase
) : ViewModel() {

    suspend fun getTerms() =
        useCase.getTerms()

    fun initTerms(jsonStr: String) =
        useCase.initTerms(jsonStr, viewModelScope)

}