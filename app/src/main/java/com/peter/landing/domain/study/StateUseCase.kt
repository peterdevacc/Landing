package com.peter.landing.domain.study

import com.peter.landing.data.local.note.Note
import com.peter.landing.data.local.wrong.WrongWord
import com.peter.landing.data.repository.note.NoteRepository
import com.peter.landing.data.repository.pref.SettingPreferencesRepository
import com.peter.landing.data.repository.progress.DailyProgressRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class StateUseCase @Inject constructor(
    private val dailyProgressRepository: DailyProgressRepository,
    private val wrongRepository: WrongRepository,
    private val noteRepository: NoteRepository,
    private val settingPreferencesRepository: SettingPreferencesRepository
) {

    fun getTodayWrongWordList(studySection: StudySection): Flow<List<WrongWord>> {
        return when (studySection) {
            StudySection.CHOICE -> wrongRepository.getTodayChosenWrongWordListFlow()
            StudySection.SPELLING -> wrongRepository.getTodaySpelledWrongWordListFlow()
            else -> emptyFlow()
        }
    }

    fun addNote(wordId: Long, scope: CoroutineScope) {
        scope.launch {
            noteRepository.addNote(Note(wordId))
            wrongRepository.markWrongNoted(wordId)
        }
    }

    fun removeNote(wordId: Long, scope: CoroutineScope) {
        scope.launch {
            noteRepository.removeNote(wordId)
            wrongRepository.unMarkWrongNoted(wordId)
        }
    }

    fun addNoteList(wrongWordList: List<WrongWord>, scope: CoroutineScope) {
        scope.launch {
            noteRepository.addNoteList(toNoteList(wrongWordList))
            val wrongList = wrongWordList.map { it.wrong }
            wrongList.forEach { it.isNoted = true }
            wrongRepository.updateWrongList(wrongList)
        }
    }

    fun removeNoteList(wrongWordList: List<WrongWord>, scope: CoroutineScope) {
        scope.launch {
            noteRepository.removeNoteList(toNoteList(wrongWordList))
            val wrongList = wrongWordList.map { it.wrong }
            wrongList.forEach { it.isNoted = false }
            wrongRepository.updateWrongList(wrongList)
        }
    }

    suspend fun finishToday() = dailyProgressRepository.finishTodayDailyProgress()

    fun setStudyState(studyState: Boolean, scope: CoroutineScope) = scope.launch {
        settingPreferencesRepository.setStudyState(studyState)
    }

    private fun toNoteList(wrongWordList: List<WrongWord>): List<Note> {
        val noteList = mutableListOf<Note>()
        wrongWordList.forEach {
            noteList.add(Note(it.wrong.wordId))
        }
        return noteList
    }

}