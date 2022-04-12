package com.peter.landing.data.local.affix

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AffixCatalogDAO {

    @Query("SELECT * FROM affix_catalog")
    suspend fun getAffixCatalogList(): List<AffixCatalog>

}