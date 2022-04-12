package com.peter.landing.repository

import com.peter.landing.data.local.progress.DailyProgress
import com.peter.landing.data.local.progress.DailyProgressDAO
import com.peter.landing.data.repository.progress.DailyProgressRepository
import com.peter.landing.util.LandingCoroutineScope
import com.peter.landing.util.getTodayDateTime
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DailyProgressRepositoryTest {

    private val scope = LandingCoroutineScope()
    private val dao = mockk<DailyProgressDAO>(relaxed = true)
    private val repository = DailyProgressRepository(scope, dao)

    private val today = getTodayDateTime()

    @Test
    fun getTodayDailyProgress() = runBlocking {
        repository.getTodayDailyProgress()

        coVerify(exactly = 1) {
            dao.getDailyProgressByCreateDate(today)
        }

        confirmVerified(dao)
    }

    @Test
    fun getLastDailyProgress() = runBlocking {
        repository.getLastDailyProgress()

        coVerify(exactly = 1) {
            dao.getDailyProgressBeforeDateAndLastOne(today)
        }

        confirmVerified(dao)
    }

    @Test
    fun addTodayDailyProgress() = runBlocking {
        val dailyProgress = DailyProgress(1, 1)

        repository.addTodayDailyProgress(dailyProgress)

        coVerify(exactly = 1) {
            dao.insertDailyProgress(dailyProgress)
        }

        confirmVerified(dao)
    }

    @Test
    fun getTotalDailyProgressNum() = runBlocking {
        coEvery {
            dao.getCountInDailyProgress()
        } returns 0

        repository.getTotalDailyProgressNum()

        coVerify(exactly = 1) {
            dao.getCountInDailyProgress()
        }

        confirmVerified(dao)
    }

    @Test
    fun finishTodayDailyProgress() = runBlocking {
        repository.finishTodayDailyProgress()

        coVerify(exactly = 1) {
            dao.updateDailyProgressIsFinishedToTrueByCreateDate(today)
        }

        confirmVerified(dao)
    }

}