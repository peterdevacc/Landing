package com.peter.landing.viewmodel

import androidx.lifecycle.asLiveData
import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.domain.affix.AffixUseCase
import com.peter.landing.ui.affix.AffixViewModel
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AffixViewModelTest {

    private val useCase = mockk<AffixUseCase>(relaxed = true)

    @Test
    fun setAffixCatalogType() {
        val viewModel = AffixViewModel(useCase)

        viewModel.setAffixCatalogType(AffixCatalog.Type.PREFIX)

        verify(exactly = 1) {
            useCase.initAffix(any())
            useCase.affixData.asLiveData()

            useCase.setAffixCatalogType(AffixCatalog.Type.PREFIX, any())
        }

        confirmVerified(useCase)
    }

}