package com.peter.landing.dao

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.peter.landing.data.local.LandingDatabase
import com.peter.landing.data.local.note.NoteDAO
import com.peter.landing.data.util.DATABASE_NAME
import com.peter.landing.data.util.DATABASE_PATH
import com.peter.landing.debug.expectNote
import com.peter.landing.debug.expectWord
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class NoteDAOTest {

    private lateinit var database: LandingDatabase
    private lateinit var dao: NoteDAO

    @Test
    fun noteCRD() = runBlocking {
        dao.insertNote(expectNote)
        var result = dao.getNoteByWordId(expectNote.wordId)

        assertNotNull(result)
        assertEquals(expectNote.wordId, result!!.wordId)

        dao.deleteNoteByWordId(expectNote.wordId)
        result = dao.getNoteByWordId(expectNote.wordId)
        assertNull(result)
    }

    @Test
    fun countNote() = runBlocking {
        dao.insertNote(expectNote)
        val resultFlow = dao.getCountInNoteFlow()
        resultFlow.take(1).collect { result ->
            assertEquals(1, result)
        }
        dao.deleteNoteByWordId(expectNote.wordId)
    }

    @Test
    fun noteListCD() = runBlocking {
        val expectNoteList = listOf(expectNote)
        dao.insertNoteList(expectNoteList)

        val resultFlowAfterInsert = dao.getCountInNoteFlow()
        resultFlowAfterInsert.take(1).collect { result ->
            assertEquals(1, result)
        }

        dao.deleteNoteList(expectNoteList)
        val resultFlowAfterDelete = dao.getCountInNoteFlow()
        resultFlowAfterDelete.take(1).collect { result ->
            assertEquals(0, result)
        }
    }

    @Test
    fun getNotedWordListOrderBySpellingWithNoCase() = runBlocking {
        dao.insertNote(expectNote)
        val resultPage = dao.getNotedWordListOrderBySpellingWithNoCasePaging().load(
            PagingSource.LoadParams.Refresh(
                null,
                30,
                false
            )
        ) as PagingSource.LoadResult.Page

        val result = resultPage.data
        assertEquals(1, result.size)
        result.forEach {
            assertEquals(expectWord.spelling, it.spelling)
            assertEquals(expectWord.ipa, it.ipa)
            assertEquals(expectWord.cn, it.cn)
            assertEquals(expectWord.en, it.en)
            assertEquals(expectWord.pronName, it.pronName)
        }

        dao.deleteNoteByWordId(expectNote.wordId)
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.databaseBuilder(
            context,
            LandingDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset(DATABASE_PATH).build()
        dao = database.getNoteDAO()
    }

    @After
    fun clean() {
        database.close()
    }

}