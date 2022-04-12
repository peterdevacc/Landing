package com.peter.landing.repository

import com.peter.landing.data.local.word.WordDAO
import com.peter.landing.data.repository.word.WordRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WordRepositoryTest {

    private val dao = mockk<WordDAO>(relaxed = true)
    private val repository = WordRepository(dao)
    
    @Test
    fun searchWord() = runBlocking {
        val name = "word"
        repository.searchWord(name)

        coVerify(exactly = 1) {
            dao.getWordBySpelling(name)
        }

        confirmVerified(dao)
    }

    @Test
    fun getSearchSuggestions() = runBlocking {
        val name = "word"
        repository.getSearchSuggestions(name)

        coVerify(exactly = 1) {
            dao.getWordSuggestionsBySimilarSpelling("$name%")
        }

        confirmVerified(dao)
    }

}