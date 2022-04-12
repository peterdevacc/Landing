package com.peter.landing.viewmodel

import androidx.lifecycle.asLiveData
import com.peter.landing.domain.dict.SearchUseCase
import com.peter.landing.ui.dict.SearchViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SearchViewModelTest {

    private val useCase = mockk<SearchUseCase>(relaxed = true)

    @Test
    fun setInput() {
        val input = "input"

        val viewModel = SearchViewModel(useCase)

        viewModel.setInput(input)

        coVerify(exactly = 1) {
            useCase.searchSuggestions.asLiveData()
            useCase.initSearch(any())
            useCase.setInput(input, any())
        }

        confirmVerified(useCase)
    }

    @Test
    fun validSearchWordSpelling() {
        val textHint = "textHint"

        val viewModel = SearchViewModel(useCase)

        viewModel.validSearchWordSpelling(textHint)

        coVerify(exactly = 1) {
            useCase.searchSuggestions.asLiveData()
            useCase.initSearch(any())
            useCase.validSearchWordSpelling(textHint)
        }

        confirmVerified(useCase)
    }

    @Test
    fun setSearchWordSpelling() {
        val wordSpelling = "wordSpelling"

        val viewModel = SearchViewModel(useCase)

        viewModel.setSearchWordSpelling(wordSpelling)

        coVerify(exactly = 1) {
            useCase.searchSuggestions.asLiveData()
            useCase.initSearch(any())
            useCase.setSearchWordSpelling(wordSpelling)
        }

        confirmVerified(useCase)
    }

    @Test
    fun getSearchWordSpelling() {
        val viewModel = SearchViewModel(useCase)

        viewModel.getSearchWordSpelling()

        coVerify(exactly = 1) {
            useCase.searchSuggestions.asLiveData()
            useCase.initSearch(any())
            useCase.getSearchWordSpelling()
        }

        confirmVerified(useCase)
    }

    @Test
    fun saveSearchHistory() {
        val viewModel = SearchViewModel(useCase)

        viewModel.saveSearchHistory()

        coVerify(exactly = 1) {
            useCase.searchSuggestions.asLiveData()
            useCase.initSearch(any())
            useCase.saveSearchHistory(any())
        }

        confirmVerified(useCase)
    }
}