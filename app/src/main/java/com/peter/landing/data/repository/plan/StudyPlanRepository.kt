package com.peter.landing.data.repository.plan

import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.plan.StudyPlanDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudyPlanRepository @Inject constructor(
    private val studyPlanDAO: StudyPlanDAO
) {

    fun getStudyPlanFlow() = studyPlanDAO.getStudyPlanFlow()
        .flowOn(Dispatchers.IO)

    suspend fun getStudyPlan() =
        studyPlanDAO.getStudyPlan()

    suspend fun upsertStudyPlan(studyPlan: StudyPlan) =
        studyPlanDAO.upsertStudyPlan(studyPlan)

    suspend fun removeStudyPlan() =
        studyPlanDAO.deleteStudyPlan()

}