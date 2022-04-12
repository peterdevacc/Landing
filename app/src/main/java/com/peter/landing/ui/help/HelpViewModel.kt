package com.peter.landing.ui.help

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.domain.help.HelpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(
    private val useCase: HelpUseCase
) : ViewModel() {

    suspend fun getHelpItemList() =
        useCase.getHelpItemList()

    init {
        useCase.initHelp(viewModelScope)
    }

}
