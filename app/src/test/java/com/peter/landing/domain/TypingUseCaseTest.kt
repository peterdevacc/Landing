package com.peter.landing.domain

import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.progress.DailyProgress
import com.peter.landing.data.local.progress.TypingProgress
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.pref.SettingPreferencesRepository
import com.peter.landing.data.repository.progress.DailyProgressRepository
import com.peter.landing.data.repository.progress.TypingProgressRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import com.peter.landing.debug.expectWord
import com.peter.landing.domain.study.TypingUseCase
import com.peter.landing.util.LandingCoroutineScope
import com.peter.landing.util.getTodayDateTime
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TypingUseCaseTest {

    private val vocabularyViewRepository = mockk<VocabularyViewRepository>(relaxed = true)
    private val studyPlanRepository = mockk<StudyPlanRepository>(relaxed = true)
    private val dailyProgressRepository = mockk<DailyProgressRepository>(relaxed = true)
    private val typingProgressRepository = mockk<TypingProgressRepository>(relaxed = true)
    private val settingPreferencesRepository = mockk<SettingPreferencesRepository>(relaxed = true)

    private val useCase = TypingUseCase(
        vocabularyViewRepository,
        studyPlanRepository,
        dailyProgressRepository,
        typingProgressRepository,
        settingPreferencesRepository
    )

    private val scope = LandingCoroutineScope()

    @Test
    fun `typing correct`() = runBlocking {
        val plan = StudyPlan(
            Vocabulary.Name.BEGINNER, 10, getTodayDateTime()
        )
        val dailyProgress = DailyProgress(plan.id, 0)
        dailyProgress.id = 1
        val typingProgress = TypingProgress(dailyProgress.id)
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
            typingProgressRepository
                .getTypingProgressByDailyProgressId(dailyProgress.id)
        } returns typingProgress
        coEvery {
            vocabularyViewRepository.getWordList(
                dailyProgress.start, plan.wordListSize, plan.vocabularyName
            )
        } returns wordList

        useCase.initTodayTyping()
        useCase.setAnswer(expectWord.spelling)
        val result = useCase.typing(expectWord.spelling, scope)
        assertTrue(result)

        coVerify(exactly = 1) {
            studyPlanRepository.getStudyPlan()
            dailyProgressRepository.getTodayDailyProgress()
            typingProgressRepository
                .getTypingProgressByDailyProgressId(dailyProgress.id)
            vocabularyViewRepository.getWordList(
                dailyProgress.start, plan.wordListSize, plan.vocabularyName
            )

            typingProgressRepository.updateTypingProgress(typingProgress)
        }

        confirmVerified(
            vocabularyViewRepository,
            studyPlanRepository,
            dailyProgressRepository,
            typingProgressRepository
        )
    }

    @Test
    fun `typing wrong`() = runBlocking {
        val plan = StudyPlan(
            Vocabulary.Name.BEGINNER, 10, getTodayDateTime()
        )
        val dailyProgress = DailyProgress(plan.id, 0)
        dailyProgress.id = 1
        val typingProgress = TypingProgress(dailyProgress.id)
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
            typingProgressRepository
                .getTypingProgressByDailyProgressId(dailyProgress.id)
        } returns typingProgress
        coEvery {
            vocabularyViewRepository.getWordList(
                dailyProgress.start, plan.wordListSize, plan.vocabularyName
            )
        } returns wordList

        useCase.initTodayTyping()
        useCase.setAnswer(expectWord.spelling)
        val result = useCase.typing("", scope)
        assertFalse(result)

        coVerify(exactly = 1) {
            studyPlanRepository.getStudyPlan()
            dailyProgressRepository.getTodayDailyProgress()
            typingProgressRepository
                .getTypingProgressByDailyProgressId(dailyProgress.id)
            vocabularyViewRepository.getWordList(
                dailyProgress.start, plan.wordListSize, plan.vocabularyName
            )
        }

        coVerify(exactly = 0) {
            typingProgressRepository.updateTypingProgress(typingProgress)
        }

        confirmVerified(
            vocabularyViewRepository,
            studyPlanRepository,
            dailyProgressRepository,
            typingProgressRepository
        )
    }

}