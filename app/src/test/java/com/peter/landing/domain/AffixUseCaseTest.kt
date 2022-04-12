package com.peter.landing.domain

import com.peter.landing.data.local.affix.Affix
import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.data.repository.affix.AffixCatalogRepository
import com.peter.landing.data.repository.affix.AffixRepository
import com.peter.landing.domain.affix.AffixUseCase
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
class AffixUseCaseTest {

    private val affixCatalogRepository = mockk<AffixCatalogRepository>(relaxed = true)
    private val affixRepository = mockk<AffixRepository>(relaxed = true)

    private val useCase = AffixUseCase(
        affixCatalogRepository,
        affixRepository
    )

    private val scope = LandingCoroutineScope()
    private val magicDelayNumber = 200L

    @Test
    fun `affixData AffixCatalog-Type-PREFIX`() = runBlocking {
        val type = AffixCatalog.Type.PREFIX
        val prefix = AffixCatalog(type, "")
        prefix.id = 1
        val catalogList = listOf(prefix)
        val prefixList = listOf(
            Affix("prefix", "", "", prefix.id)
        )
        val expect = mapOf(
            prefix to prefixList
        )

        coEvery {
            affixCatalogRepository.getAffixCatalogList()
        } returns catalogList
        coEvery {
            affixRepository.getAffixListByCatalog(prefix.id)
        } returns prefixList

        useCase.initAffix(scope)
        useCase.setAffixCatalogType(type, scope)

        delay(magicDelayNumber)
        val result = useCase.affixData.value
        assertEquals(expect, result)

        coVerify(exactly = 1) {
            affixCatalogRepository.getAffixCatalogList()
            affixRepository.getAffixListByCatalog(prefix.id)
        }

        confirmVerified(affixCatalogRepository, affixRepository)
    }

    @Test
    fun `affixData AffixCatalog-Type-SUFFIX`() = runBlocking {
        val type = AffixCatalog.Type.SUFFIX
        val suffix = AffixCatalog(type, "")
        suffix.id = 2
        val catalogList = listOf(suffix)
        val suffixList = listOf(
            Affix("suffix", "", "", suffix.id)
        )
        val expect = mapOf(
            suffix to suffixList
        )

        coEvery {
            affixCatalogRepository.getAffixCatalogList()
        } returns catalogList
        coEvery {
            affixRepository.getAffixListByCatalog(suffix.id)
        } returns suffixList

        useCase.initAffix(scope)
        useCase.setAffixCatalogType(type, scope)

        delay(magicDelayNumber)
        val result = useCase.affixData.value
        assertEquals(expect, result)

        coVerify(exactly = 1) {
            affixCatalogRepository.getAffixCatalogList()
            affixRepository.getAffixListByCatalog(suffix.id)
        }

        confirmVerified(affixCatalogRepository, affixRepository)
    }

    @Test
    fun `affixData AffixCatalog-Type-MIXED`() = runBlocking {
        val type = AffixCatalog.Type.MIXED
        val mixed = AffixCatalog(type, "")
        mixed.id = 3
        val catalogList = listOf(mixed)
        val mixedList = listOf(
            Affix("mixed", "", "", mixed.id)
        )
        val expect = mapOf(
            mixed to mixedList
        )

        coEvery {
            affixCatalogRepository.getAffixCatalogList()
        } returns catalogList
        coEvery {
            affixRepository.getAffixListByCatalog(mixed.id)
        } returns mixedList

        useCase.initAffix(scope)
        useCase.setAffixCatalogType(type, scope)

        delay(magicDelayNumber)
        val result = useCase.affixData.value
        assertEquals(expect, result)

        coVerify(exactly = 1) {
            affixCatalogRepository.getAffixCatalogList()
            affixRepository.getAffixListByCatalog(mixed.id)
        }

        confirmVerified(affixCatalogRepository, affixRepository)
    }

}