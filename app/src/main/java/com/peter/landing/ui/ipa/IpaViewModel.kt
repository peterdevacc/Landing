package com.peter.landing.ui.ipa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.ipa.Ipa
import com.peter.landing.domain.ipa.IpaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IpaViewModel @Inject constructor(
    private val useCase: IpaUseCase
) : ViewModel() {

    val ipaAndExampleList = useCase
        .ipaList
        .asLiveData()

    fun setIpaType(type: Ipa.Type) =
        useCase.setIpaType(type, viewModelScope)

    init {
        useCase.initIpa(viewModelScope)
    }

}