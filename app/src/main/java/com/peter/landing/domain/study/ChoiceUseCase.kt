package com.peter.landing.domain.study

import com.peter.landing.data.local.progress.ChoiceProgress
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.local.wrong.Wrong
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.pref.SettingPreferencesRepository
import com.peter.landing.data.repository.progress.ChoiceProgressRepository
import com.peter.landing.data.repository.progress.DailyProgressRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChoiceUseCase @Inject constructor(
    private val vocabularyViewRepository: VocabularyViewRepository,
    private val studyPlanRepository: StudyPlanRepository,
    private val dailyProgressRepository: DailyProgressRepository,
    private val choiceProgressRepository: ChoiceProgressRepository,
    private val wrongRepository: WrongRepository,
    private val settingPreferencesRepository: SettingPreferencesRepository
) {

    private lateinit var choiceProgress: ChoiceProgress
    private lateinit var wordList: List<Word>
    private var currentIndex = 0

    private var options = emptyList<String>()
    private var answer = ""

    fun choose(option: String, scope: CoroutineScope): Boolean {
        choiceProgress.chosen += 1
        val isCorrect = option == answer
        if (isCorrect) {
            choiceProgress.chooseCorrect += 1
        } else {
            scope.launch {
                wrongRepository.addWrong(
                    wordList[currentIndex].id, Wrong.Type.CHOOSE
                )
            }
        }
        scope.launch {
            choiceProgressRepository.updateChoiceProgress(choiceProgress)
        }
        return isCorrect
    }

    fun getCurrentWord() = wordList[currentIndex]

    fun nextWord() {
        currentIndex += 1
    }

    fun getAnswer() = answer

    fun getOptions() = options

    fun setOptions() {
        val wordOptions = getShuffleListWithoutWord(wordList[currentIndex])
        val explainOptions = mutableListOf<String>()
        wordOptions.forEach {
            if (it.id == wordList[currentIndex].id) {
                answer = it.cn
            }
            explainOptions.add(it.cn)
        }
        options = explainOptions.toList()
    }

    fun getCurrentNum() = currentIndex + 1

    fun getTotalNum() = wordList.size

    fun isLastWord() = (currentIndex + 1) == wordList.size

    fun setStudyState(studyState: Boolean, scope: CoroutineScope) = scope.launch {
        settingPreferencesRepository.setStudyState(studyState)
    }

    suspend fun initTodayChoice() {
        val studyPlan = studyPlanRepository.getStudyPlan()!!
        val dailyProgress = dailyProgressRepository.getTodayDailyProgress()!!
        choiceProgress = choiceProgressRepository
            .getChoiceProgressByDailyProgressId(dailyProgress.id)
        wordList = vocabularyViewRepository.getWordList(
            dailyProgress.start, studyPlan.wordListSize, studyPlan.vocabularyName
        )
        currentIndex = choiceProgress.chosen
    }

    private fun getShuffleListWithoutWord(word: Word): List<Word> {
        val choiceList = wordList.toMutableList()
        choiceList.remove(word)
        choiceList.shuffle()
        val wordOptions = choiceList.take(3).toMutableList()
        wordOptions.add(word)
        wordOptions.shuffle()
        return wordOptions.toList()
    }

}