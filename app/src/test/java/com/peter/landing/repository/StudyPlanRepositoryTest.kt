package com.peter.landing.repository

import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.plan.StudyPlanDAO
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.util.LandingCoroutineScope
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StudyPlanRepositoryTest {

    private val scope = LandingCoroutineScope()
    private val dao = mockk<StudyPlanDAO>(relaxed = true)
    private val repository = StudyPlanRepository(scope, dao)

    @Test
    fun getStudyPlanFlow() = runBlocking {
        repository.getStudyPlanFlow().take(1).toList()

        coVerify(exactly = 1) {
            dao.getStudyPlanFlow()
        }

        confirmVerified(dao)
    }

    @Test
    fun getStudyPlan() = runBlocking {
        coEvery {
            dao.getStudyPlan()
        } returns null

        repository.getStudyPlan()

        coVerify(exactly = 1) {
            dao.getStudyPlan()
        }

        confirmVerified(dao)
    }

    @Test
    fun addStudyPlan() = runBlocking {
        val plan = StudyPlan(Vocabulary.Name.BEGINNER, 0, null)

        repository.addStudyPlan(plan)

        coVerify(exactly = 1) {
            dao.insertStudyPlan(plan)
        }

        confirmVerified(dao)
    }

    @Test
    fun removeStudyPlan() = runBlocking {
        repository.removeStudyPlan()

        coVerify(exactly = 1) {
            dao.deleteStudyPlan()
        }

        confirmVerified(dao)
    }

}