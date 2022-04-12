package com.peter.landing.domain

import com.peter.landing.data.repository.pref.SettingPreferencesRepository
import javax.inject.Inject

class MainUseCase @Inject constructor(
    private val settingPreferencesRepository: SettingPreferencesRepository
) {
    val agreementData = settingPreferencesRepository
        .agreementValueFlow

    val themeData = settingPreferencesRepository
        .themeValueFlow
}