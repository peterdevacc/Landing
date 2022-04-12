package com.peter.landing.repository

import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.local.vocabulary.VocabularyViewDAO
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import com.peter.landing.debug.expectWord
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class VocabularyViewRepositoryTest {

    private val dao = mockk<VocabularyViewDAO>(relaxed = true)
    private val repository = VocabularyViewRepository(dao)

    private val start = 0
    private val end = 20
    
    @Test
    fun getWordListBEGINNER() = runBlocking {
        val beginner = Vocabulary.Name.BEGINNER

        coEvery {
            dao.getWordListFromBeginnerViewByRange(start, end)
        } returns listOf(expectWord)

        repository.getWordList(start, end, beginner)
        repository.getWordList(start, end, beginner)

        coVerify(exactly = 1) {
            dao.getWordListFromBeginnerViewByRange(start, end)
        }

        confirmVerified(dao)
    }

    @Test
    fun getWordListINTERMEDIATE() = runBlocking {
        val beginner = Vocabulary.Name.INTERMEDIATE

        coEvery {
            dao.getWordListFromIntermediateViewByRange(start, end)
        } returns listOf(expectWord)

        repository.getWordList(start, end, beginner)
        repository.getWordList(start, end, beginner)

        coVerify(exactly = 1) {
            dao.getWordListFromIntermediateViewByRange(start, end)
        }

        confirmVerified(dao)
    }
}