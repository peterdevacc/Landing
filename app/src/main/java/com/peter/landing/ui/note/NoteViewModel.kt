package com.peter.landing.ui.note

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.peter.landing.data.local.note.Note
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.repository.note.NoteRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import com.peter.landing.ui.note.pager.NoteItem
import com.peter.landing.ui.note.pager.WrongWordItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val wrongRepository: WrongRepository
) : ViewModel() {
    private val noteUiState: MutableState<NoteUiState> =
        mutableStateOf(NoteUiState.Loading)
    val uiState: State<NoteUiState> = noteUiState

    fun addNote(wordSpelling: String, wordId: Long) {
        val state = noteUiState.value
        if (state is NoteUiState.Default) {
            viewModelScope.launch {
                noteRepository.addNote(Note(wordId))

                val updatedList = state.removedNoteList.toMutableList()
                updatedList.remove(wordSpelling to wordId)
                noteUiState.value = state.copy(
                    removedNoteList = updatedList
                )
            }
        }
    }

    fun removeNote(wordSpelling: String, wordId: Long) {
        val state = noteUiState.value
        if (state is NoteUiState.Default) {
            viewModelScope.launch {
                noteRepository.removeNote(wordId)

                val updatedList = state.removedNoteList.toMutableList()
                updatedList.add(wordSpelling to wordId)
                noteUiState.value = state.copy(
                    removedNoteList = updatedList
                )
            }
        }
    }

    fun openDictionaryDialog(word: Word) {
        val state = noteUiState.value
        if (state is NoteUiState.Default) {
            noteUiState.value = state.copy(
                dictionaryWord = word,
                dialog = NoteUiState.Default.Dialog.Dictionary,
            )
        }
    }

    fun openReverseDialog() {
        val state = noteUiState.value
        if (state is NoteUiState.Default) {
            noteUiState.value = state.copy(
                dialog = NoteUiState.Default.Dialog.Reverse,
            )
        }
    }

    fun openExplainDialog(spelling: String, explain: Map<String, List<String>>) {
        val state = noteUiState.value
        if (state is NoteUiState.Default) {
            noteUiState.value = state.copy(
                wrongWordSpelling = spelling,
                wrongWordExplain = explain,
                dialog = NoteUiState.Default.Dialog.Explain,
            )
        }
    }

    fun closeDialog() {
        val state = noteUiState.value
        if (state is NoteUiState.Default) {
            noteUiState.value = state.copy(
                dictionaryWord = null,
                wrongWordSpelling = "",
                wrongWordExplain = emptyMap(),
                dialog = NoteUiState.Default.Dialog.None,
            )
        }
    }

    private fun getAlphabet(spelling: String): String {
        return spelling.first().lowercase(Locale.ENGLISH)
    }

    init {
        viewModelScope.launch {
            val noteWordList: Flow<PagingData<NoteItem>> = noteRepository
                .getNotedWordFlow().map { pagingData ->
                    pagingData.map {
                        NoteItem.ItemNote(it)
                    }
                }.map {
                    it.insertSeparators { before, after ->
                        if (before == null) {
                            after?.let {
                                val alphabet = getAlphabet(after.word.spelling)
                                return@insertSeparators NoteItem.AlphabetHeader(alphabet)

                            }
                        }
                        if (after != null && before != null) {
                            val beforeAlphabet = getAlphabet(before.word.spelling)
                            val afterAlphabet = getAlphabet(after.word.spelling)
                            if (beforeAlphabet != afterAlphabet) {
                                return@insertSeparators NoteItem.AlphabetHeader(afterAlphabet)
                            }
                        }
                        return@insertSeparators null
                    }
                }.cachedIn(viewModelScope)

            val wrongWordList: Flow<PagingData<WrongWordItem>> = wrongRepository
                .getWrongWordFlow().map { pagingData ->
                    pagingData.map {
                        WrongWordItem.ItemWrongWord(it)
                    }
                }.map {
                    it.insertSeparators { before, after ->
                        if (before == null) {
                            after?.let {
                                return@insertSeparators WrongWordItem.NumHeader(
                                    after.progressWrongWord.studyProgressId
                                )
                            }
                        }
                        if (after != null && before != null) {
                            val beforeNum = before.progressWrongWord.studyProgressId
                            val afterNum = after.progressWrongWord.studyProgressId
                            if (beforeNum != afterNum) {
                                return@insertSeparators WrongWordItem.NumHeader(
                                    after.progressWrongWord.studyProgressId
                                )
                            }
                        }
                        return@insertSeparators null
                    }
                }.cachedIn(viewModelScope)

            noteUiState.value = NoteUiState.Default(
                noteList = noteWordList,
                wrongWordList = wrongWordList
            )
        }
    }
}