package com.peter.landing.domain

import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.vocabulary.VocabularyRepository
import com.peter.landing.domain.plan.NewPlanUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class NewPlanUseCaseTest {
    private val vocabularyRepository = mockk<VocabularyRepository>(relaxed = true)
    private val planRepository = mockk<StudyPlanRepository>(relaxed = true)

    private val useCase = NewPlanUseCase(
        vocabularyRepository,
        planRepository
    )

    @Test
    fun addPlan() = runBlocking {
        useCase.addPlan()

        coVerify(exactly = 1) {
            planRepository.addStudyPlan(StudyPlan())
        }

        confirmVerified(planRepository)
    }

    @Test
    fun initVocabularyList() = runBlocking {
        coEvery {
            vocabularyRepository.getVocabularyList()
        } returns listOf()

        useCase.initVocabularyList()

        coVerify(exactly = 1) {
            vocabularyRepository.getVocabularyList()
        }

        confirmVerified(vocabularyRepository)
    }

}