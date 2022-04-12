package com.peter.landing.viewmodel

import com.peter.landing.domain.wrong.WrongWordUseCase
import com.peter.landing.ui.wrong.WrongListViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class WrongListViewModelTest {

    private val useCase = mockk<WrongWordUseCase>(relaxed = true)

    @Test
    fun todayOrAllSwitcher() {
        val viewModel = WrongListViewModel(useCase)

        viewModel.todayOrAllSwitcher()

        coVerify(exactly = 1) {
            useCase.wrongWordList
            useCase.isAllCurrentValue

            useCase.todayOrAllSwitcher(any())
        }

        confirmVerified(useCase)
    }

    @Test
    fun addNote() {
        val wordId: Long = 1

        val viewModel = WrongListViewModel(useCase)

        viewModel.addNote(wordId)

        coVerify(exactly = 1) {
            useCase.wrongWordList
            useCase.isAllCurrentValue

            useCase.addNote(wordId, any())
        }

        confirmVerified(useCase)
    }

    @Test
    fun removeNote() {
        val wordId: Long = 1

        val viewModel = WrongListViewModel(useCase)

        viewModel.removeNote(wordId)

        coVerify(exactly = 1) {
            useCase.wrongWordList
            useCase.isAllCurrentValue

            useCase.removeNote(wordId, any())
        }

        confirmVerified(useCase)
    }

}