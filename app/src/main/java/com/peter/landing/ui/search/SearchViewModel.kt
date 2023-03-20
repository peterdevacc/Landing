package com.peter.landing.ui.search

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.peter.landing.data.local.history.SearchHistory
import com.peter.landing.data.repository.history.SearchHistoryRepository
import com.peter.landing.data.repository.word.WordRepository
import com.peter.landing.data.util.MAX_SEARCH_HISTORY_SAVE_NUM
import com.peter.landing.util.getDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val searchHistoryRepository: SearchHistoryRepository
): ViewModel() {

    private val searchUiState: MutableState<SearchUiState> = mutableStateOf(SearchUiState.Loading)
    val uiState: State<SearchUiState> = searchUiState

    fun search() {
        val state = searchUiState.value
        if (state is SearchUiState.Default) {
            viewModelScope.launch {
                val total = searchHistoryRepository.getTotalSearchHistoryNum()
                if (total <= MAX_SEARCH_HISTORY_SAVE_NUM) {
                    searchHistoryRepository.insertSearchHistory(
                        SearchHistory(input = state.spelling)
                    )
                }
            }
        }
    }

    fun write(alphabet: String) {
        val state = searchUiState.value
        if (state is SearchUiState.Default) {
            viewModelScope.launch {
                var updated = state.spelling
                if (updated == "请输入要搜索的单词") {
                    updated = alphabet
                } else {
                    updated += alphabet
                }

                val suggestionList = wordRepository.getSearchSuggestions(updated)
                searchUiState.value = state.copy(
                    spelling = updated,
                    suggestionList = suggestionList
                )
            }
        }
    }

    fun remove() {
        val state = searchUiState.value
        if (state is SearchUiState.Default) {
            viewModelScope.launch {
                var updated = state.spelling
                var suggestionList = emptyList<String>()

                if (updated == "请输入要搜索的单词") {
                    updated = ""
                } else {
                    updated = updated.dropLast(1)
                    if (updated != "") {
                        suggestionList = wordRepository.getSearchSuggestions(updated)
                    }
                }

                searchUiState.value = state.copy(
                    spelling = updated,
                    suggestionList = suggestionList
                )
            }
        }
    }

    fun setWord(suggestion: String) {
        val state = searchUiState.value
        if (state is SearchUiState.Default) {
            viewModelScope.launch {
                val suggestionList = wordRepository.getSearchSuggestions(suggestion)
                searchUiState.value = state.copy(
                    spelling = suggestion,
                    suggestionList = suggestionList,
                    dialog = SearchUiState.Default.Dialog.None
                )
            }
        }
    }

    fun removeSearchHistory() {
        viewModelScope.launch {
            searchHistoryRepository.removeSearchHistory()
        }
    }

    fun openHistoryDialog() {
        val state = searchUiState.value
        if (state is SearchUiState.Default) {
            searchUiState.value = state.copy(
                dialog = SearchUiState.Default.Dialog.History,
            )
        }
    }

    fun closeDialog() {
        val state = searchUiState.value
        if (state is SearchUiState.Default) {
            searchUiState.value = state.copy(
                dialog = SearchUiState.Default.Dialog.None,
            )
        }
    }

    private fun isNeedSeparate(dateA: Calendar, dateB: Calendar): Boolean {
        return dateA.get(Calendar.DAY_OF_YEAR) != dateB.get(Calendar.DAY_OF_YEAR)
    }

    init {
        viewModelScope.launch {
            val searchHistory: Flow<PagingData<SearchHistoryItem>> = searchHistoryRepository
                .getSearchHistoryFlow().map { pagingData ->
                    pagingData.map {
                        SearchHistoryItem.ItemSearchHistory(it)
                    }
                }.map {
                    it.insertSeparators { before, after ->
                        if (before == null) {
                            after?.let {
                                return@insertSeparators SearchHistoryItem.SeparatorSearchDate(
                                    getDate(after.searchHistory.searchDate.time)
                                )
                            }
                        }
                        if (after != null && before != null) {
                            val isNeedSeparate = isNeedSeparate(
                                before.searchHistory.searchDate,
                                after.searchHistory.searchDate
                            )
                            if (isNeedSeparate) {
                                return@insertSeparators SearchHistoryItem.SeparatorSearchDate(
                                    getDate(after.searchHistory.searchDate.time)
                                )
                            }
                        }
                        return@insertSeparators null
                    }
                }.cachedIn(viewModelScope)

            searchUiState.value = SearchUiState.Default(
                searchHistory = searchHistory
            )
        }
    }

}