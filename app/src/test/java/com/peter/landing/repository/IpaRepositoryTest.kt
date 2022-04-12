package com.peter.landing.repository

import com.peter.landing.data.local.ipa.Ipa
import com.peter.landing.data.local.ipa.IpaDAO
import com.peter.landing.data.repository.ipa.IpaRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class IpaRepositoryTest {

    private val dao = mockk<IpaDAO>(relaxed = true)
    private val repository = IpaRepository(dao)
    
    @Test
    fun getIpaList() = runBlocking {
        coEvery {
            dao.getIpaList()
        } returns listOf(
            Ipa(
                Ipa.Type.CONSONANTS,
                "",
                "",
                "",
                ""
            )
        )

        repository.getIpaList()
        repository.getIpaList()

        coVerify(exactly = 1) {
            dao.getIpaList()
        }

        confirmVerified(dao)
    }

}