package com.peter.landing.data.repository.wrong

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.peter.landing.data.local.wrong.Wrong
import com.peter.landing.data.local.wrong.WrongDAO
import com.peter.landing.data.local.wrong.WrongWord
import com.peter.landing.util.LandingCoroutineScope
import com.peter.landing.util.getTodayDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WrongRepository @Inject constructor(
    private val scope: LandingCoroutineScope,
    private val wrongDAO: WrongDAO
) {

    private var today = getTodayDateTime()

    suspend fun getTodayReviseNum() =
        wrongDAO.countWrongByReviseDateAndReviseTimesLessThanThree(today)

    suspend fun getTotalReviseNum() =
        wrongDAO.countWrongByReviseTimesLessThanThree()

    suspend fun markWrongNoted(wordId: Long) = scope.launch {
        val wrong = wrongDAO.getWrongById(wordId)
        wrong?.run {
            isNoted = true
            wrongDAO.updateWrong(this)
        }
    }.join()

    suspend fun unMarkWrongNoted(wordId: Long) = scope.launch {
        val wrong = wrongDAO.getWrongById(wordId)
        wrong?.run {
            isNoted = false
            wrongDAO.updateWrong(this)
        }
    }.join()

    suspend fun addWrong(wordId: Long, type: Wrong.Type) = scope.launch {
        when (type) {
            Wrong.Type.CHOOSE -> {
                wrongDAO.insertWrong(
                    Wrong(wordId, chosenWrong = true)
                )
            }
            Wrong.Type.SPELLING -> {
                val wrong = wrongDAO.getWrongById(wordId)
                if (wrong != null) {
                    wrong.spelledWrong = true
                    wrongDAO.updateWrong(wrong)
                } else {
                    wrongDAO.insertWrong(
                        Wrong(wordId, spelledWrong = true)
                    )
                }
            }
        }
    }.join()

    suspend fun addWrongList(wrongList: List<Wrong>) = scope.launch {
        wrongDAO.insertWrongList(wrongList)
    }.join()

    suspend fun updateWrong(wrong: Wrong) = scope.launch {
        wrongDAO.updateWrong(wrong)
    }.join()

    suspend fun updateWrongList(wrongList: List<Wrong>) = scope.launch {
        wrongDAO.updateWrongList(wrongList)
    }.join()

    suspend fun removeWrong() = scope.launch {
        wrongDAO.deleteWrong()
    }.join()

    fun getTodayChosenWrongWordListFlow() =
        wrongDAO.getWrongWordListByAddDateAndChosenWrongFlow(today)
            .flowOn(Dispatchers.IO)

    fun getTodaySpelledWrongWordListFlow() =
        wrongDAO.getWrongWordListByAddDateAndSpelledWrongFlow(today)
            .flowOn(Dispatchers.IO)

    fun getWrongWordFlow(): Flow<PagingData<WrongWord>> {
        return Pager(
            PagingConfig(
                pageSize = 30,
                enablePlaceholders = false,
                maxSize = 100
            )
        ) { wrongDAO.getWrongWordPaging() }.flow
    }

    suspend fun getTodayReviseWordList() =
        wrongDAO.getWrongWordListByReviseDateAndReviseTimeLessThanFour(today)

    fun refreshDateTime(date: Calendar) {
        today = date
    }

}