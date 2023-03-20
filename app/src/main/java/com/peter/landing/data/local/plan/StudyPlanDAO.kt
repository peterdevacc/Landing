package com.peter.landing.data.local.plan

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyPlanDAO {

    @Query("SELECT * FROM study_plan")
    fun getStudyPlanFlow(): Flow<StudyPlan?>

    @Query("SELECT * FROM study_plan WHERE id == 1")
    suspend fun getStudyPlan(): StudyPlan?

    @Upsert
    suspend fun upsertStudyPlan(plan: StudyPlan)

    @Query("DELETE FROM study_plan")
    suspend fun deleteStudyPlan()

}