package com.peter.landing.data.local.history

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchHistoryDAO {

    @Query("SELECT * FROM search_history ORDER BY search_date DESC")
    fun getSearchHistoryListOrderBySearchDatePaging(): PagingSource<Int, SearchHistory>

    @Query("SELECT COALESCE(COUNT(*), 0) AS result FROM search_history")
    suspend fun countSearchHistory(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(searchHistory: SearchHistory)

    @Query("DELETE FROM search_history")
    suspend fun deleteSearchHistoryList()

}