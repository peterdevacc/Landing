package com.peter.landing.data.repository.affix

import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.data.local.affix.AffixCatalogDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AffixCatalogRepository @Inject constructor(
    private val affixCatalogDAO: AffixCatalogDAO
) {

    private var affixCatalog = emptyList<AffixCatalog>()

    suspend fun getAffixCatalogList(): List<AffixCatalog> {
        if (affixCatalog.isEmpty()) {
            affixCatalog = affixCatalogDAO.getAffixCatalogList()
        }
        return affixCatalog
    }

}