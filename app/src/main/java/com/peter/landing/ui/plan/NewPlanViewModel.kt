package com.peter.landing.ui.plan

import androidx.lifecycle.ViewModel
import com.peter.landing.domain.plan.NewPlanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewPlanViewModel @Inject constructor(
    private val useCase: NewPlanUseCase
) : ViewModel() {

    suspend fun addPlan() =
        useCase.addPlan()

    suspend fun initVocabularyList() =
        useCase.initVocabularyList()

    fun getVocabularyList() =
        useCase.getVocabularyList()

    fun setPlanStartToday(isToday: Boolean) =
        useCase.setPlanStartToday(isToday)

    fun setPlanStartDate() =
        useCase.setPlanStartDate()

    fun setPlanVocabulary(position: Int) =
        useCase.setPlanVocabulary(position)

    fun setPlanWordListSize(wordListSize: Int) =
        useCase.setPlanWordListSize(wordListSize)

    fun isPlanVocabularyExist() =
        useCase.isPlanVocabularyExist()

    fun getPlanVocabularyName() =
        useCase.getPlanVocabularyName()

    fun getPlanWordListSize() =
        useCase.getPlanWordListSize()

    fun getPlanEndDate() =
        useCase.getPlanEndDate()

}
