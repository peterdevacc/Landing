package com.peter.landing.repository

import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.local.vocabulary.VocabularyDAO
import com.peter.landing.data.repository.vocabulary.VocabularyRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class VocabularyRepositoryTest {

    private val dao = mockk<VocabularyDAO>(relaxed = true)
    private val repository = VocabularyRepository(dao)
    
    @Test
    fun getVocabularyByName() = runBlocking {
        val name = Vocabulary.Name.BEGINNER

        repository.getVocabularyByName(name)

        coVerify(exactly = 1) {
            dao.getVocabularyByVocabularyName(name)
        }

        confirmVerified(dao)
    }

    @Test
    fun getVocabularyList() = runBlocking {
        coEvery {
            dao.getVocabularyList()
        } returns listOf(Vocabulary())

        repository.getVocabularyList()
        repository.getVocabularyList()

        coVerify(exactly = 1) {
            dao.getVocabularyList()
        }

        confirmVerified(dao)
    }

}