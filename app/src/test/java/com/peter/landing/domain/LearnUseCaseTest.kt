package com.peter.landing.domain

import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.progress.DailyProgress
import com.peter.landing.data.local.progress.LearnProgress
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.pref.SettingPreferencesRepository
import com.peter.landing.data.repository.progress.DailyProgressRepository
import com.peter.landing.data.repository.progress.LearnProgressRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import com.peter.landing.debug.expectWord
import com.peter.landing.domain.study.LearnUseCase
import com.peter.landing.util.LandingCoroutineScope
import com.peter.landing.util.getTodayDateTime
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LearnUseCaseTest {

    private val vocabularyViewRepository = mockk<VocabularyViewRepository>(relaxed = true)
    private val studyPlanRepository = mockk<StudyPlanRepository>(relaxed = true)
    private val dailyProgressRepository = mockk<DailyProgressRepository>(relaxed = true)
    private val learnProgressRepository = mockk<LearnProgressRepository>(relaxed = true)
    private val settingPreferencesRepository = mockk<SettingPreferencesRepository>(relaxed = true)

    private val useCase = LearnUseCase(
        vocabularyViewRepository,
        studyPlanRepository,
        dailyProgressRepository,
        learnProgressRepository,
        settingPreferencesRepository
    )

    private val scope = LandingCoroutineScope()

    @Test
    fun learn() = runBlocking {
        val plan = StudyPlan(
            Vocabulary.Name.BEGINNER, 10, getTodayDateTime()
        )
        val dailyProgress = DailyProgress(plan.id, 0)
        dailyProgress.id = 1
        val learnProgress = LearnProgress(dailyProgress.id)
        val wordList = mutableListOf<Word>()
        repeat(10) {
            wordList.add(expectWord)
        }

        coEvery {
            studyPlanRepository.getStudyPlan()
        } returns plan
        coEvery {
            dailyProgressRepository.getTodayDailyProgress()
        } returns dailyProgress
        coEvery {
            learnProgressRepository.getLearnProgressByDailyProgressId(dailyProgress.id)
        } returns learnProgress
        coEvery {
            vocabularyViewRepository.getWordList(
                dailyProgress.start, plan.wordListSize, plan.vocabularyName
            )
        } returns wordList

        useCase.initTodayLearn()
        useCase.learn(scope)
        useCase.getNextWord()
        val result = useCase.getCurrentNum()
        assertEquals(2, result)

        coVerify(exactly = 1) {
            studyPlanRepository.getStudyPlan()
            dailyProgressRepository.getTodayDailyProgress()
            learnProgressRepository.getLearnProgressByDailyProgressId(dailyProgress.id)
            vocabularyViewRepository.getWordList(
                dailyProgress.start, plan.wordListSize, plan.vocabularyName
            )
            learnProgressRepository.updateLearnProgress(learnProgress)
        }

        confirmVerified(
            studyPlanRepository,
            dailyProgressRepository,
            vocabularyViewRepository,
            learnProgressRepository
        )
    }
    
}