package com.peter.landing.data.local.vocabulary

import androidx.room.Dao
import androidx.room.Query

@Dao
interface VocabularyDAO {

    @Query("SELECT * FROM vocabulary")
    suspend fun getVocabularyList(): List<Vocabulary>

}