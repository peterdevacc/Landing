package com.peter.landing.domain

import com.peter.landing.data.local.terms.Terms
import com.peter.landing.data.repository.terms.TermsRepository
import com.peter.landing.domain.terms.TermsUseCase
import com.peter.landing.util.LandingCoroutineScope
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TermsUseCaseTest {

    private val termsRepository = mockk<TermsRepository>(relaxed = true)
    private val useCase = TermsUseCase(termsRepository)
    private val scope = LandingCoroutineScope()

    @Test
    fun `terms no exception`() = runBlocking {
        val jsonStr = ""
        val terms = Terms("", listOf())
        coEvery {
            termsRepository.getTerms(jsonStr)
        } returns terms

        useCase.initTerms(jsonStr, scope)

        val result = useCase.getTerms()
        assertEquals(terms, result)
    }

    @Test
    fun `terms exception`() = runBlocking {
        val jsonStr = ""
        coEvery {
            termsRepository.getTerms(jsonStr)
        } throws Exception()

        useCase.initTerms(jsonStr, scope)

        val result = useCase.getTerms()
        assertNull(result)
    }

}