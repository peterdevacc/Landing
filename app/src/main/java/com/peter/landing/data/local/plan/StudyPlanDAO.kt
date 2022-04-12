package com.peter.landing.data.local.plan

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyPlanDAO {

    @Query("SELECT * FROM study_plan")
    fun getStudyPlanFlow(): Flow<StudyPlan>

    @Query("SELECT * FROM study_plan")
    suspend fun getStudyPlan(): StudyPlan?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyPlan(plan: StudyPlan)

    @Query("DELETE FROM study_plan")
    suspend fun deleteStudyPlan()

}