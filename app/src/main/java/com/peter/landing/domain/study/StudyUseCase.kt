package com.peter.landing.domain.study

import com.peter.landing.data.local.progress.*
import com.peter.landing.data.local.wrong.Wrong
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.pref.SettingPreferencesRepository
import com.peter.landing.data.repository.progress.*
import com.peter.landing.data.repository.vocabulary.VocabularyRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import com.peter.landing.util.getTodayDateTime
import com.peter.landing.util.getYesterdayDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class StudyUseCase @Inject constructor(
    private val vocabularyRepository: VocabularyRepository,
    private val vocabularyViewRepository: VocabularyViewRepository,
    private val studyPlanRepository: StudyPlanRepository,
    private val dailyProgressRepository: DailyProgressRepository,
    private val learnProgressRepository: LearnProgressRepository,
    private val choiceProgressRepository: ChoiceProgressRepository,
    private val typingProgressRepository: TypingProgressRepository,
    private val spellingProgressRepository: SpellingProgressRepository,
    private val reviseProgressRepository: ReviseProgressRepository,
    private val wrongRepository: WrongRepository,
    private val settingPreferencesRepository: SettingPreferencesRepository
) {

    suspend fun initStudy(): PlanState {
        val studyPlan = studyPlanRepository.getStudyPlan()
        studyPlan?.let { plan ->
            val spelledSum = spellingProgressRepository.getTotalSpelled()
            val vocabulary = vocabularyRepository.getVocabularyByName(plan.vocabularyName)
            if (spelledSum == vocabulary.size) {
                val totalNeedRevisedNum = wrongRepository.getTotalReviseNum()
                return if (totalNeedRevisedNum == 0) {
                    reviseProgressRepository.removeReviseProgress()
                    vocabularyViewRepository.emptyWordListCache()

                    PlanState.FINISHED
                } else {
                    val todayNeedRevisedNum = wrongRepository.getTodayReviseNum()
                    if (todayNeedRevisedNum != 0) {
                        val reviseProgress = reviseProgressRepository.getTodayReviseProgress()
                        if (reviseProgress == null) {
                            reviseProgressRepository.addTodayReviseProgress(ReviseProgress())
                        }
                        return PlanState.REVISE
                    }

                    PlanState.COMPLETED_BUT_HAVE_REVISE_REMAIN
                }
            } else {
                if (plan.startDate!! <= getTodayDateTime()) {
                    // update revise data
                    val lastDailyProgress = dailyProgressRepository.getLastDailyProgress()
                    lastDailyProgress?.let { progress ->
                        val remain = vocabulary.size - progress.start
                        val wordListSize = if (remain < plan.wordListSize) {
                            remain
                        } else {
                            plan.wordListSize
                        }

                        val spellingProgress = spellingProgressRepository
                            .getSpellingProgressByDailyProgressId(progress.id)
                        val gap = wordListSize - spellingProgress.spelled
                        if (gap > 0) {
                            val start = progress.start + spellingProgress.spelled

                            val learnProgress = learnProgressRepository
                                .getLearnProgressByDailyProgressId(progress.id)
                            val choiceProgress = choiceProgressRepository
                                .getChoiceProgressByDailyProgressId(progress.id)
                            val typingProgress = typingProgressRepository
                                .getTypingProgressByDailyProgressId(progress.id)

                            learnProgress.learned = wordListSize
                            choiceProgress.chosen = wordListSize
                            typingProgress.typed = wordListSize
                            spellingProgress.spelled = wordListSize

                            learnProgressRepository.updateLearnProgress(learnProgress)
                            choiceProgressRepository.updateChoiceProgress(choiceProgress)
                            typingProgressRepository.updateTypingProgress(typingProgress)
                            spellingProgressRepository.updateSpellingProgress(spellingProgress)

                            val skippedWordList = vocabularyViewRepository
                                .getWordList(start, wordListSize, plan.vocabularyName)
                            wrongRepository.addWrongList(
                                skippedWordList.map {
                                    Wrong(
                                        it.id, chosenWrong = true, spelledWrong = true,
                                        reviseDate = getTodayDateTime(),
                                        addDate = getYesterdayDateTime()
                                    )
                                }
                            )
                        }
                    }

                    val todayNeedRevisedNum = wrongRepository.getTodayReviseNum()
                    if (todayNeedRevisedNum != 0) {
                        val reviseProgress = reviseProgressRepository.getTodayReviseProgress()
                        if (reviseProgress == null) {
                            reviseProgressRepository.addTodayReviseProgress(ReviseProgress())
                        }
                        return PlanState.REVISE
                    }

                    // init today progress
                    val dailyProgress = dailyProgressRepository.getTodayDailyProgress()
                    if (dailyProgress == null) {
                        val num = dailyProgressRepository.getTotalDailyProgressNum()
                        val start = num * plan.wordListSize
                        val dailyProgressId = dailyProgressRepository.addTodayDailyProgress(
                            DailyProgress(plan.id, start)
                        )
                        learnProgressRepository.addLearnProgress(
                            LearnProgress(dailyProgressId)
                        )
                        choiceProgressRepository.addChoiceProgress(
                            ChoiceProgress(dailyProgressId)
                        )
                        typingProgressRepository.addTypingProgress(
                            TypingProgress(dailyProgressId)
                        )
                        spellingProgressRepository.addSpellingProgress(
                            SpellingProgress(dailyProgressId)
                        )
                    }

                    return PlanState.LEARN
                }

                return PlanState.NON_START
            }
        }

        return PlanState.NONE
    }

    suspend fun getStudyProgressState(): StudyProgressState {
        val todayDailyProgress = dailyProgressRepository.getTodayDailyProgress()
        todayDailyProgress?.run {
            val studyPlan = studyPlanRepository.getStudyPlan()!!
            val vocabulary = vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
            val learnProgress = learnProgressRepository
                .getLearnProgressByDailyProgressId(id)
            val choiceProgress = choiceProgressRepository
                .getChoiceProgressByDailyProgressId(id)
            val typingProgress = typingProgressRepository
                .getTypingProgressByDailyProgressId(id)
            val spellingProgress = spellingProgressRepository
                .getSpellingProgressByDailyProgressId(id)

            val remain = vocabulary.size - start
            val wordListSize = if (remain < studyPlan.wordListSize) {
                remain
            } else {
                studyPlan.wordListSize
            }
            if (learnProgress.learned == 0) {
                return StudyProgressState.FreshStart
            } else if (
                learnProgress.learned >= 1 &&
                learnProgress.learned <= wordListSize - 1
            ) {
                return StudyProgressState.Learn
            } else if (
                learnProgress.learned == wordListSize &&
                choiceProgress.chosen == 0
            ) {
                return StudyProgressState.LearnCompleted
            } else if (
                choiceProgress.chosen >= 1 &&
                choiceProgress.chosen <= wordListSize - 1
            ) {
                return StudyProgressState.Choice
            } else if (
                choiceProgress.chosen == wordListSize &&
                typingProgress.typed == 0
            ) {
                return StudyProgressState.ChoiceCompleted
            } else if (
                typingProgress.typed >= 1 &&
                typingProgress.typed <= wordListSize - 1
            ) {
                return StudyProgressState.Typing
            } else if (
                typingProgress.typed == wordListSize &&
                spellingProgress.spelled == 0
            ) {
                return StudyProgressState.TypingCompleted
            } else if (
                spellingProgress.spelled >= 1 &&
                spellingProgress.spelled <= wordListSize - 1
            ) {
                return StudyProgressState.Spelling
            } else if (
                spellingProgress.spelled == wordListSize &&
                !isFinished
            ) {
                return StudyProgressState.SpellingCompleted
            } else if (isFinished) {
                return StudyProgressState.TodayFinished
            }
        }
        return StudyProgressState.None
    }

    val currentThemeData = settingPreferencesRepository
        .themeValueFlow

    fun saveCurrentTheme(theme: String, scope: CoroutineScope) = scope.launch {
        settingPreferencesRepository.saveCurrentTheme(theme)
    }

    val currentStudyStateData = settingPreferencesRepository
        .studyStateValueFlow

    fun refreshDate() {
        val today = getTodayDateTime()
        dailyProgressRepository.refreshDateTime(today)
        reviseProgressRepository.refreshDateTime(today)
        wrongRepository.refreshDateTime(today)
    }

}