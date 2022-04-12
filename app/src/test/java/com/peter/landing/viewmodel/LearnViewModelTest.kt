package com.peter.landing.viewmodel

import com.peter.landing.domain.study.LearnUseCase
import com.peter.landing.ui.home.learn.LearnViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LearnViewModelTest {

    private val useCase = mockk<LearnUseCase>(relaxed = true)
    private val viewModel = LearnViewModel(useCase)

    @Test
    fun learn() {
        viewModel.learn()

        verify(exactly = 1) {
            useCase.learn(any())
        }

        confirmVerified(useCase)
    }

    @Test
    fun isCurrentPositionBiggerThanLearned() {
        viewModel.isCurrentPositionBiggerThanLearned()

        verify(exactly = 1) {
            useCase.isCurrentIndexBiggerThanLearned()
        }

        confirmVerified(useCase)
    }

    @Test
    fun getNextWord() {
        viewModel.getNextWord()

        verify(exactly = 1) {
            useCase.getNextWord()
            useCase.getCurrentWord()
        }

        confirmVerified(useCase)
    }

    @Test
    fun getPreviousWord() {
        viewModel.getPreviousWord()

        verify(exactly = 1) {
            useCase.getPreviousWord()
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
    fun initTodayLearn() = runBlocking {
        viewModel.initTodayLearn()

        coVerify(exactly = 1) {
            useCase.initTodayLearn()
            useCase.getCurrentWord()
        }

        confirmVerified(useCase)
    }

}