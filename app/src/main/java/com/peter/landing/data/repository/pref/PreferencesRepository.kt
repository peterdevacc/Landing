package com.peter.landing.data.repository.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.peter.landing.data.util.AGREEMENT_PREF
import com.peter.landing.data.util.DataResult
import com.peter.landing.data.util.THEME_MODE_PREF
import com.peter.landing.data.util.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private val agreementValue = booleanPreferencesKey(AGREEMENT_PREF)
    private val themeValue = stringPreferencesKey(THEME_MODE_PREF)

    suspend fun acceptAgreement(): DataResult<String> {
        return try {
            dataStore.edit { pref ->
                pref[agreementValue] = true
            }
            DataResult.Success("")
        } catch (exception: Exception) {
            if (exception is IOException) {
                DataResult.Error(code = DataResult.Error.Code.IO)
            } else {
                DataResult.Error(code = DataResult.Error.Code.UNKNOWN)
            }
        }
    }

    fun getAgreementValueFlow(): Flow<DataResult<Boolean>> {
        return dataStore.data
            .catch {
                DataResult.Error(code = DataResult.Error.Code.IO)
            }
            .map { pref ->
                val data = pref[agreementValue] ?: false
                DataResult.Success(data)
            }
    }

    suspend fun setTheme(themeMode: ThemeMode): DataResult<String> {
        return try {
            dataStore.edit { pref ->
                pref[themeValue] = themeMode.name
            }
            DataResult.Success("")
        } catch (exception: Exception) {
            if (exception is IOException) {
                DataResult.Error(code = DataResult.Error.Code.IO)
            } else {
                DataResult.Error(code = DataResult.Error.Code.UNKNOWN)
            }
        }
    }

    fun getThemeValueFlow(): Flow<DataResult<ThemeMode>> {
        return dataStore.data
            .catch {
                DataResult.Error(code = DataResult.Error.Code.IO)
            }
            .map { pref ->
                val data = pref[themeValue] ?: ThemeMode.DEFAULT.name
                val themeMode = ThemeMode.valueOf(data)
                DataResult.Success(themeMode)
            }
    }

}