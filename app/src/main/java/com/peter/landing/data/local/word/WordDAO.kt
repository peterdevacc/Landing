package com.peter.landing.data.local.word

import androidx.room.Dao
import androidx.room.Query
import com.peter.landing.data.util.MAX_SUGGESTIONS_NUM

@Dao
interface WordDAO {

    @Query("SELECT * FROM word_list WHERE spelling = :spelling")
    suspend fun getWordBySpelling(spelling: String): Word?

    @Query(
        """
        SELECT spelling FROM word_list WHERE spelling LIKE :spelling 
        ORDER BY LENGTH(spelling) LIMIT $MAX_SUGGESTIONS_NUM
        """
    )
    suspend fun getWordSuggestionsBySimilarSpelling(spelling: String): List<String>

}