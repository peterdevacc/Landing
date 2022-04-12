package com.peter.landing.viewmodel

import com.peter.landing.domain.study.ReviseUseCase
import com.peter.landing.ui.home.revise.ExerciseViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ExerciseViewModelTest {

    private val useCase = mockk<ReviseUseCase>(relaxed = true)
    private val viewModel = ExerciseViewModel(useCase)

    @Test
    fun revised() {
        viewModel.revised()

        verify(exactly = 1) {
            useCase.revised(any())
        }

        confirmVerified(useCase)
    }

    @Test
    fun getNextWord() {
        viewModel.getNextWord()

        verify(exactly = 1) {
            useCase.nextWord()
            useCase.getCurrentWrongWord()
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
    fun isCorrect() {
        val answer = "answer"

        viewModel.isCorrect(answer)

        verify(exactly = 1) {
            useCase.isCorrect(answer)
        }

        confirmVerified(useCase)
    }

    @Test
    fun setArranged() {
        val arranged = true

        viewModel.setArranged(arranged)

        verify(exactly = 1) {
            useCase.setArranged(arranged)
        }

        confirmVerified(useCase)
    }

    @Test
    fun isArranged() {
        viewModel.isArranged()

        verify(exactly = 1) {
            useCase.isArranged()
        }

        confirmVerified(useCase)
    }

    @Test
    fun initTodayRevise() = runBlocking {
        viewModel.initTodayRevise()

        coVerify(exactly = 1) {
            useCase.initTodayRevise()
            useCase.getCurrentWrongWord()
        }

        confirmVerified(useCase)
    }

}