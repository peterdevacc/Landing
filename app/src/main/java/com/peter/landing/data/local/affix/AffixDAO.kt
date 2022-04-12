package com.peter.landing.data.local.affix

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AffixDAO {

    @Query("SELECT * FROM affix WHERE catalog_id = :catalogId")
    suspend fun getAffixListByCatalogId(catalogId: Long): List<Affix>

}