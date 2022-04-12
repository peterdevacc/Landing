package com.peter.landing.viewmodel

import androidx.lifecycle.asLiveData
import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.domain.plan.PlanUseCase
import com.peter.landing.ui.plan.PlanViewModel
import com.peter.landing.util.getTodayDateTime
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PlanViewModelTest {

    private val useCase = mockk<PlanUseCase>(relaxed = true)

    @Test
    fun getPlanEndDate() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        val viewModel = PlanViewModel(useCase)

        viewModel.getPlanEndDate(studyPlan)

        coVerify(exactly = 1) {
            useCase.getStudyPlanFlow().asLiveData()
            useCase.getPlanEndDate(studyPlan)
        }

        confirmVerified(useCase)
    }

    @Test
    fun deletePlan() {
        val viewModel = PlanViewModel(useCase)

        viewModel.deletePlan()

        coVerify(exactly = 1) {
            useCase.getStudyPlanFlow().asLiveData()
            useCase.deletePlan(any())
        }

        confirmVerified(useCase)
    }

    @Test
    fun getPlanVocabularyStrName() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        val viewModel = PlanViewModel(useCase)

        viewModel.getPlanVocabularyStrName(studyPlan)

        coVerify(exactly = 1) {
            useCase.getStudyPlanFlow().asLiveData()
            useCase.getPlanVocabularyStrName(studyPlan)
        }

        confirmVerified(useCase)
    }

    @Test
    fun getDailyPercentage() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        val viewModel = PlanViewModel(useCase)

        viewModel.getDailyPercentage(studyPlan)

        coVerify(exactly = 1) {
            useCase.getStudyPlanFlow().asLiveData()
            useCase.getDailyPercentage(studyPlan)
        }

        confirmVerified(useCase)
    }

    @Test
    fun getTotalPercentage() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        val viewModel = PlanViewModel(useCase)

        viewModel.getTotalPercentage(studyPlan)

        coVerify(exactly = 1) {
            useCase.getStudyPlanFlow().asLiveData()
            useCase.getTotalPercentage(studyPlan)
        }

        confirmVerified(useCase)
    }

}