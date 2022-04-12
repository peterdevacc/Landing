package com.peter.landing.data.repository.note

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.peter.landing.data.local.note.Note
import com.peter.landing.data.local.note.NoteDAO
import com.peter.landing.data.local.word.Word
import com.peter.landing.util.LandingCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(
    private val scope: LandingCoroutineScope,
    private val noteDAO: NoteDAO
) {

    suspend fun checkNoteExist(wordId: Long): Boolean {
        val noteWord = noteDAO.getNoteByWordId(wordId)
        return noteWord != null
    }

    fun getTotalNoteNumFlow() =
        noteDAO.getCountInNoteFlow()
            .flowOn(Dispatchers.IO)

    suspend fun addNote(note: Note) = scope.launch {
        noteDAO.insertNote(note)
    }.join()

    suspend fun addNoteList(notes: List<Note>) = scope.launch {
        noteDAO.insertNoteList(notes)
    }.join()

    suspend fun removeNote(wordId: Long) = scope.launch {
        noteDAO.deleteNoteByWordId(wordId)
    }.join()

    suspend fun removeNoteList(notes: List<Note>) = scope.launch {
        noteDAO.deleteNoteList(notes)
    }.join()

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