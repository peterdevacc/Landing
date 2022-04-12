package com.peter.landing.domain.dict

import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.peter.landing.data.local.history.SearchHistory
import com.peter.landing.data.repository.history.SearchHistoryRepository
import com.peter.landing.data.repository.word.WordRepository
import com.peter.landing.data.util.MAX_SEARCH_HISTORY_SAVE_NUM
import com.peter.landing.util.getDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchUseCase @Inject constructor(
    private val wordRepository: WordRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) {

    private var searchWord: String = ""

    private val inputMutableStateFlow = MutableStateFlow("")
    private val inputFlow: StateFlow<String> = inputMutableStateFlow
    private val suggestionsMutableStateFlow = MutableStateFlow<List<String>>(emptyList())
    val searchSuggestions: StateFlow<List<String>> = suggestionsMutableStateFlow

    fun initSearch(scope: CoroutineScope) = scope.launch {
        inputFlow.collectLatest {
            if (it.isNotEmpty() && it.isNotBlank()) {
                suggestionsMutableStateFlow.emit(wordRepository.getSearchSuggestions(it))
            } else {
                suggestionsMutableStateFlow.emit(emptyList())
            }
        }
    }

    val searchHistoryList: Flow<PagingData<SearchHistoryItem.Data>>
        get() = searchHistoryRepository.getSearchHistoryFlow()
            .map { pagingData ->
                pagingData.map {
                    SearchHistoryItem.Data.ItemSearchHistory(it)
                }
            }.map {
                it.insertSeparators { before, after ->
                    if (before == null) {
                        after?.let {
                            return@insertSeparators SearchHistoryItem.Data.SeparatorSearchDate(
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
                            return@insertSeparators SearchHistoryItem.Data.SeparatorSearchDate(
                                getDate(after.searchHistory.searchDate.time)
                            )
                        }
                    }
                    return@insertSeparators null
                }
            }

    fun setInput(input: String, scope: CoroutineScope) = scope.launch {
        inputMutableStateFlow.emit(input)
    }

    fun getSearchWordSpelling(): String {
        return searchWord
    }

    fun setSearchWordSpelling(word: String) {
        searchWord = word
    }

    fun validSearchWordSpelling(textHint: String): Boolean {
        return searchWord.isNotEmpty() && searchWord.isNotBlank() && searchWord != textHint
    }

    fun saveSearchHistory(scope: CoroutineScope) = scope.launch {
        val total = searchHistoryRepository.getTotalSearchHistoryNum()
        if (total < MAX_SEARCH_HISTORY_SAVE_NUM) {
            searchHistoryRepository.insertSearchHistory(
                SearchHistory(searchWord)
            )
        }
    }

    suspend fun getTotalSearchHistoryNum() =
        searchHistoryRepository.getTotalSearchHistoryNum()

    fun removeSearchHistory(scope: CoroutineScope) = scope.launch {
        searchHistoryRepository.removeSearchHistory()
    }

    private fun isNeedSeparate(dateA: Calendar, dateB: Calendar): Boolean {
        return dateA.get(Calendar.DAY_OF_YEAR) != dateB.get(Calendar.DAY_OF_YEAR)
    }

}