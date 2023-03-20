package com.peter.landing.data.local.wrong

import androidx.paging.PagingSource
import androidx.room.*
import com.peter.landing.data.local.word.Word
import java.util.*

@Dao
interface WrongDAO {

    @Query("SELECT * FROM wrong WHERE word_id = :id")
    suspend fun getWrongById(id: Long): Wrong?

    @Query(
        """
            SELECT id, spelling, ipa, cn, en, pron_name FROM wrong 
            INNER JOIN word_list ON word_list.id = wrong.word_id
            WHERE study_progress_id = :studyProgressId AND chosen_wrong IS 1
        """
    )
    suspend fun getWrongByStudyProgressIdAndChosenWrong(studyProgressId: Long): List<Word>

    @Query(
        """
            SELECT id, spelling, ipa, cn, en, pron_name FROM wrong 
            INNER JOIN word_list ON word_list.id = wrong.word_id
            WHERE study_progress_id = :studyProgressId AND spelled_wrong IS 1
        """
    )
    suspend fun getWrongByStudyProgressIdAndSpelledWrong(studyProgressId: Long): List<Word>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWrong(wrong: Wrong)
    @Update
    suspend fun updateWrong(wrong: Wrong)

    @Query("DELETE FROM wrong")
    suspend fun deleteWrong()

    @Query(
        """
            SELECT study_progress_id, spelling, ipa, cn, en, pron_name FROM wrong a
            INNER JOIN word_list ON word_list.id = a.word_id
            ORDER BY study_progress_id DESC
        """
    )
    fun getWrongWordPaging(): PagingSource<Int, ProgressWrongWord>

}