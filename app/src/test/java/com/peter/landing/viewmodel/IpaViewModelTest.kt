package com.peter.landing.viewmodel

import androidx.lifecycle.asLiveData
import com.peter.landing.data.local.ipa.Ipa
import com.peter.landing.domain.ipa.IpaUseCase
import com.peter.landing.ui.ipa.IpaViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class IpaViewModelTest {

    private val useCase = mockk<IpaUseCase>(relaxed = true)

    @Test
    fun setIpaType() {
        val viewModel = IpaViewModel(useCase)

        viewModel.setIpaType(Ipa.Type.CONSONANTS)

        coVerify(exactly = 1) {
            useCase.ipaList.asLiveData()
            useCase.initIpa(any())

            useCase.setIpaType(Ipa.Type.CONSONANTS, any())
        }

        confirmVerified(useCase)
    }

}