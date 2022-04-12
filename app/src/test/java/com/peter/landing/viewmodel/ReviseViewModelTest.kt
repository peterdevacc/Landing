package com.peter.landing.viewmodel

import com.peter.landing.domain.study.ReviseUseCase
import com.peter.landing.ui.home.revise.ReviseViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ReviseViewModelTest {

    private val useCase = mockk<ReviseUseCase>(relaxed = true)
    private val viewModel = ReviseViewModel(useCase)

    @Test
    fun getTodayReviseWordList() = runBlocking {
        viewModel.getTodayReviseWordList()

        coVerify(exactly = 1) {
            useCase.getTodayReviseWordList()
        }

        confirmVerified(useCase)
    }

    @Test
    fun initTodayRevise() = runBlocking {
        viewModel.initTodayRevise()

        coVerify(exactly = 1) {
            useCase.initTodayRevise()
        }

        confirmVerified(useCase)
    }

}