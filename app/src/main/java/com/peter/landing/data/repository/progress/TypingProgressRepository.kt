package com.peter.landing.data.repository.progress

import com.peter.landing.data.local.progress.TypingProgress
import com.peter.landing.data.local.progress.TypingProgressDAO
import com.peter.landing.util.LandingCoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TypingProgressRepository @Inject constructor(
    private val scope: LandingCoroutineScope,
    private val typingProgressDAO: TypingProgressDAO
) {

    suspend fun getTypingProgressByDailyProgressId(dailyProgressId: Long) =
        typingProgressDAO.getTypingProgressByDailyProgressId(dailyProgressId)

    suspend fun addTypingProgress(typingProgress: TypingProgress) = scope.launch {
        typingProgressDAO.insertTypingProgress(typingProgress)
    }.join()

    suspend fun updateTypingProgress(typingProgress: TypingProgress) = scope.launch {
        typingProgressDAO.updateTypingProgress(typingProgress)
    }.join()

}