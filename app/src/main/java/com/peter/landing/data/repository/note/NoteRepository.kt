package com.peter.landing.data.repository.note

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.peter.landing.data.local.note.Note
import com.peter.landing.data.local.note.NoteDAO
import com.peter.landing.data.local.word.Word
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val noteDAO: NoteDAO
) {

    suspend fun checkNoteExist(wordId: Long): Boolean {
        val noteWord = noteDAO.getNoteByWordId(wordId)
        return noteWord != null
    }

    suspend fun addNote(note: Note) =
        noteDAO.insertNote(note)

    suspend fun removeNote(wordId: Long) =
        noteDAO.deleteNoteByWordId(wordId)

    fun getNotedWordFlow(): Flow<PagingData<Word>> {
        return Pager(
            PagingConfig(
                pageSize = 30,
                enablePlaceholders = false,
                maxSize = 100
            )
        ) { noteDAO.getNotedWordListOrderBySpellingWithNoCasePaging() }.flow
    }

}