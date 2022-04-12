package com.peter.landing.data.local.vocabulary

import androidx.room.Dao
import androidx.room.Query
import com.peter.landing.data.local.word.Word

@Dao
interface VocabularyViewDAO {

    @Query("SELECT * FROM vocabulary_view_beginner LIMIT :start , :num")
    suspend fun getWordListFromBeginnerViewByRange(start: Int, num: Int): List<Word>

    @Query("SELECT * FROM vocabulary_view_intermediate LIMIT :start , :num")
    suspend fun getWordListFromIntermediateViewByRange(start: Int, num: Int): List<Word>

}