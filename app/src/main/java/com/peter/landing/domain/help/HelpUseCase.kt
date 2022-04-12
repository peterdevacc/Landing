package com.peter.landing.domain.help

import com.peter.landing.data.repository.help.HelpCatalogRepository
import com.peter.landing.data.repository.help.HelpRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HelpUseCase @Inject constructor(
    private val helpCatalogRepository: HelpCatalogRepository,
    private val helpRepository: HelpRepository
) {

    private lateinit var helpItemListDeferred: Deferred<List<HelpItem>>

    suspend fun getHelpItemList(): List<HelpItem> {
        return try {
            helpItemListDeferred.await()
        } catch (exception: Exception) {
            emptyList()
        }
    }

    fun initHelp(scope: CoroutineScope) {
        helpItemListDeferred = scope.async {
            val helpItemList = mutableListOf<HelpItem>()
            val helpCatalogList = helpCatalogRepository.getHelpCatalogList()
            for (helpCatalog in helpCatalogList) {
                helpItemList.add(
                    HelpItem(
                        HelpItem.Type.ItemHelpCatalog,
                        HelpItem.Data.ItemHelpCatalog(helpCatalog)
                    )
                )
                val helpList = helpRepository.getHelpListByCatalog(helpCatalog.id)
                helpItemList.addAll(
                    helpList.map {
                        HelpItem(
                            HelpItem.Type.ItemHelp,
                            HelpItem.Data.ItemHelp(it)
                        )
                    }
                )
            }
            helpItemList.add(
                HelpItem(
                    HelpItem.Type.Footer,
                    HelpItem.Data.ItemHelpFooter
                )
            )
            helpItemList
        }
    }

}