package com.peter.landing.domain

import com.peter.landing.data.local.help.Help
import com.peter.landing.data.local.help.HelpCatalog
import com.peter.landing.data.repository.help.HelpCatalogRepository
import com.peter.landing.data.repository.help.HelpRepository
import com.peter.landing.domain.help.HelpItem
import com.peter.landing.domain.help.HelpUseCase
import com.peter.landing.util.LandingCoroutineScope
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HelpUseCaseTest {

    private val helpCatalogRepository = mockk<HelpCatalogRepository>(relaxed = true)
    private val helpRepository = mockk<HelpRepository>(relaxed = true)

    private val useCase = HelpUseCase(
        helpCatalogRepository,
        helpRepository
    )

    private val scope = LandingCoroutineScope()

    @Test
    fun helpItemList() = runBlocking {
        val catalog = HelpCatalog("", "")
        catalog.id = 1
        val help = Help(catalog.id, "", "")
        coEvery {
            helpCatalogRepository.getHelpCatalogList()
        } returns listOf(catalog)
        coEvery {
            helpRepository.getHelpListByCatalog(catalog.id)
        } returns listOf(help)

        useCase.initHelp(scope)

        val expect = listOf(
            HelpItem(
                HelpItem.Type.ItemHelpCatalog,
                HelpItem.Data.ItemHelpCatalog(catalog)
            ),
            HelpItem(
                HelpItem.Type.ItemHelp,
                HelpItem.Data.ItemHelp(help)
            ),
            HelpItem(
                HelpItem.Type.Footer,
                HelpItem.Data.ItemHelpFooter
            )
        )
        val result = useCase.getHelpItemList()
        assertEquals(expect, result)

        coVerify(exactly = 1) {
            helpCatalogRepository.getHelpCatalogList()
            helpRepository.getHelpListByCatalog(catalog.id)
        }

        confirmVerified(helpCatalogRepository, helpRepository)
    }

}