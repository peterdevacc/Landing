package com.peter.landing.repository

import com.peter.landing.data.local.note.Note
import com.peter.landing.data.local.note.NoteDAO
import com.peter.landing.data.repository.note.NoteRepository
import com.peter.landing.util.LandingCoroutineScope
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class NoteRepositoryTest {

    private val scope = LandingCoroutineScope()
    private val dao = mockk<NoteDAO>(relaxed = true)
    private val repository = NoteRepository(scope, dao)

    @Test
    fun checkNoteExist() = runBlocking {
        val id: Long = 1
        coEvery {
            dao.getNoteByWordId(id)
        } returns null

        val result = repository.checkNoteExist(id)
        assertFalse(result)

        coVerify(exactly = 1) {
            dao.getNoteByWordId(id)
        }

        confirmVerified(dao)
    }

    @Test
    fun addNote() = runBlocking {
        val note = Note(1)

        repository.addNote(note)

        coVerify(exactly = 1) {
            dao.insertNote(note)
        }

        confirmVerified(dao)
    }

    @Test
    fun addNoteList() = runBlocking {
        val note = Note(1)
        val noteList = listOf(note)

        repository.addNoteList(noteList)

        coVerify(exactly = 1) {
            dao.insertNoteList(noteList)
        }

        confirmVerified(dao)
    }

    @Test
    fun removeNote() = runBlocking {
        val id: Long = 1

        repository.removeNote(id)

        coVerify(exactly = 1) {
            dao.deleteNoteByWordId(id)
        }

        confirmVerified(dao)
    }

    @Test
    fun removeNoteList() = runBlocking {
        val note = Note(1)
        val noteList = listOf(note)

        repository.removeNoteList(noteList)

        coVerify(exactly = 1) {
            dao.deleteNoteList(noteList)
        }

        confirmVerified(dao)
    }

    @Test
    fun getNotedWordFlow() = runBlocking {
        repository.getNotedWordFlow().take(1).toList()

        coVerify(exactly = 1) {
            dao.getNotedWordListOrderBySpellingWithNoCasePaging()
        }

        confirmVerified(dao)
    }

}