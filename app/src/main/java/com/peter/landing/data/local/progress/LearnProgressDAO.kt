package com.peter.landing.data.local.progress

import androidx.room.*

@Dao
interface LearnProgressDAO {

    @Query("SELECT * FROM learn_progress WHERE daily_progress_id = :dailyProgressId")
    suspend fun getLearnProgressByDailyProgressId(dailyProgressId: Long): LearnProgress

    @Query("SELECT COALESCE(SUM(learned), 0) AS result FROM learn_progress;")
    suspend fun getLearnedSumInLearnProgress(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLearnProgress(learnProgress: LearnProgress)

    @Update
    suspend fun updateLearnProgress(learnProgress: LearnProgress)

}