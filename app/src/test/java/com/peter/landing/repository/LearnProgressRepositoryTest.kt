package com.peter.landing.repository

import com.peter.landing.data.local.progress.LearnProgress
import com.peter.landing.data.local.progress.LearnProgressDAO
import com.peter.landing.data.repository.progress.LearnProgressRepository
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
class LearnProgressRepositoryTest {

    private val scope = LandingCoroutineScope()
    private val dao = mockk<LearnProgressDAO>(relaxed = true)
    private val repository = LearnProgressRepository(scope, dao)
    
    @Test
    fun getLearnProgressByDailyProgressId() = runBlocking {
        val id: Long = 1
        repository.getLearnProgressByDailyProgressId(id)

        coVerify(exactly = 1) {
            dao.getLearnProgressByDailyProgressId(id)
        }

        confirmVerified(dao)
    }

    @Test
    fun addLearnProgress() = runBlocking {
        val learnProgress = LearnProgress(1)

        repository.addLearnProgress(learnProgress)

        coVerify(exactly = 1) {
            dao.insertLearnProgress(learnProgress)
        }

        confirmVerified(dao)
    }

    @Test
    fun updateLearnProgress() = runBlocking {
        val learnProgress = LearnProgress(1)

        repository.updateLearnProgress(learnProgress)

        coVerify(exactly = 1) {
            dao.updateLearnProgress(learnProgress)
        }

        confirmVerified(dao)
    }

    @Test
    fun getTotalLearned() = runBlocking {
        coEvery {
            dao.getLearnedSumInLearnProgress()
        } returns 0

        repository.getTotalLearned()

        coVerify(exactly = 1) {
            dao.getLearnedSumInLearnProgress()
        }

        confirmVerified(dao)
    }

}