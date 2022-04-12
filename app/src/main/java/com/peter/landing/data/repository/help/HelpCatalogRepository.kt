package com.peter.landing.data.repository.help

import com.peter.landing.data.local.help.HelpCatalog
import com.peter.landing.data.local.help.HelpCatalogDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HelpCatalogRepository @Inject constructor(
    private val helpCatalogDAO: HelpCatalogDAO
) {

    private var helpCatalogListCache = emptyList<HelpCatalog>()

    suspend fun getHelpCatalogList(): List<HelpCatalog> {
        if (helpCatalogListCache.isEmpty()) {
            helpCatalogListCache = helpCatalogDAO.getHelpCatalogList()
        }
        return helpCatalogListCache
    }

}