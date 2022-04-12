package com.peter.landing.repository

import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.data.local.affix.AffixCatalogDAO
import com.peter.landing.data.repository.affix.AffixCatalogRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AffixCatalogRepositoryTest {

    private val dao = mockk<AffixCatalogDAO>(relaxed = true)
    private val repository = AffixCatalogRepository(dao)
    
    @Test
    fun getAffixCatalogList() = runBlocking {
        coEvery {
            dao.getAffixCatalogList()
        } returns listOf(AffixCatalog(AffixCatalog.Type.PREFIX, ""))

        repository.getAffixCatalogList()
        repository.getAffixCatalogList()

        coVerify(exactly = 1) {
            dao.getAffixCatalogList()
        }

        confirmVerified(dao)
    }

}