package com.peter.landing.ui.plan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.domain.plan.PlanUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val useCase: PlanUseCase
) : ViewModel() {

    val studyPlan = useCase
        .getStudyPlanFlow()
        .asLiveData()

    suspend fun getPlanEndDate(studyPlan: StudyPlan) =
        useCase.getPlanEndDate(studyPlan)

    fun deletePlan() =
        useCase.deletePlan(viewModelScope)

    fun getPlanVocabularyStrName(studyPlan: StudyPlan) =
        useCase.getPlanVocabularyStrName(studyPlan)

    suspend fun getDailyPercentage(studyPlan: StudyPlan) =
        useCase.getDailyPercentage(studyPlan)

    suspend fun getTotalPercentage(studyPlan: StudyPlan) =
        useCase.getTotalPercentage(studyPlan)

}
