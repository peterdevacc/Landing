package com.peter.landing.viewmodel

import com.peter.landing.domain.terms.TermsUseCase
import com.peter.landing.ui.terms.TermsViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TermsViewModelTest {

    private val useCase = mockk<TermsUseCase>(relaxed = true)
    private val viewModel = TermsViewModel(useCase)

    @Test
    fun getTerms() = runBlocking {
        viewModel.getTerms()

        coVerify(exactly = 1) {
            useCase.getTerms()
        }

        confirmVerified(useCase)
    }

    @Test
    fun initTerms() {
        val jsonStr = "jsonStr"

        viewModel.initTerms(jsonStr)

        verify(exactly = 1) {
            useCase.initTerms(jsonStr, any())
        }

        confirmVerified(useCase)
    }

}