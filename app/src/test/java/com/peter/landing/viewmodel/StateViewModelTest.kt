package com.peter.landing.viewmodel

import androidx.lifecycle.asLiveData
import com.peter.landing.debug.getWrongWordListForTest
import com.peter.landing.domain.study.StateUseCase
import com.peter.landing.domain.study.StudySection
import com.peter.landing.ui.home.state.StateViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StateViewModelTest {

    private val useCase = mockk<StateUseCase>(relaxed = true)
    private val viewModel = StateViewModel(useCase)

    @Test
    fun getTodayWrongAndWordList() {
        val studySection = StudySection.LEARN

        viewModel.getTodayWrongAndWordList(studySection)

        verify(exactly = 1) {
            useCase.getTodayWrongWordList(studySection).asLiveData()
        }

        confirmVerified(useCase)
    }

    @Test
    fun addNote() {
        val wordId: Long = 1

        viewModel.addNote(wordId)

        verify(exactly = 1) {
            useCase.addNote(wordId, any())
        }

        confirmVerified(useCase)
    }

    @Test
    fun removeNote() {
        val wordId: Long = 1

        viewModel.removeNote(wordId)

        verify(exactly = 1) {
            useCase.removeNote(wordId, any())
        }

        confirmVerified(useCase)
    }

    @Test
    fun addNoteList() {
        val wrongWordList = getWrongWordListForTest()

        viewModel.addNoteList(wrongWordList)

        verify(exactly = 1) {
            useCase.addNoteList(wrongWordList, any())
        }

        confirmVerified(useCase)
    }

    @Test
    fun removeNoteList() {
        val wrongWordList = getWrongWordListForTest()

        viewModel.removeNoteList(wrongWordList)

        verify(exactly = 1) {
            useCase.removeNoteList(wrongWordList, any())
        }

        confirmVerified(useCase)
    }

    @Test
    fun finishToday() = runBlocking {
        viewModel.finishToday()

        coVerify(exactly = 1) {
            useCase.finishToday()
        }

        confirmVerified(useCase)
    }

}