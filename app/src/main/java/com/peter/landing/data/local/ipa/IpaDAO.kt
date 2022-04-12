package com.peter.landing.data.local.ipa

import androidx.room.Dao
import androidx.room.Query

@Dao
interface IpaDAO {

    @Query("SELECT * FROM ipa")
    suspend fun getIpaList(): List<Ipa>

}