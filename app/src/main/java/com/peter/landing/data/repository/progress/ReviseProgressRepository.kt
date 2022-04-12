package com.peter.landing.data.repository.progress

import com.peter.landing.data.local.progress.ReviseProgress
import com.peter.landing.data.local.progress.ReviseProgressDAO
import com.peter.landing.util.LandingCoroutineScope
import com.peter.landing.util.getTodayDateTime
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviseProgressRepository @Inject constructor(
    private val scope: LandingCoroutineScope,
    private val reviseProgressDAO: ReviseProgressDAO
) {

    private var today = getTodayDateTime()

    suspend fun getTodayReviseProgress() =
        reviseProgressDAO.getReviseProgressByCreateDate(today)

    suspend fun addTodayReviseProgress(reviseProgress: ReviseProgress) = scope.launch {
        reviseProgressDAO.insertReviseProgress(reviseProgress)
    }.join()

    suspend fun updateReviseProcess(reviseProgress: ReviseProgress) = scope.launch {
        reviseProgressDAO.updateReviseProgress(reviseProgress)
    }.join()

    suspend fun removeReviseProgress() = scope.launch {
        reviseProgressDAO.deleteReviseProgress()
    }.join()

    fun refreshDateTime(date: Calendar) {
        today = date
    }

}