package com.peter.landing.repository

import com.peter.landing.data.local.progress.ReviseProgress
import com.peter.landing.data.local.progress.ReviseProgressDAO
import com.peter.landing.data.repository.progress.ReviseProgressRepository
import com.peter.landing.util.LandingCoroutineScope
import com.peter.landing.util.getTodayDateTime
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ReviseProgressRepositoryTest {

    private val scope = LandingCoroutineScope()
    private val dao = mockk<ReviseProgressDAO>(relaxed = true)
    private val repository = ReviseProgressRepository(scope, dao)

    private val today = getTodayDateTime()

    @Test
    fun getTodayReviseProgress() = runBlocking {
        repository.getTodayReviseProgress()

        coVerify(exactly = 1) {
            dao.getReviseProgressByCreateDate(today)
        }

        confirmVerified(dao)
    }

    @Test
    fun addTodayReviseProgress() = runBlocking {
        val reviseProgress = ReviseProgress(1)

        repository.addTodayReviseProgress(reviseProgress)

        coVerify(exactly = 1) {
            dao.insertReviseProgress(reviseProgress)
        }

        confirmVerified(dao)
    }

    @Test
    fun updateReviseProcess() = runBlocking {
        val reviseProgress = ReviseProgress(1)

        repository.updateReviseProcess(reviseProgress)

        coVerify(exactly = 1) {
            dao.updateReviseProgress(reviseProgress)
        }

        confirmVerified(dao)
    }

    @Test
    fun removeReviseProgress() = runBlocking {
        repository.removeReviseProgress()

        coVerify(exactly = 1) {
            dao.deleteReviseProgress()
        }

        confirmVerified(dao)
    }

}