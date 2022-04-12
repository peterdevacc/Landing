package com.peter.landing.repository

import com.peter.landing.data.local.progress.TypingProgress
import com.peter.landing.data.local.progress.TypingProgressDAO
import com.peter.landing.data.repository.progress.TypingProgressRepository
import com.peter.landing.util.LandingCoroutineScope
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TypingProgressRepositoryTest {

    private val scope = LandingCoroutineScope()
    private val dao = mockk<TypingProgressDAO>(relaxed = true)
    private val repository = TypingProgressRepository(scope, dao)

    @Test
    fun getTypingProgressByDailyProgressId() = runBlocking {
        val id: Long = 1
        repository.getTypingProgressByDailyProgressId(id)

        coVerify(exactly = 1) {
            dao.getTypingProgressByDailyProgressId(id)
        }

        confirmVerified(dao)
    }

    @Test
    fun addTypingProgress() = runBlocking {
        val typingProgress = TypingProgress(1)

        repository.addTypingProgress(typingProgress)

        coVerify(exactly = 1) {
            dao.insertTypingProgress(typingProgress)
        }

        confirmVerified(dao)
    }

    @Test
    fun updateTypingProgress() = runBlocking {
        val typingProgress = TypingProgress(1)

        repository.updateTypingProgress(typingProgress)

        coVerify(exactly = 1) {
            dao.updateTypingProgress(typingProgress)
        }

        confirmVerified(dao)
    }

}