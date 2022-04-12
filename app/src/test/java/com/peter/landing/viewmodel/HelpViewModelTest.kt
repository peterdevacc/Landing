package com.peter.landing.viewmodel

import com.peter.landing.domain.help.HelpUseCase
import com.peter.landing.ui.help.HelpViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HelpViewModelTest {

    private val useCase = mockk<HelpUseCase>(relaxed = true)

    @Test
    fun getHelpItemList() = runBlocking {
        val viewModel = HelpViewModel(useCase)

        viewModel.getHelpItemList()

        coVerify(exactly = 1) {
            useCase.initHelp(any())
            useCase.getHelpItemList()
        }

        confirmVerified(useCase)
    }

}