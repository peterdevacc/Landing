package com.peter.landing.domain

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.peter.landing.DumbListCallback
import com.peter.landing.WrongWordItemDataDiffCallback
import com.peter.landing.data.local.note.Note
import com.peter.landing.data.repository.note.NoteRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import com.peter.landing.debug.getWrongWordListForTest
import com.peter.landing.domain.wrong.WrongWordItem
import com.peter.landing.domain.wrong.WrongWordUseCase
import com.peter.landing.util.LandingCoroutineScope
import com.peter.landing.util.getDate
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
class WrongWordUseCaseTest {

    private val wrongRepository = mockk<WrongRepository>(relaxed = true)
    private val noteRepository = mockk<NoteRepository>(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()
    private val scope = LandingCoroutineScope()
    private val magicDelayNumber = 200L

    @Test
    fun `wrongWordList today`() = runTest {
        // list size = 2
        val wrongWordList = getWrongWordListForTest()
        val wrongWordFlow = flowOf(
            PagingData.from(wrongWordList)
        )
        coEvery {
            wrongRepository.getWrongWordFlow()
        } returns wrongWordFlow

        val useCase = WrongWordUseCase(wrongRepository, noteRepository)

        val pagingData = useCase.wrongWordList.take(1).toList().first()
        val differ = AsyncPagingDataDiffer(
            diffCallback = WrongWordItemDataDiffCallback(),
            updateCallback = DumbListCallback()
        )

        differ.submitData(pagingData)
        advanceUntilIdle()

        val spellingA = wrongWordList[0].word.spelling
        val spellingB = wrongWordList[1].word.spelling
        val expect = listOf(
            getAlphabet(spellingA), spellingA,
            getAlphabet(spellingB), spellingB
        )
        val result = differ.snapshot().items.map {
            when (it) {
                is WrongWordItem.Data.ItemWrongWord -> {
                    it.wrongWord.word.spelling
                }
                else -> {
                    val alphabet = it as WrongWordItem.Data.AlphabetHeader
                    alphabet.alphabet
                }
            }
        }
        assertEquals(expect, result)

        coVerify(exactly = 1) {
            wrongRepository.getWrongWordFlow()
        }

        confirmVerified(wrongRepository)
    }

    @Test
    fun `wrongWordList all`() = runTest {
        // list size = 2
        val wrongWordList = getWrongWordListForTest()
        val wrongWordFlow = flowOf(
            PagingData.from(wrongWordList)
        )
        coEvery {
            wrongRepository.getWrongWordFlow()
        } returns wrongWordFlow

        val useCase = WrongWordUseCase(wrongRepository, noteRepository)
        useCase.todayOrAllSwitcher(scope)
        delay(magicDelayNumber)

        val pagingData = useCase.wrongWordList.take(1).toList().first()
        val differ = AsyncPagingDataDiffer(
            diffCallback = WrongWordItemDataDiffCallback(),
            updateCallback = DumbListCallback()
        )

        differ.submitData(pagingData)
        advanceUntilIdle()

        val spellingA = wrongWordList[0].word.spelling
        val spellingB = wrongWordList[1].word.spelling
        val expect = listOf(
            getDate(wrongWordList[0].wrong.addDate.time),
            spellingA, spellingB
        )
        val result = differ.snapshot().items.map {
            when (it) {
                is WrongWordItem.Data.ItemWrongWord -> {
                    it.wrongWord.word.spelling
                }
                else -> {
                    val date = it as WrongWordItem.Data.SeparatorAddDate
                    date.addDate
                }
            }
        }
        assertEquals(expect, result)

        coVerify(exactly = 1) {
            wrongRepository.getWrongWordFlow()
        }

        confirmVerified(wrongRepository)
    }

    @Test
    fun addNote() = runBlocking {
        val wordId: Long = 1

        val useCase = WrongWordUseCase(wrongRepository, noteRepository)
        useCase.addNote(wordId, scope)

        coVerify(exactly = 1) {
            wrongRepository.getWrongWordFlow()
            noteRepository.addNote(Note(wordId))
            wrongRepository.markWrongNoted(wordId)
        }

        confirmVerified(noteRepository, wrongRepository)
    }

    @Test
    fun removeNote() = runBlocking {
        val wordId: Long = 1

        val useCase = WrongWordUseCase(wrongRepository, noteRepository)
        useCase.removeNote(wordId, scope)

        coVerify(exactly = 1) {
            wrongRepository.getWrongWordFlow()
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

    private fun getAlphabet(spelling: String): String {
        return spelling.first().lowercase()
    }

}