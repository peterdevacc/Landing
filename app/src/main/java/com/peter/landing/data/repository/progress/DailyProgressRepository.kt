package com.peter.landing.data.repository.progress

import com.peter.landing.data.local.progress.DailyProgress
import com.peter.landing.data.local.progress.DailyProgressDAO
import com.peter.landing.util.LandingCoroutineScope
import com.peter.landing.util.getTodayDateTime
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyProgressRepository @Inject constructor(
    private val scope: LandingCoroutineScope,
    private val dailyProgressDAO: DailyProgressDAO
) {

    private var today = getTodayDateTime()

    suspend fun getTodayDailyProgress() =
        dailyProgressDAO.getDailyProgressByCreateDate(today)

    suspend fun getLastDailyProgress() =
        dailyProgressDAO.getDailyProgressBeforeDateAndLastOne(today)

    suspend fun addTodayDailyProgress(dailyProgress: DailyProgress) =
        withContext(scope.coroutineContext) {
            dailyProgressDAO.insertDailyProgress(dailyProgress)
        }

    suspend fun getTotalDailyProgressNum() =
        dailyProgressDAO.getCountInDailyProgress()

    suspend fun finishTodayDailyProgress() = scope.launch {
        dailyProgressDAO.updateDailyProgressIsFinishedToTrueByCreateDate(today)
    }.join()

    fun refreshDateTime(date: Calendar) {
        today = date
    }

}