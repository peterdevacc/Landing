package com.peter.landing.ui.dict

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.peter.landing.domain.dict.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    val inputSuggestions = searchUseCase
        .searchSuggestions
        .asLiveData()

    fun setInput(input: String) =
        searchUseCase.setInput(input, viewModelScope)

    fun getSearchWordSpelling() =
        searchUseCase.getSearchWordSpelling()

    fun setSearchWordSpelling(wordSpelling: String) =
        searchUseCase.setSearchWordSpelling(wordSpelling)

    fun validSearchWordSpelling(textHint: String) =
        searchUseCase.validSearchWordSpelling(textHint)

    fun saveSearchHistory() =
        searchUseCase.saveSearchHistory(viewModelScope)

    init {
        searchUseCase.initSearch(viewModelScope)
    }

}
