package com.peter.landing.domain.study

import com.peter.landing.data.local.progress.TypingProgress
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.pref.SettingPreferencesRepository
import com.peter.landing.data.repository.progress.DailyProgressRepository
import com.peter.landing.data.repository.progress.TypingProgressRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class TypingUseCase @Inject constructor(
    private val vocabularyViewRepository: VocabularyViewRepository,
    private val studyPlanRepository: StudyPlanRepository,
    private val dailyProgressRepository: DailyProgressRepository,
    private val typingProgressRepository: TypingProgressRepository,
    private val settingPreferencesRepository: SettingPreferencesRepository
) {

    private lateinit var typingProgress: TypingProgress
    private lateinit var wordList: List<Word>
    private var currentIndex = 0

    private var answer = ""

    fun typing(typing: String, scope: CoroutineScope): Boolean {
        val isCorrect = typing == answer
        if (isCorrect) {
            typingProgress.typed += 1
            scope.launch {
                typingProgressRepository.updateTypingProgress(typingProgress)
            }
        }

        return isCorrect
    }

    fun nextWord() {
        currentIndex += 1
    }

    fun getCurrentWord() = wordList[currentIndex]

    fun setAnswer(spelling: String) {
        this.answer = spelling
    }

    fun getCurrentNum() = currentIndex + 1

    fun getTotalNum() = wordList.size

    fun isLastWord() = (currentIndex + 1) == wordList.size

    fun setStudyState(studyState: Boolean, scope: CoroutineScope) = scope.launch {
        settingPreferencesRepository.setStudyState(studyState)
    }

    suspend fun initTodayTyping() {
        val studyPlan = studyPlanRepository.getStudyPlan()!!
        val dailyProgress = dailyProgressRepository.getTodayDailyProgress()!!
        typingProgress = typingProgressRepository
            .getTypingProgressByDailyProgressId(dailyProgress.id)
        wordList = vocabularyViewRepository.getWordList(
            dailyProgress.start, studyPlan.wordListSize, studyPlan.vocabularyName
        )
        currentIndex = typingProgress.typed
    }

}