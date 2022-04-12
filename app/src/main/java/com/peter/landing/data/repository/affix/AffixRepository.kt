package com.peter.landing.data.repository.affix

import com.peter.landing.data.local.affix.Affix
import com.peter.landing.data.local.affix.AffixDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AffixRepository @Inject constructor(
    private val affixDAO: AffixDAO
) {

    suspend fun getAffixListByCatalog(catalogId: Long): List<Affix> =
        affixDAO.getAffixListByCatalogId(catalogId)

}