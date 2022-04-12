package com.peter.landing.data.repository.progress

import com.peter.landing.data.local.progress.ChoiceProgress
import com.peter.landing.data.local.progress.ChoiceProgressDAO
import com.peter.landing.util.LandingCoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChoiceProgressRepository @Inject constructor(
    private val scope: LandingCoroutineScope,
    private val choiceProgressDAO: ChoiceProgressDAO
) {

    suspend fun getChoiceProgressByDailyProgressId(dailyProgressId: Long) =
        choiceProgressDAO.getChoiceProgressByDailyProgressId(dailyProgressId)

    suspend fun addChoiceProgress(choiceProgress: ChoiceProgress) = scope.launch {
        choiceProgressDAO.insertChoiceProgress(choiceProgress)
    }.join()

    suspend fun updateChoiceProgress(choiceProgress: ChoiceProgress) = scope.launch {
        choiceProgressDAO.updateChoiceProgress(choiceProgress)
    }.join()

}