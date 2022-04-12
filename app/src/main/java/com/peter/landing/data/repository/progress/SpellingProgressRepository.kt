package com.peter.landing.data.repository.progress

import com.peter.landing.data.local.progress.SpellingProgress
import com.peter.landing.data.local.progress.SpellingProgressDAO
import com.peter.landing.util.LandingCoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpellingProgressRepository @Inject constructor(
    private val scope: LandingCoroutineScope,
    private val spellingProgressDAO: SpellingProgressDAO
) {

    suspend fun getSpellingProgressByDailyProgressId(dailyProgressId: Long) =
        spellingProgressDAO.getSpellingProgressByDailyProgressId(dailyProgressId)

    suspend fun addSpellingProgress(spellingProgress: SpellingProgress) = scope.launch {
        spellingProgressDAO.insertSpellingProgress(spellingProgress)
    }.join()

    suspend fun updateSpellingProgress(spellingProgress: SpellingProgress) = scope.launch {
        spellingProgressDAO.updateSpellingProgress(spellingProgress)
    }.join()

    suspend fun getTotalSpelled() =
        spellingProgressDAO.getSpelledSumInSpellingProgress()

}