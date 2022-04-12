package com.peter.landing.data.local.help

import androidx.room.Dao
import androidx.room.Query

@Dao
interface HelpDAO {

    @Query("SELECT * FROM help WHERE catalog_id = :catalog_id")
    suspend fun getHelpListByCatalogId(catalog_id: Long): List<Help>

}