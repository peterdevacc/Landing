package com.peter.landing.domain.agreement

import com.peter.landing.data.repository.pref.SettingPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class AgreementUseCase @Inject constructor(
    private val settingPreferencesRepository: SettingPreferencesRepository
) {

    fun acceptAgreement(scope: CoroutineScope) = scope.launch {
        settingPreferencesRepository.acceptAgreement()
    }

}