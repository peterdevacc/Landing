package com.peter.landing.data.repository.history

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.peter.landing.data.local.history.SearchHistory
import com.peter.landing.data.local.history.SearchHistoryDAO
import com.peter.landing.util.LandingCoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchHistoryRepository @Inject constructor(
    private val scope: LandingCoroutineScope,
    private val searchHistoryDAO: SearchHistoryDAO
) {

    fun getSearchHistoryFlow(): Flow<PagingData<SearchHistory>> {
        return Pager(
            PagingConfig(
                pageSize = 30,
                enablePlaceholders = false,
                maxSize = 100
            )
        ) { searchHistoryDAO.getSearchHistoryListOrderBySearchDatePaging() }.flow
    }

    suspend fun getTotalSearchHistoryNum() =
        searchHistoryDAO.countSearchHistory()

    suspend fun insertSearchHistory(searchHistory: SearchHistory) = scope.launch {
        searchHistoryDAO.insertSearchHistory(searchHistory)
    }.join()

    suspend fun removeSearchHistory() = scope.launch {
        searchHistoryDAO.deleteSearchHistoryList()
    }.join()

}