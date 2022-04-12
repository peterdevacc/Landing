package com.peter.landing.viewmodel

import com.peter.landing.domain.study.TypingUseCase
import com.peter.landing.ui.home.typing.TypingViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TypingViewModelTest {

    private val useCase = mockk<TypingUseCase>(relaxed = true)
    private val viewModel = TypingViewModel(useCase)

    @Test
    fun typing() {
        val input = "input"

        viewModel.typing(input)

        verify(exactly = 1) {
            useCase.typing(input, any())
        }

        confirmVerified(useCase)
    }

    @Test
    fun getNextWord() {
        viewModel.getNextWord()

        verify(exactly = 1) {
            useCase.nextWord()
            useCase.getCurrentWord()
        }

        confirmVerified(useCase)
    }

    @Test
    fun getCurrentNum() {
        viewModel.getCurrentNum()

        verify(exactly = 1) {
            useCase.getCurrentNum()
        }

        confirmVerified(useCase)
    }

    @Test
    fun getTotalNum() {
        viewModel.getTotalNum()

        verify(exactly = 1) {
            useCase.getTotalNum()
        }

        confirmVerified(useCase)
    }

    @Test
    fun isLastWord() {
        viewModel.isLastWord()

        verify(exactly = 1) {
            useCase.isLastWord()
        }

        confirmVerified(useCase)
    }

    @Test
    fun setAnswer() {
        val spelling = "input"

        viewModel.setAnswer(spelling)

        verify(exactly = 1) {
            useCase.setAnswer(spelling)
        }

        confirmVerified(useCase)
    }

    @Test
    fun initTodayTyping() = runBlocking {
        viewModel.initTodayTyping()

        coVerify(exactly = 1) {
            useCase.initTodayTyping()
            useCase.getCurrentWord()
        }

        confirmVerified(useCase)
    }

}