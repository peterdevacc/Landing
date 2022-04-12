package com.peter.landing.viewmodel

import androidx.lifecycle.asLiveData
import com.peter.landing.domain.note.NoteUseCase
import com.peter.landing.ui.note.NoteViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class NoteViewModelTest {

    private val useCase = mockk<NoteUseCase>(relaxed = true)

    @Test
    fun addNote() {
        val wordId: Long = 1
        val viewModel = NoteViewModel(useCase)

        viewModel.addNote(wordId)

        coVerify(exactly = 1) {
            useCase.noteWordList
            useCase.getTotalNoteNum().asLiveData()
            useCase.addNote(wordId, any())
        }

        confirmVerified(useCase)
    }

    @Test
    fun removeNote() {
        val wordId: Long = 1
        val viewModel = NoteViewModel(useCase)

        viewModel.removeNote(wordId)

        coVerify(exactly = 1) {
            useCase.noteWordList
            useCase.getTotalNoteNum().asLiveData()
            useCase.removeNote(wordId, any())
        }

        confirmVerified(useCase)
    }

}