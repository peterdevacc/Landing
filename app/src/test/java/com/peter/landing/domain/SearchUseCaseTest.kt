package com.peter.landing.domain

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.peter.landing.DumbListCallback
import com.peter.landing.SearchHistoryItemDataDiffCallback
import com.peter.landing.data.local.history.SearchHistory
import com.peter.landing.data.repository.history.SearchHistoryRepository
import com.peter.landing.data.repository.word.WordRepository
import com.peter.landing.domain.dict.SearchHistoryItem
import com.peter.landing.domain.dict.SearchUseCase
import com.peter.landing.util.LandingCoroutineScope
import com.peter.landing.util.getDate
import com.peter.landing.util.getTomorrowDateTime
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class SearchUseCaseTest {

    private val wordRepository = mockk<WordRepository>(relaxed = true)
    private val searchHistoryRepository = mockk<SearchHistoryRepository>(relaxed = true)

    private val useCase = SearchUseCase(
        wordRepository,
        searchHistoryRepository
    )

    private val testDispatcher = StandardTestDispatcher()
    private val scope = LandingCoroutineScope()
    private val magicDelayNumber = 200L

    @Test
    fun `search history - have suggestions`() = runBlocking {
        val input = "input"
        val suggestionList = listOf("input A", "input B")

        coEvery {
            wordRepository.getSearchSuggestions(input)
        } returns suggestionList

        useCase.initSearch(scope)
        useCase.setInput(input, scope)

        delay(magicDelayNumber)

        val result = useCase.searchSuggestions.value
        assertEquals(suggestionList, result)

        coVerify(exactly = 1) {
            wordRepository.getSearchSuggestions(input)
        }

        confirmVerified(wordRepository)
    }

    @Test
    fun `search history - no suggestions`() = runBlocking {
        val input = " "

        useCase.initSearch(scope)
        useCase.setInput(input, scope)

        delay(magicDelayNumber)

        val result = useCase.searchSuggestions.value
        assertEquals(emptyList<String>(), result)

        coVerify(exactly = 0) {
            wordRepository.getSearchSuggestions(input)
        }

        confirmVerified(wordRepository)
    }

    @Test
    fun `search history list`() = runTest {
        val searchHistoryList = listOf(
            SearchHistory("input A"),
            SearchHistory("input B"),
            SearchHistory("input C", getTomorrowDateTime())
        )
        val searchHistoryFlow = flowOf(
            PagingData.from(searchHistoryList)
        )
        coEvery {
            searchHistoryRepository.getSearchHistoryFlow()
        } returns searchHistoryFlow

        val pagingData = useCase.searchHistoryList.take(1).toList().first()
        val differ = AsyncPagingDataDiffer(
            diffCallback = SearchHistoryItemDataDiffCallback(),
            updateCallback = DumbListCallback()
        )

        differ.submitData(pagingData)

        advanceUntilIdle()

        val expect = listOf(
            getDate(searchHistoryList.first().searchDate.time),
            "input A",
            "input B",
            getDate(searchHistoryList.last().searchDate.time),
            "input C"
        )
        val result = differ.snapshot().items.map {
            when (it) {
                is SearchHistoryItem.Data.SeparatorSearchDate -> {
                    it.searchDate
                }
                is SearchHistoryItem.Data.ItemSearchHistory -> {
                    it.searchHistory.input
                }
            }
        }
        assertEquals(expect, result)

        coVerify(exactly = 1) {
            searchHistoryRepository.getSearchHistoryFlow()
        }

        confirmVerified(searchHistoryRepository)
    }

    @Test
    fun `saveSearchHistory not exceed max limit`() = runBlocking {
        coEvery {
            searchHistoryRepository.getTotalSearchHistoryNum()
        } returns 0

        useCase.saveSearchHistory(scope)

        coVerify(exactly = 1) {
            searchHistoryRepository.getTotalSearchHistoryNum()
            searchHistoryRepository.insertSearchHistory(
                any()
            )
        }

        confirmVerified(searchHistoryRepository)
    }

    @Test
    fun `saveSearchHistory exceed max limit`() = runBlocking {
        coEvery {
            searchHistoryRepository.getTotalSearchHistoryNum()
        } returns 500

        useCase.saveSearchHistory(scope)

        coVerify(exactly = 1) {
            searchHistoryRepository.getTotalSearchHistoryNum()
        }
        coVerify(exactly = 0) {
            searchHistoryRepository.insertSearchHistory(
                any()
            )
        }

        confirmVerified(searchHistoryRepository)
    }

    @Test
    fun getTotalSearchHistoryNum() = runBlocking {
        useCase.getTotalSearchHistoryNum()

        coVerify(exactly = 1) {
            searchHistoryRepository.getTotalSearchHistoryNum()
        }

        confirmVerified(searchHistoryRepository)
    }

    @Test
    fun removeSearchHistory() = runBlocking {
        useCase.removeSearchHistory(scope)

        coVerify(exactly = 1) {
            searchHistoryRepository.removeSearchHistory()
        }

        confirmVerified(searchHistoryRepository)
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}