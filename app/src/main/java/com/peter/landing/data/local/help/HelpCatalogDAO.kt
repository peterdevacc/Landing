package com.peter.landing.data.local.help

import androidx.room.Dao
import androidx.room.Query

@Dao
interface HelpCatalogDAO {

    @Query("SELECT * FROM help_catalog")
    suspend fun getHelpCatalogList(): List<HelpCatalog>

}