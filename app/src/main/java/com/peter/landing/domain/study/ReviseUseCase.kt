package com.peter.landing.domain.study

import com.peter.landing.data.local.progress.ReviseProgress
import com.peter.landing.data.local.wrong.WrongWord
import com.peter.landing.data.repository.progress.ReviseProgressRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import com.peter.landing.util.getThreeDayAfterDateTime
import com.peter.landing.util.getTwoDayAfterDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReviseUseCase @Inject constructor(
    private val reviseProgressRepository: ReviseProgressRepository,
    private val wrongRepository: WrongRepository
) {
    private var wrongWordList = emptyList<WrongWord>()
    private lateinit var reviseProgress: ReviseProgress
    private var currentPosition = 0

    private var isArranged = false

    fun getTodayReviseWordList() = wrongWordList

    fun revised(scope: CoroutineScope) {
        wrongWordList[currentPosition].wrong.reviseTimes += 1
        scope.launch {
            wrongRepository.updateWrong(wrongWordList[currentPosition].wrong)
        }
        reviseProgress.revised += 1
        if (reviseProgress.revised == wrongWordList.size) {
            wrongWordList.forEach {
                when (it.wrong.reviseTimes) {
                    2 -> it.wrong.reviseDate = getThreeDayAfterDateTime()
                    1 -> it.wrong.reviseDate = getTwoDayAfterDateTime()
                }
            }
            scope.launch {
                wrongRepository.updateWrongList(wrongWordList.map { it.wrong })
            }
        }
        scope.launch {
            reviseProgressRepository.updateReviseProcess(reviseProgress)
        }
    }

    fun getCurrentWrongWord() = wrongWordList[currentPosition]

    fun nextWord() {
        currentPosition += 1
    }

    fun isCorrect(answer: String) = wrongWordList[currentPosition].word.spelling == answer

    fun setArranged(arranged: Boolean) {
        this.isArranged = arranged
    }

    fun isArranged() = this.isArranged

    fun getCurrentNum() = currentPosition + 1

    fun getTotalNum() = wrongWordList.size

    fun isLastWord() = (currentPosition + 1) == wrongWordList.size

    suspend fun initTodayRevise() {
        wrongWordList = wrongRepository.getTodayReviseWordList()
        reviseProgress = reviseProgressRepository.getTodayReviseProgress()!!
        currentPosition = reviseProgress.revised
    }

}