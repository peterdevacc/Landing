package com.peter.landing.data.local.progress

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface StudyProgressDAO {

    @Query(
        """
            SELECT * FROM study_progress 
            ORDER BY id DESC LIMIT 1
        """
    )
    fun getStudyProgressLatestFlow(): Flow<StudyProgress?>

    @Query(
        """
            SELECT * FROM study_progress 
            ORDER BY id DESC LIMIT 1
        """
    )
    suspend fun getStudyProgressLatest(): StudyProgress?

    @Query(
        """
            SELECT COALESCE(SUM(spelled), 0) AS result
            FROM study_progress
        """
    )
    suspend fun getCountSpelledInStudyProgress(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyProgress(studyProgress: StudyProgress)

    @Update
    suspend fun updateStudyProgress(studyProgress: StudyProgress)

    @Query("DELETE FROM study_progress")
    suspend fun deleteStudyProgress()

}