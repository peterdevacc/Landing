package com.peter.landing.data.local.wrong

import androidx.paging.PagingSource
import androidx.room.*
import com.peter.landing.data.util.MAX_REVISE_TIME
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface WrongDAO {

    @Query("SELECT * FROM wrong WHERE word_id = :id")
    suspend fun getWrongById(id: Long): Wrong?

    @Query("""
        SELECT COALESCE(COUNT(*), 0) AS result FROM wrong 
        WHERE revise_times < $MAX_REVISE_TIME
    """)
    suspend fun countWrongByReviseTimesLessThanThree(): Int

    @Query(
        """
            SELECT COALESCE(COUNT(*), 0) AS result FROM wrong 
            WHERE revise_date = :date AND revise_times < $MAX_REVISE_TIME
        """
    )
    suspend fun countWrongByReviseDateAndReviseTimesLessThanThree(date: Calendar): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWrong(wrong: Wrong)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWrongList(wrongList: List<Wrong>)

    @Update
    suspend fun updateWrong(wrong: Wrong)

    @Update
    suspend fun updateWrongList(wrongList: List<Wrong>)

    @Query("DELETE FROM wrong")
    suspend fun deleteWrong()

    @Query(
        """
        SELECT * FROM wrong a INNER JOIN word_list b 
        ON a.word_id = b.id ORDER BY a.add_date desc"""
    )
    fun getWrongWordPaging(): PagingSource<Int, WrongWord>

    @Query(
        """
        SELECT * FROM wrong a INNER JOIN word_list b 
        ON a.word_id = b.id
        WHERE a.add_date = :addDate AND a.chosen_wrong = 1"""
    )
    fun getWrongWordListByAddDateAndChosenWrongFlow(addDate: Calendar): Flow<List<WrongWord>>

    @Query(
        """
        SELECT * FROM wrong a INNER JOIN word_list b 
        ON a.word_id = b.id
        WHERE a.add_date = :addDate AND a.spelled_wrong = 1"""
    )
    fun getWrongWordListByAddDateAndSpelledWrongFlow(addDate: Calendar): Flow<List<WrongWord>>

    @Query(
        """
        SELECT * FROM wrong a INNER JOIN word_list b 
        ON a.word_id = b.id
        WHERE a.revise_date = :date AND a.revise_times < 4"""
    )
    suspend fun getWrongWordListByReviseDateAndReviseTimeLessThanFour(date: Calendar): List<WrongWord>

}