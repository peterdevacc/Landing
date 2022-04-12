package com.peter.landing.data.repository.terms

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.peter.landing.data.local.terms.Terms
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TermsRepository @Inject constructor(
    private val googleJson: Gson
) {

    suspend fun getTerms(jsonStr: String): Terms = withContext(Dispatchers.Default) {
        return@withContext googleJson.fromJson(jsonStr, object : TypeToken<Terms>() {}.type)
    }

}