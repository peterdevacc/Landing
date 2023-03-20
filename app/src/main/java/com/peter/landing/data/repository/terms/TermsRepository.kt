package com.peter.landing.data.repository.terms

import android.content.res.AssetManager
import com.peter.landing.data.local.terms.Terms
import com.peter.landing.data.util.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TermsRepository @Inject constructor(
    private val assetManager: AssetManager
) {

    private val termsMap = mutableMapOf<Terms.Type, Terms>()

    suspend fun getTerms(type: Terms.Type): DataResult<Terms> =
        withContext(Dispatchers.IO) {
            if (termsMap[type] == null) {
                try {
                    val jsonFileInputStream = assetManager.open(type.getFileName())
                    val jsonString = jsonFileInputStream.bufferedReader().readText()
                    val info = Json.decodeFromString<Terms>(jsonString)
                    termsMap[type] = info
                    jsonFileInputStream.close()
                } catch (exception: Exception) {
                    val code = if (exception is IOException) {
                        DataResult.Error.Code.IO
                    } else {
                        DataResult.Error.Code.UNKNOWN
                    }

                    return@withContext DataResult.Error(code)
                }
            }

            return@withContext DataResult.Success(termsMap.getValue(type))
        }

}