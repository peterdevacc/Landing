package com.peter.landing.viewmodel

import com.peter.landing.domain.dict.DefinitionUseCase
import com.peter.landing.ui.dict.DefinitionViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DefinitionViewModelTest {

    private val useCase = mockk<DefinitionUseCase>(relaxed = true)
    private val viewModel = DefinitionViewModel(useCase)

    @Test
    fun search() {
        val spelling = "spelling"

        viewModel.search(spelling)

        verify(exactly = 1) {
            useCase.search(spelling, any())
        }

        confirmVerified(useCase)
    }

    @Test
    fun getWord() = runBlocking {
        viewModel.getWord()

        coVerify(exactly = 1) {
            useCase.getWord()
        }

        confirmVerified(useCase)
    }

    @Test
    fun isWordNoted() = runBlocking {
        val wordId: Long = 1
        viewModel.isWordNoted(wordId)

        coVerify(exactly = 1) {
            useCase.isWordNoted(wordId)
        }

        confirmVerified(useCase)
    }

    @Test
    fun addNote() = runBlocking {
        val wordId: Long = 1
        viewModel.addNote(wordId)

        coVerify(exactly = 1) {
            useCase.addNote(wordId, any())
        }

        confirmVerified(useCase)
    }

    @Test
    fun removeNote() = runBlocking {
        val wordId: Long = 1
        viewModel.removeNote(wordId)

        coVerify(exactly = 1) {
            useCase.removeNote(wordId, any())
        }

        confirmVerified(useCase)
    }

}