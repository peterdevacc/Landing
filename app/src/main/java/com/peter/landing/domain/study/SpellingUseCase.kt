package com.peter.landing.domain.study

import com.peter.landing.data.local.progress.SpellingProgress
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.local.wrong.Wrong
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.pref.SettingPreferencesRepository
import com.peter.landing.data.repository.progress.DailyProgressRepository
import com.peter.landing.data.repository.progress.SpellingProgressRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SpellingUseCase @Inject constructor(
    private val vocabularyViewRepository: VocabularyViewRepository,
    private val studyPlanRepository: StudyPlanRepository,
    private val dailyProgressRepository: DailyProgressRepository,
    private val spellingProgressRepository: SpellingProgressRepository,
    private val wrongRepository: WrongRepository,
    private val settingPreferencesRepository: SettingPreferencesRepository
) {

    private lateinit var spellingProgress: SpellingProgress
    private lateinit var wordList: List<Word>
    private var currentIndex = 0

    private var answer = ""

    fun spell(input: String, scope: CoroutineScope): Boolean {
        spellingProgress.spelled += 1
        val isCorrect = input == answer
        if (isCorrect) {
            spellingProgress.spellCorrect += 1
        } else {
            scope.launch {
                wrongRepository.addWrong(
                    wordList[currentIndex].id, Wrong.Type.SPELLING
                )
            }
        }
        scope.launch {
            spellingProgressRepository.updateSpellingProgress(spellingProgress)
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

    suspend fun initTodaySpelling() {
        val studyPlan = studyPlanRepository.getStudyPlan()!!
        val dailyProgress = dailyProgressRepository.getTodayDailyProgress()!!
        spellingProgress = spellingProgressRepository
            .getSpellingProgressByDailyProgressId(dailyProgress.id)
        wordList = vocabularyViewRepository.getWordList(
            dailyProgress.start, studyPlan.wordListSize, studyPlan.vocabularyName
        )
        currentIndex = spellingProgress.spelled
    }

}