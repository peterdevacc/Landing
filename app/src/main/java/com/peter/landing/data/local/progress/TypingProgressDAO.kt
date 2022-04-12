package com.peter.landing.data.local.progress

import androidx.room.*

@Dao
interface TypingProgressDAO {

    @Query("SELECT * FROM typing_progress WHERE daily_progress_id = :dailyProgressId")
    suspend fun getTypingProgressByDailyProgressId(dailyProgressId: Long): TypingProgress

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTypingProgress(typingProgress: TypingProgress)

    @Update
    suspend fun updateTypingProgress(typingProgress: TypingProgress)

}