package com.peter.landing.data.local.progress

import androidx.room.*

@Dao
interface ChoiceProgressDAO {

    @Query("SELECT * FROM choice_progress WHERE daily_progress_id = :dailyProgressId")
    suspend fun getChoiceProgressByDailyProgressId(dailyProgressId: Long): ChoiceProgress

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChoiceProgress(choiceProgress: ChoiceProgress)

    @Update
    suspend fun updateChoiceProgress(choiceProgress: ChoiceProgress)

}