package com.peter.landing.viewmodel

import com.peter.landing.domain.plan.NewPlanUseCase
import com.peter.landing.ui.plan.NewPlanViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class NewPlanViewModelTest {

    private val useCase = mockk<NewPlanUseCase>(relaxed = true)
    private val viewModel = NewPlanViewModel(useCase)

    @Test
    fun addPlan() = runBlocking {
        viewModel.addPlan()

        coVerify(exactly = 1) {
            useCase.addPlan()
        }

        confirmVerified(useCase)
    }

    @Test
    fun initVocabularyList() = runBlocking {
        viewModel.initVocabularyList()

        coVerify(exactly = 1) {
            useCase.initVocabularyList()
        }

        confirmVerified(useCase)
    }

    @Test
    fun getVocabularyList() = runBlocking {
        viewModel.getVocabularyList()

        coVerify(exactly = 1) {
            useCase.getVocabularyList()
        }

        confirmVerified(useCase)
    }

    @Test
    fun setPlanStartToday() = runBlocking {
        val isToday = true

        viewModel.setPlanStartToday(isToday)

        coVerify(exactly = 1) {
            useCase.setPlanStartToday(isToday)
        }

        confirmVerified(useCase)
    }

    @Test
    fun setPlanStartDate() = runBlocking {
        viewModel.setPlanStartDate()

        coVerify(exactly = 1) {
            useCase.setPlanStartDate()
        }

        confirmVerified(useCase)
    }

    @Test
    fun setPlanVocabulary() = runBlocking {
        val position = 1

        viewModel.setPlanVocabulary(position)

        coVerify(exactly = 1) {
            useCase.setPlanVocabulary(position)
        }

        confirmVerified(useCase)
    }

    @Test
    fun setPlanWordListSize() = runBlocking {
        val wordListSize = 1

        viewModel.setPlanWordListSize(wordListSize)

        coVerify(exactly = 1) {
            useCase.setPlanWordListSize(wordListSize)
        }

        confirmVerified(useCase)
    }

    @Test
    fun isPlanVocabularyExist() = runBlocking {
        viewModel.isPlanVocabularyExist()

        coVerify(exactly = 1) {
            useCase.isPlanVocabularyExist()
        }

        confirmVerified(useCase)
    }

    @Test
    fun getPlanVocabularyName() = runBlocking {
        viewModel.getPlanVocabularyName()

        coVerify(exactly = 1) {
            useCase.getPlanVocabularyName()
        }

        confirmVerified(useCase)
    }

    @Test
    fun getPlanWordListSize() = runBlocking {
        viewModel.getPlanWordListSize()

        coVerify(exactly = 1) {
            useCase.getPlanWordListSize()
        }

        confirmVerified(useCase)
    }

    @Test
    fun getPlanEndDate() = runBlocking {
        viewModel.getPlanEndDate()

        coVerify(exactly = 1) {
            useCase.getPlanEndDate()
        }

        confirmVerified(useCase)
    }

}