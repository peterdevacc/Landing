package com.peter.landing.ui.home.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.wrong.WrongWord
import com.peter.landing.domain.study.StateUseCase
import com.peter.landing.domain.study.StudySection
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StateViewModel @Inject constructor(
    private val useCase: StateUseCase
) : ViewModel() {

    fun getTodayWrongAndWordList(studySection: StudySection) =
        useCase.getTodayWrongWordList(studySection).asLiveData()

    fun addNote(wordId: Long) =
        useCase.addNote(wordId, viewModelScope)

    fun removeNote(wordId: Long) =
        useCase.removeNote(wordId, viewModelScope)

    fun addNoteList(wrongWordList: List<WrongWord>) =
        useCase.addNoteList(wrongWordList, viewModelScope)

    fun removeNoteList(wrongWordList: List<WrongWord>) =
        useCase.removeNoteList(wrongWordList, viewModelScope)

    fun setStudyState(studyState: Boolean) {
        useCase.setStudyState(studyState, viewModelScope)
    }

    suspend fun finishToday() =
        useCase.finishToday()

}
