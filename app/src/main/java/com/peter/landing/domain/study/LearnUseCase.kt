package com.peter.landing.domain.study

import com.peter.landing.data.local.progress.LearnProgress
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.pref.SettingPreferencesRepository
import com.peter.landing.data.repository.progress.DailyProgressRepository
import com.peter.landing.data.repository.progress.LearnProgressRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LearnUseCase @Inject constructor(
    private val vocabularyViewRepository: VocabularyViewRepository,
    private val studyPlanRepository: StudyPlanRepository,
    private val dailyProgressRepository: DailyProgressRepository,
    private val learnProgressRepository: LearnProgressRepository,
    private val settingPreferencesRepository: SettingPreferencesRepository
) {

    private lateinit var learnProgress: LearnProgress
    private lateinit var wordList: List<Word>
    private var currentIndex = 0

    fun learn(scope: CoroutineScope) {
        learnProgress.learned += 1
        scope.launch {
            learnProgressRepository.updateLearnProgress(learnProgress)
        }
    }

    fun isCurrentIndexBiggerThanLearned() = currentIndex > learnProgress.learned

    fun getCurrentWord() = wordList[currentIndex]

    fun getNextWord() {
        currentIndex += 1
    }

    fun getPreviousWord() {
        if (currentIndex > 0) {
            currentIndex -= 1
        }
    }

    fun getCurrentNum() = currentIndex + 1

    fun getTotalNum() = wordList.size

    fun isLastWord() = (currentIndex + 1) == wordList.size

    fun setStudyState(studyState: Boolean, scope: CoroutineScope) = scope.launch {
        settingPreferencesRepository.setStudyState(studyState)
    }

    suspend fun initTodayLearn() {
        val studyPlan = studyPlanRepository.getStudyPlan()!!
        val dailyProgress = dailyProgressRepository.getTodayDailyProgress()!!
        learnProgress = learnProgressRepository
            .getLearnProgressByDailyProgressId(dailyProgress.id)
        wordList = vocabularyViewRepository.getWordList(
            dailyProgress.start, studyPlan.wordListSize, studyPlan.vocabularyName
        )
        currentIndex = learnProgress.learned
    }

}