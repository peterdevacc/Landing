package com.peter.landing.data.local.progress

import androidx.room.*

@Dao
interface SpellingProgressDAO {

    @Query("SELECT * FROM spelling_progress WHERE daily_progress_id = :dailyProgressId")
    suspend fun getSpellingProgressByDailyProgressId(dailyProgressId: Long): SpellingProgress

    @Query("SELECT COALESCE(SUM(spelled), 0) AS result FROM spelling_progress")
    suspend fun getSpelledSumInSpellingProgress(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpellingProgress(spellingProgress: SpellingProgress)

    @Update
    suspend fun updateSpellingProgress(spellingProgress: SpellingProgress)

}