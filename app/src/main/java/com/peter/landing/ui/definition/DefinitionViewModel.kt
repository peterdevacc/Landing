package com.peter.landing.ui.definition

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.note.Note
import com.peter.landing.data.repository.note.NoteRepository
import com.peter.landing.data.repository.word.WordRepository
import com.peter.landing.data.util.DataResult
import com.peter.landing.data.util.SPELLING_SAVED_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DefinitionViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val noteRepository: NoteRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val definitionUiState: MutableState<DefinitionUiState> =
        mutableStateOf(DefinitionUiState.Loading)
    val uiState: State<DefinitionUiState> = definitionUiState

    fun addNote(wordId: Long) {
        val state = definitionUiState.value
        if (state is DefinitionUiState.WordExisted) {
            definitionUiState.value = state.copy(
                dialog = DefinitionUiState.WordExisted.Dialog.Processing
            )
            viewModelScope.launch {
                noteRepository.addNote(Note(wordId))
                definitionUiState.value = state.copy(
                    isNoted = true,
                    dialog = DefinitionUiState.WordExisted.Dialog.None
                )
            }
        }
    }

    fun removeNote(wordId: Long) {
        val state = definitionUiState.value
        if (state is DefinitionUiState.WordExisted) {
            definitionUiState.value = state.copy(
                dialog = DefinitionUiState.WordExisted.Dialog.Processing
            )
            viewModelScope.launch {
                noteRepository.removeNote(wordId)
                definitionUiState.value = state.copy(
                    isNoted = false,
                    dialog = DefinitionUiState.WordExisted.Dialog.None
                )
            }
        }

    }

    init {
        viewModelScope.launch {
            try {
                val spelling: String = checkNotNull(
                    savedStateHandle[SPELLING_SAVED_KEY]
                )
                val word = wordRepository.searchWord(spelling)
                if (word != null) {
                    val isNoted = noteRepository.checkNoteExist(word.id)
                    definitionUiState.value = DefinitionUiState.WordExisted(
                        word = word,
                        isNoted = isNoted
                    )
                } else {
                    definitionUiState.value = DefinitionUiState.WordNotExisted(
                        spelling = spelling
                    )
                }
            } catch (exception: Exception) {
                val code = if (exception is IOException) {
                    DataResult.Error.Code.IO
                } else {
                    DataResult.Error.Code.UNKNOWN
                }
                definitionUiState.value = DefinitionUiState.Error(
                    code = code
                )
            }
        }
    }

}