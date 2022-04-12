package com.peter.landing.data.local.vocabulary

import androidx.room.Dao
import androidx.room.Query

@Dao
interface VocabularyDAO {

    @Query("SELECT * FROM vocabulary WHERE name = :name")
    suspend fun getVocabularyByVocabularyName(name: Vocabulary.Name): Vocabulary

    @Query("SELECT * FROM vocabulary")
    suspend fun getVocabularyList(): List<Vocabulary>

}