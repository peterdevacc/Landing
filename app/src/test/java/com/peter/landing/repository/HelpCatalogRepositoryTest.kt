package com.peter.landing.repository

import com.peter.landing.data.local.help.HelpCatalog
import com.peter.landing.data.local.help.HelpCatalogDAO
import com.peter.landing.data.repository.help.HelpCatalogRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HelpCatalogRepositoryTest {

    private val dao = mockk<HelpCatalogDAO>(relaxed = true)
    private val repository = HelpCatalogRepository(dao)
    
    @Test
    fun getHelpCatalogList() = runBlocking {
        coEvery {
            dao.getHelpCatalogList()
        } returns listOf(HelpCatalog("", ""))

        repository.getHelpCatalogList()
        repository.getHelpCatalogList()

        coVerify(exactly = 1) {
            dao.getHelpCatalogList()
        }

        confirmVerified(dao)
    }

}