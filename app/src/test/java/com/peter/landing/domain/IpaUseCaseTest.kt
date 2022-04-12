package com.peter.landing.domain

import com.peter.landing.data.local.ipa.Ipa
import com.peter.landing.data.repository.ipa.IpaRepository
import com.peter.landing.domain.ipa.IpaItem
import com.peter.landing.domain.ipa.IpaUseCase
import com.peter.landing.util.LandingCoroutineScope
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class IpaUseCaseTest {

    private val ipaRepository = mockk<IpaRepository>(relaxed = true)

    private val useCase = IpaUseCase(
        ipaRepository
    )

    private val scope = LandingCoroutineScope()
    private val magicDelayNumber = 200L

    @Test
    fun ipaList() = runBlocking {
        val ipaCONSONANTS = Ipa(
            Ipa.Type.CONSONANTS,
            "",
            "",
            "",
            ""
        )
        val ipaVOWELS = Ipa(
            Ipa.Type.VOWELS,
            "",
            "",
            "",
            ""
        )
        coEvery {
            ipaRepository.getIpaList()
        } returns listOf(ipaCONSONANTS, ipaVOWELS)

        useCase.initIpa(scope)
        useCase.setIpaType(Ipa.Type.CONSONANTS, scope)
        delay(magicDelayNumber)

        val expect = listOf(
            IpaItem(
                IpaItem.Type.IpaTypeHeader,
                IpaItem.Data.IpaTypeHeader(ipaCONSONANTS.type.cnValue)
            ),
            IpaItem(
                IpaItem.Type.ItemIpa,
                IpaItem.Data.ItemIpa(ipaCONSONANTS)
            )
        )
        val result = useCase.ipaList.value
        assertEquals(expect, result)

        coVerify(exactly = 1) {
            ipaRepository.getIpaList()
        }

        confirmVerified(ipaRepository)
    }

}