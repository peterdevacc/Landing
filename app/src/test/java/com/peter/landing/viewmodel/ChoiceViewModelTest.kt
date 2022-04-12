package com.peter.landing.viewmodel

import com.peter.landing.domain.study.ChoiceUseCase
import com.peter.landing.ui.home.choice.ChoiceViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ChoiceViewModelTest {

    private val useCase = mockk<ChoiceUseCase>(relaxed = true)
    private val viewModel = ChoiceViewModel(useCase)

    @Test
    fun choose() {
        val option = "option"

        viewModel.choose(option)

        verify(exactly = 1) {
            useCase.choose(option, any())
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
    fun getAnswer() {
        viewModel.getAnswer()

        verify(exactly = 1) {
            useCase.getAnswer()
        }

        confirmVerified(useCase)
    }

    @Test
    fun getOptions() {
        viewModel.getOptions()

        verify(exactly = 1) {
            useCase.getOptions()
        }

        confirmVerified(useCase)
    }

    @Test
    fun setOptions() {
        viewModel.setOptions()

        verify(exactly = 1) {
            useCase.setOptions()
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
    fun initTodayChoice() = runBlocking {
        viewModel.initTodayChoice()

        coVerify(exactly = 1) {
            useCase.initTodayChoice()
            useCase.getCurrentWord()
        }

        confirmVerified(useCase)
    }

}