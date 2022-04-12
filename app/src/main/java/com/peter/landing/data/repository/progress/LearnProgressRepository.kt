package com.peter.landing.data.repository.progress

import com.peter.landing.data.local.progress.LearnProgress
import com.peter.landing.data.local.progress.LearnProgressDAO
import com.peter.landing.util.LandingCoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearnProgressRepository @Inject constructor(
    private val scope: LandingCoroutineScope,
    private val learnProgressDAO: LearnProgressDAO
) {

    suspend fun getLearnProgressByDailyProgressId(dailyProgressId: Long) =
        learnProgressDAO.getLearnProgressByDailyProgressId(dailyProgressId)

    suspend fun addLearnProgress(learnProgress: LearnProgress) = scope.launch {
        learnProgressDAO.insertLearnProgress(learnProgress)
    }.join()

    suspend fun updateLearnProgress(learnProgress: LearnProgress) = scope.launch {
        learnProgressDAO.updateLearnProgress(learnProgress)
    }.join()

    suspend fun getTotalLearned() =
        learnProgressDAO.getLearnedSumInLearnProgress()

}