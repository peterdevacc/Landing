package com.peter.landing.data.repository.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.peter.landing.data.util.AGREEMENT_PREF
import com.peter.landing.data.util.STUDY_STATE
import com.peter.landing.data.util.THEME_PREF
import com.peter.landing.ui.util.ThemeMode
import com.peter.landing.util.LandingCoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingPreferencesRepository @Inject constructor(
    private val scope: LandingCoroutineScope,
    private val dataStore: DataStore<Preferences>
) {

    private val agreementValue = booleanPreferencesKey(AGREEMENT_PREF)
    private val themeValue = stringPreferencesKey(THEME_PREF)
    private val studyStateValue = booleanPreferencesKey(STUDY_STATE)

    suspend fun acceptAgreement() = scope.launch {
        dataStore.edit { pref ->
            pref[agreementValue] = true
        }
    }.join()

    val agreementValueFlow: Flow<Boolean> = dataStore.data.map { pref ->
        pref[agreementValue] ?: false
    }

    val themeValueFlow: Flow<String> = dataStore.data.map { pref ->
        pref[themeValue] ?: ThemeMode.DEFAULT.name
    }

    suspend fun saveCurrentTheme(theme: String) = scope.launch {
        dataStore.edit { pref ->
            pref[themeValue] = theme
        }
    }.join()

    val studyStateValueFlow: Flow<Boolean> = dataStore.data.map { pref ->
        pref[studyStateValue] ?: false
    }

    suspend fun setStudyState(studyState: Boolean) = scope.launch {
        dataStore.edit { pref ->
            pref[studyStateValue] = studyState
        }
    }.join()

}