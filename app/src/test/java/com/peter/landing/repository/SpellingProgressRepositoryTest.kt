package com.peter.landing.repository

import com.peter.landing.data.local.progress.SpellingProgress
import com.peter.landing.data.local.progress.SpellingProgressDAO
import com.peter.landing.data.repository.progress.SpellingProgressRepository
import com.peter.landing.util.LandingCoroutineScope
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SpellingProgressRepositoryTest {

    private val scope = LandingCoroutineScope()
    private val dao = mockk<SpellingProgressDAO>(relaxed = true)
    private val repository = SpellingProgressRepository(scope, dao)

    @Test
    fun getSpellingProgressByDailyProgressId() = runBlocking {
        val id: Long = 1
        repository.getSpellingProgressByDailyProgressId(id)

        coVerify(exactly = 1) {
            dao.getSpellingProgressByDailyProgressId(id)
        }

        confirmVerified(dao)
    }

    @Test
    fun addSpellingProgress() = runBlocking {
        val spellingProgress = SpellingProgress(1)

        repository.addSpellingProgress(spellingProgress)

        coVerify(exactly = 1) {
            dao.insertSpellingProgress(spellingProgress)
        }

        confirmVerified(dao)
    }

    @Test
    fun updateSpellingProgress() = runBlocking {
        val spellingProgress = SpellingProgress(1)

        repository.updateSpellingProgress(spellingProgress)

        coVerify(exactly = 1) {
            dao.updateSpellingProgress(spellingProgress)
        }

        confirmVerified(dao)
    }

    @Test
    fun getTotalSpelled() = runBlocking {
        coEvery {
            dao.getSpelledSumInSpellingProgress()
        } returns 0

        repository.getTotalSpelled()

        coVerify(exactly = 1) {
            dao.getSpelledSumInSpellingProgress()
        }

        confirmVerified(dao)
    }

}