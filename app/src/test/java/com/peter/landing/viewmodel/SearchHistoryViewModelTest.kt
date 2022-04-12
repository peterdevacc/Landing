package com.peter.landing.viewmodel

import com.peter.landing.domain.dict.SearchUseCase
import com.peter.landing.ui.dict.SearchHistoryViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SearchHistoryViewModelTest {

    private val useCase = mockk<SearchUseCase>(relaxed = true)

    @Test
    fun getTotalSearchHistoryNum() = runBlocking {
        val viewModel = SearchHistoryViewModel(useCase)

        viewModel.getTotalSearchHistoryNum()

        coVerify(exactly = 1) {
            useCase.searchHistoryList
            useCase.getTotalSearchHistoryNum()
        }

        confirmVerified(useCase)
    }

    @Test
    fun emptySearchHistory() = runBlocking {
        val viewModel = SearchHistoryViewModel(useCase)

        viewModel.emptySearchHistory()

        coVerify(exactly = 1) {
            useCase.searchHistoryList
            useCase.removeSearchHistory(any())
        }

        confirmVerified(useCase)
    }

}