package com.peter.landing.domain

import com.peter.landing.data.local.note.Note
import com.peter.landing.data.local.wrong.WrongWord
import com.peter.landing.data.repository.note.NoteRepository
import com.peter.landing.data.repository.pref.SettingPreferencesRepository
import com.peter.landing.data.repository.progress.DailyProgressRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import com.peter.landing.debug.getWrongWordListForTest
import com.peter.landing.domain.study.StateUseCase
import com.peter.landing.domain.study.StudySection
import com.peter.landing.util.LandingCoroutineScope
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StateUseCaseTest {
    private val dailyProgressRepository = mockk<DailyProgressRepository>(relaxed = true)
    private val wrongRepository = mockk<WrongRepository>(relaxed = true)
    private val noteRepository = mockk<NoteRepository>(relaxed = true)
    private val settingPreferencesRepository = mockk<SettingPreferencesRepository>(relaxed = true)

    private val useCase = StateUseCase(
        dailyProgressRepository,
        wrongRepository,
        noteRepository,
        settingPreferencesRepository
    )

    private val scope = LandingCoroutineScope()

    @Test
    fun `getTodayWrongWordList StudySection-CHOICE`() = runBlocking {
        useCase.getTodayWrongWordList(StudySection.CHOICE)

        coVerify(exactly = 1) {
            wrongRepository.getTodayChosenWrongWordListFlow()
        }

        confirmVerified(wrongRepository)
    }

    @Test
    fun `getTodayWrongWordList StudySection-SPELLING`() = runBlocking {
        useCase.getTodayWrongWordList(StudySection.SPELLING)

        coVerify(exactly = 1) {
            wrongRepository.getTodaySpelledWrongWordListFlow()
        }

        confirmVerified(wrongRepository)
    }

    @Test
    fun `getTodayWrongWordList other StudySection`() = runBlocking {
        val expect = emptyFlow<List<WrongWord>>()
        val result = useCase.getTodayWrongWordList(StudySection.LEARN)
        assertEquals(expect, result)

        coVerify(exactly = 0) {
            wrongRepository.getTodayChosenWrongWordListFlow()
            wrongRepository.getTodaySpelledWrongWordListFlow()
        }

        confirmVerified(wrongRepository)
    }

    @Test
    fun addNote() = runBlocking {
        val wordId: Long = 1
        useCase.addNote(wordId, scope)

        coVerify(exactly = 1) {
            noteRepository.addNote(Note(wordId))
            wrongRepository.markWrongNoted(wordId)
        }

        confirmVerified(noteRepository, wrongRepository)
    }

    @Test
    fun removeNote() = runBlocking {
        val wordId: Long = 1
        useCase.removeNote(wordId, scope)

        coVerify(exactly = 1) {
            noteRepository.removeNote(wordId)
            wrongRepository.unMarkWrongNoted(wordId)
        }

        confirmVerified(noteRepository, wrongRepository)
    }

    @Test
    fun addNoteList() = runBlocking {
        val wrongWordList = getWrongWordListForTest()

        useCase.addNoteList(wrongWordList, scope)

        coVerify(exactly = 1) {
            noteRepository.addNoteList(toNoteList(wrongWordList))
            val wrongList = wrongWordList.map { it.wrong }
            wrongList.forEach { it.isNoted = true }
            wrongRepository.updateWrongList(wrongList)
        }

        confirmVerified(noteRepository, wrongRepository)
    }

    @Test
    fun removeNoteList() = runBlocking {
        val wrongWordList = getWrongWordListForTest()

        useCase.removeNoteList(wrongWordList, scope)

        coVerify(exactly = 1) {
            noteRepository.removeNoteList(toNoteList(wrongWordList))
            val wrongList = wrongWordList.map { it.wrong }
            wrongList.forEach { it.isNoted = false }
            wrongRepository.updateWrongList(wrongList)
        }

        confirmVerified(noteRepository, wrongRepository)
    }

    @Test
    fun finishToday() = runBlocking {
        useCase.finishToday()

        coVerify(exactly = 1) {
            dailyProgressRepository.finishTodayDailyProgress()
        }

        confirmVerified(dailyProgressRepository)
    }

    private fun toNoteList(wrongWordList: List<WrongWord>): List<Note> {
        val noteList = mutableListOf<Note>()
        wrongWordList.forEach {
            noteList.add(Note(it.wrong.wordId))
        }
        return noteList
    }

}