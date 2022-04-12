package com.peter.landing.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.peter.landing.domain.note.NoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val useCase: NoteUseCase
) : ViewModel() {

    val noteWordList = useCase
        .noteWordList
        .cachedIn(viewModelScope)

    val totalNoteNum = useCase
        .getTotalNoteNum()
        .asLiveData()

    fun addNote(wordId: Long) =
        useCase.addNote(wordId, viewModelScope)

    fun removeNote(wordId: Long) =
        useCase.removeNote(wordId, viewModelScope)

}
