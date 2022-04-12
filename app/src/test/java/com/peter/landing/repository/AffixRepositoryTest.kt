package com.peter.landing.repository

import com.peter.landing.data.local.affix.AffixDAO
import com.peter.landing.data.repository.affix.AffixRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AffixRepositoryTest {

    private val dao = mockk<AffixDAO>(relaxed = true)
    private val repository = AffixRepository(dao)

    @Test
    fun getAffixListByCatalog() = runBlocking {
        val id: Long = 1
        repository.getAffixListByCatalog(id)

        coVerify {
            dao.getAffixListByCatalogId(id)
        }

        confirmVerified(dao)
    }

}