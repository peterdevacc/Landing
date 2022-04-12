package com.peter.landing.domain

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.peter.landing.DumbListCallback
import com.peter.landing.NoteItemDataDiffCallback
import com.peter.landing.data.local.note.Note
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.repository.note.NoteRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import com.peter.landing.domain.note.NoteItem
import com.peter.landing.domain.note.NoteUseCase
import com.peter.landing.util.LandingCoroutineScope
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class NoteUseCaseTest {

    private val noteRepository = mockk<NoteRepository>(relaxed = true)
    private val wrongRepository= mockk<WrongRepository>(relaxed = true)

    private val useCase = NoteUseCase(
        noteRepository,
        wrongRepository
    )

    private val testDispatcher = StandardTestDispatcher()
    private val scope = LandingCoroutineScope()

    @Test
    fun noteWordList() = runTest {
        val wordA = Word("aaa", "", "", "", "")
        val wordB = Word("bbb", "", "", "", "")
        val notedWord = listOf(wordA, wordB)
        val notedWordFlow = flowOf(
            PagingData.from(notedWord)
        )
        coEvery {
            noteRepository.getNotedWordFlow()
        } returns notedWordFlow

        val pagingData = useCase.noteWordList.take(1).toList().first()
        val differ = AsyncPagingDataDiffer(
            diffCallback = NoteItemDataDiffCallback(),
            updateCallback = DumbListCallback()
        )
        differ.submitData(pagingData)

        advanceUntilIdle()

        val expect = listOf(
            wordA.spelling.first().lowercase(),
            wordA.spelling,
            wordB.spelling.first().lowercase(),
            wordB.spelling
        )
        val result = differ.snapshot().items.map {
            when (it) {
                is NoteItem.Data.AlphabetHeader -> {
                    it.alphabet
                }
                is NoteItem.Data.ItemNote -> {
                    it.word.spelling
                }
            }
        }
        assertEquals(expect, result)

        coVerify(exactly = 1) {
            noteRepository.getNotedWordFlow()
        }

        confirmVerified(noteRepository)
    }

    @Test
    fun getTotalNoteNum() = runBlocking {
        useCase.getTotalNoteNum()

        coVerify(exactly = 1) {
            noteRepository.getTotalNoteNumFlow()
        }

        confirmVerified(noteRepository)
    }

    @Test
    fun addNote() = runBlocking {
        val wordId: Long = 1

        useCase.addNote(wordId, scope)

        coVerify(exactly = 1) {
            noteRepository.addNote(Note(wordId))
            wrongRepository.markWrongNoted(wordId)
        }

        confirmVerified(noteRepository, wrongRepository)
    }

    @Test
    fun removeNote() = runBlocking {
        val wordId: Long = 1

        useCase.removeNote(wordId, scope)

        coVerify(exactly = 1) {
            noteRepository.removeNote(wordId)
            wrongRepository.unMarkWrongNoted(wordId)
        }

        confirmVerified(noteRepository, wrongRepository)
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

}