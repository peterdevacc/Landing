package com.peter.landing.data.repository.history

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.peter.landing.data.local.history.SearchHistory
import com.peter.landing.data.local.history.SearchHistoryDAO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchHistoryRepository @Inject constructor(
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

    suspend fun insertSearchHistory(searchHistory: SearchHistory) =
        searchHistoryDAO.insertSearchHistory(searchHistory)

    suspend fun removeSearchHistory() =
        searchHistoryDAO.deleteSearchHistoryList()

}