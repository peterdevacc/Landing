package com.peter.landing.data.local.progress

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*

@Dao
interface DailyProgressDAO {

    @Query("SELECT * FROM daily_progress WHERE create_date = :date")
    suspend fun getDailyProgressByCreateDate(date: Calendar): DailyProgress?

    @Query(
        """
        SELECT * FROM daily_progress 
        WHERE create_date < :date 
        ORDER BY create_date DESC LIMIT 1"""
    )
    suspend fun getDailyProgressBeforeDateAndLastOne(date: Calendar): DailyProgress?

    @Query("SELECT COALESCE(COUNT(*), 0) AS result FROM daily_progress")
    suspend fun getCountInDailyProgress(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyProgress(dailyProgress: DailyProgress): Long

    @Query(
        """
        UPDATE daily_progress 
        SET is_finished = 1 
        WHERE create_date = :date"""
    )
    suspend fun updateDailyProgressIsFinishedToTrueByCreateDate(date: Calendar)

}