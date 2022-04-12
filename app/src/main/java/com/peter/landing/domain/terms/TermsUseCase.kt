package com.peter.landing.domain.terms

import com.peter.landing.data.local.terms.Terms
import com.peter.landing.data.repository.terms.TermsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TermsUseCase @Inject constructor(
    private val termsRepository: TermsRepository
) {

    private lateinit var termsDeferred: Deferred<Terms?>

    suspend fun getTerms(): Terms? {
        return try {
            termsDeferred.await()
        } catch (exception: Exception) {
            null
        }
    }

    fun initTerms(jsonStr: String, scope: CoroutineScope) {
        termsDeferred = scope.async {
            termsRepository.getTerms(jsonStr)
        }
    }

}