package com.peter.landing.domain

import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.progress.DailyProgress
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.pref.SettingPreferencesRepository
import com.peter.landing.data.repository.progress.DailyProgressRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import com.peter.landing.debug.expectWord
import com.peter.landing.domain.study.WordListUseCase
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
class WordListUseCaseTest {
    private val vocabularyViewRepository = mockk<VocabularyViewRepository>(relaxed = true)
    private val studyPlanRepository = mockk<StudyPlanRepository>(relaxed = true)
    private val dailyProgressRepository = mockk<DailyProgressRepository>(relaxed = true)
    private val settingPreferencesRepository = mockk<SettingPreferencesRepository>(relaxed = true)

    private val useCase = WordListUseCase(
        vocabularyViewRepository,
        studyPlanRepository,
        dailyProgressRepository,
        settingPreferencesRepository
    )

    private val scope = LandingCoroutineScope()

    @Test
    fun wordList() = runBlocking {
        val plan = StudyPlan(
            Vocabulary.Name.BEGINNER, 10, getTodayDateTime()
        )
        val dailyProgress = DailyProgress(plan.id, 0)
        dailyProgress.id = 1
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
            vocabularyViewRepository.getWordList(
                dailyProgress.start, plan.wordListSize, plan.vocabularyName
            )
        } returns wordList

        useCase.initWordList(scope)
        val result = useCase.getWordList()
        assertEquals(wordList, result)

        coVerify(exactly = 1) {
            studyPlanRepository.getStudyPlan()
            dailyProgressRepository.getTodayDailyProgress()
            vocabularyViewRepository.getWordList(
                dailyProgress.start, plan.wordListSize, plan.vocabularyName
            )
        }

        confirmVerified(
            studyPlanRepository,
            dailyProgressRepository,
            vocabularyViewRepository
        )
    }

}