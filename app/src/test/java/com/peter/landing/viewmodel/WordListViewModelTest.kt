package com.peter.landing.viewmodel

import com.peter.landing.domain.study.WordListUseCase
import com.peter.landing.ui.home.word.WordListViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WordListViewModelTest {

    private val useCase = mockk<WordListUseCase>(relaxed = true)

    @Test
    fun getWordList() = runBlocking {
        val viewModel = WordListViewModel(useCase)

        viewModel.getWordList()

        coVerify(exactly = 1) {
            useCase.initWordList(any())
            useCase.getWordList()
        }

        confirmVerified(useCase)
    }

}