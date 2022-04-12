package com.peter.landing.data.repository.help

import com.peter.landing.data.local.help.HelpDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HelpRepository @Inject constructor(
    private val helpDAO: HelpDAO
) {

    suspend fun getHelpListByCatalog(helpCatalogId: Long) =
        helpDAO.getHelpListByCatalogId(helpCatalogId)

}