package com.peter.landing.viewmodel

import com.peter.landing.domain.study.StudyUseCase
import com.peter.landing.ui.home.HomeViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HomeViewModelTest {

    private val useCase = mockk<StudyUseCase>(relaxed = true)
    private val viewModel = HomeViewModel(useCase)

    @Test
    fun initStudy() = runBlocking {
        viewModel.initStudy()

        coVerify(exactly = 1) {
            useCase.initStudy()
        }

        confirmVerified(useCase)
    }

    @Test
    fun getProgressState() = runBlocking {
        viewModel.getProgressState()

        coVerify(exactly = 1) {
            useCase.getStudyProgressState()
        }

        confirmVerified(useCase)
    }

    @Test
    fun refreshDate() {
        viewModel.refreshDate()

        verify(exactly = 1) {
            useCase.refreshDate()
        }

        confirmVerified(useCase)
    }

}