package com.peter.landing.domain.study

import com.peter.landing.data.local.word.Word
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.pref.SettingPreferencesRepository
import com.peter.landing.data.repository.progress.DailyProgressRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class WordListUseCase @Inject constructor(
    private val vocabularyViewRepository: VocabularyViewRepository,
    private val studyPlanRepository: StudyPlanRepository,
    private val dailyProgressRepository: DailyProgressRepository,
    private val settingPreferencesRepository: SettingPreferencesRepository
) {

    private lateinit var wordListDeferred: Deferred<List<Word>>

    suspend fun getWordList(): List<Word> {
        return try {
            wordListDeferred.await()
        } catch (exception: Exception) {
            emptyList()
        }
    }

    fun setStudyState(studyState: Boolean, scope: CoroutineScope) = scope.launch {
        settingPreferencesRepository.setStudyState(studyState)
    }

    fun initWordList(scope: CoroutineScope) {
        wordListDeferred = scope.async {
            val studyPlan = studyPlanRepository.getStudyPlan()!!
            val dailyProgress = dailyProgressRepository.getTodayDailyProgress()!!
            vocabularyViewRepository.getWordList(
                dailyProgress.start, studyPlan.wordListSize, studyPlan.vocabularyName
            )
        }
    }

}