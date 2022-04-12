package com.peter.landing.data.repository.plan

import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.plan.StudyPlanDAO
import com.peter.landing.util.LandingCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudyPlanRepository @Inject constructor(
    private val scope: LandingCoroutineScope,
    private val studyPlanDAO: StudyPlanDAO
) {

    fun getStudyPlanFlow() =
        studyPlanDAO.getStudyPlanFlow()
            .flowOn(Dispatchers.IO)

    suspend fun getStudyPlan() =
        studyPlanDAO.getStudyPlan()

    suspend fun addStudyPlan(studyPlan: StudyPlan) = scope.launch {
        studyPlanDAO.insertStudyPlan(studyPlan)
    }.join()

    suspend fun removeStudyPlan() = scope.launch {
        studyPlanDAO.deleteStudyPlan()
    }.join()

}