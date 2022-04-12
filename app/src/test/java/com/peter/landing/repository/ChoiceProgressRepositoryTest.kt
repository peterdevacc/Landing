package com.peter.landing.repository

import com.peter.landing.data.local.progress.ChoiceProgress
import com.peter.landing.data.local.progress.ChoiceProgressDAO
import com.peter.landing.data.repository.progress.ChoiceProgressRepository
import com.peter.landing.util.LandingCoroutineScope
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ChoiceProgressRepositoryTest {

    private val scope = LandingCoroutineScope()
    private val dao = mockk<ChoiceProgressDAO>(relaxed = true)
    private val repository = ChoiceProgressRepository(scope, dao)

    @Test
    fun getChoiceProgressByDailyProgressId() = runBlocking {
        val id: Long = 1
        repository.getChoiceProgressByDailyProgressId(id)

        coVerify(exactly = 1) {
            dao.getChoiceProgressByDailyProgressId(id)
        }

        confirmVerified(dao)
    }

    @Test
    fun addChoiceProgress() = runBlocking {
        val choiceProgress = ChoiceProgress(1)

        repository.addChoiceProgress(choiceProgress)

        coVerify(exactly = 1) {
            dao.insertChoiceProgress(choiceProgress)
        }

        confirmVerified(dao)
    }

    @Test
    fun updateChoiceProgress() = runBlocking {
        val choiceProgress = ChoiceProgress(1)

        repository.updateChoiceProgress(choiceProgress)

        verify(exactly = 1) {
            runBlocking {
                dao.updateChoiceProgress(choiceProgress)
            }
        }

        confirmVerified(dao)
    }

}