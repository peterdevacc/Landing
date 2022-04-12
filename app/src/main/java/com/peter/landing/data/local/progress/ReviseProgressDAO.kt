package com.peter.landing.data.local.progress

import androidx.room.*
import java.util.*

@Dao
interface ReviseProgressDAO {

    @Query("SELECT * FROM revise_progress WHERE create_date = :date")
    suspend fun getReviseProgressByCreateDate(date: Calendar): ReviseProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviseProgress(reviseProgress: ReviseProgress)

    @Update
    suspend fun updateReviseProgress(reviseProgress: ReviseProgress)

    @Query("DELETE FROM revise_progress")
    suspend fun deleteReviseProgress()

}