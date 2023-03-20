package com.peter.landing.ui.note

import androidx.paging.PagingData
import com.peter.landing.data.local.word.Word
import com.peter.landing.ui.note.pager.NoteItem
import com.peter.landing.ui.note.pager.WrongWordItem
import kotlinx.coroutines.flow.Flow

sealed interface NoteUiState {

    object Loading : NoteUiState

    data class Default(
        val noteList: Flow<PagingData<NoteItem>>,
        val dictionaryWord: Word? = null,
        val removedNoteList: List<Pair<String, Long>> = emptyList(),
        val wrongWordList: Flow<PagingData<WrongWordItem>>,
        val wrongWordSpelling: String = "",
        val wrongWordExplain: Map<String, List<String>> = emptyMap(),
        val dialog: Dialog = Dialog.None,
    ) : NoteUiState {

        enum class Dialog {
            Dictionary, Reverse, Explain, None
        }

    }

}