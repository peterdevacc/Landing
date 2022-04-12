package com.peter.landing.repository

import com.peter.landing.data.local.help.HelpDAO
import com.peter.landing.data.repository.help.HelpRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HelpRepositoryTest {

    private val dao = mockk<HelpDAO>(relaxed = true)
    private val repository = HelpRepository(dao)

    @Test
    fun getHelpListByCatalog() = runBlocking {
        val id: Long = 1
        repository.getHelpListByCatalog(id)

        coVerify {
            dao.getHelpListByCatalogId(id)
        }

        confirmVerified(dao)
    }

}