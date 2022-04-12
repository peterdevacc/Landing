package com.peter.landing.repository

import com.peter.landing.data.local.history.SearchHistory
import com.peter.landing.data.local.history.SearchHistoryDAO
import com.peter.landing.data.repository.history.SearchHistoryRepository
import com.peter.landing.util.LandingCoroutineScope
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SearchHistoryRepositoryTest {

    private val scope = LandingCoroutineScope()
    private val dao = mockk<SearchHistoryDAO>(relaxed = true)
    private val repository = SearchHistoryRepository(scope, dao)

    @Test
    fun getSearchHistoryFlow() = runBlocking {
        repository.getSearchHistoryFlow().take(1).toList()

        coVerify(exactly = 1) {
            dao.getSearchHistoryListOrderBySearchDatePaging()
        }

        confirmVerified(dao)
    }

    @Test
    fun getTotalSearchHistoryNum() = runBlocking {
        coEvery {
            dao.countSearchHistory()
        } returns 0

        repository.getTotalSearchHistoryNum()

        coVerify(exactly = 1) {
            dao.countSearchHistory()
        }

        confirmVerified(dao)
    }

    @Test
    fun insertSearchHistory() = runBlocking {
        val history = SearchHistory("")

        repository.insertSearchHistory(history)

        coVerify(exactly = 1) {
            dao.insertSearchHistory(history)
        }

        confirmVerified(dao)
    }

    @Test
    fun removeSearchHistory() = runBlocking {
        repository.removeSearchHistory()

        coVerify(exactly = 1) {
            dao.deleteSearchHistoryList()
        }

        confirmVerified(dao)
    }

}