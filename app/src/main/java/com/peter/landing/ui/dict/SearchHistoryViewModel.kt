package com.peter.landing.ui.dict

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.peter.landing.domain.dict.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class SearchHistoryViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    val searchHistoryList = searchUseCase
        .searchHistoryList
        .cachedIn(viewModelScope)

    suspend fun getTotalSearchHistoryNum() =
        searchUseCase.getTotalSearchHistoryNum()

    fun emptySearchHistory() =
        searchUseCase.removeSearchHistory(viewModelScope)

}