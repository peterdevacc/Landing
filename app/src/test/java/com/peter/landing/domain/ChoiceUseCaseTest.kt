package com.peter.landing.domain

import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.progress.ChoiceProgress
import com.peter.landing.data.local.progress.DailyProgress
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.local.wrong.Wrong
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.pref.SettingPreferencesRepository
import com.peter.landing.data.repository.progress.ChoiceProgressRepository
import com.peter.landing.data.repository.progress.DailyProgressRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import com.peter.landing.debug.expectWord
import com.peter.landing.domain.study.ChoiceUseCase
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
class ChoiceUseCaseTest {

    private val vocabularyViewRepository = mockk<VocabularyViewRepository>(relaxed = true)
    private val studyPlanRepository = mockk<StudyPlanRepository>(relaxed = true)
    private val dailyProgressRepository = mockk<DailyProgressRepository>(relaxed = true)
    private val choiceProgressRepository = mockk<ChoiceProgressRepository>(relaxed = true)
    private val wrongRepository = mockk<WrongRepository>(relaxed = true)
    private val settingPreferencesRepository = mockk<SettingPreferencesRepository>(relaxed = true)

    private val useCase = ChoiceUseCase(
        vocabularyViewRepository,
        studyPlanRepository,
        dailyProgressRepository,
        choiceProgressRepository,
        wrongRepository,
        settingPreferencesRepository
    )

    private val scope = LandingCoroutineScope()

    @Test
    fun `choice correct`() = runBlocking {
        val plan = StudyPlan(
            Vocabulary.Name.BEGINNER, 10, getTodayDateTime()
        )
        val dailyProgress = DailyProgress(plan.id, 0)
        dailyProgress.id = 1
        val choiceProgress = ChoiceProgress(dailyProgress.id)
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
            choiceProgressRepository.getChoiceProgressByDailyProgressId(dailyProgress.id)
        } returns choiceProgress
        coEvery {
            vocabularyViewRepository.getWordList(
                dailyProgress.start, plan.wordListSize, plan.vocabularyName
            )
        } returns wordList

        useCase.initTodayChoice()
        useCase.setOptions()
        val result = useCase.choose(useCase.getAnswer(), scope)
        assertTrue(result)

        coVerify(exactly = 1) {
            studyPlanRepository.getStudyPlan()
            dailyProgressRepository.getTodayDailyProgress()
            choiceProgressRepository.getChoiceProgressByDailyProgressId(dailyProgress.id)
            vocabularyViewRepository.getWordList(
                dailyProgress.start, plan.wordListSize, plan.vocabularyName
            )
            choiceProgressRepository.updateChoiceProgress(choiceProgress)
        }

        coVerify(exactly = 0) {
            wrongRepository.addWrong(
                expectWord.id, Wrong.Type.CHOOSE
            )
        }

        confirmVerified(
            studyPlanRepository,
            dailyProgressRepository,
            choiceProgressRepository,
            vocabularyViewRepository
        )
    }

    @Test
    fun `choice wrong`() = runBlocking {
        val plan = StudyPlan(
            Vocabulary.Name.BEGINNER, 10, getTodayDateTime()
        )
        val dailyProgress = DailyProgress(plan.id, 0)
        dailyProgress.id = 1
        val choiceProgress = ChoiceProgress(dailyProgress.id)
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
            choiceProgressRepository.getChoiceProgressByDailyProgressId(dailyProgress.id)
        } returns choiceProgress
        coEvery {
            vocabularyViewRepository.getWordList(
                dailyProgress.start, plan.wordListSize, plan.vocabularyName
            )
        } returns wordList

        useCase.initTodayChoice()
        useCase.setOptions()
        val result = useCase.choose(" ", scope)
        assertFalse(result)

        coVerify(exactly = 1) {
            studyPlanRepository.getStudyPlan()
            dailyProgressRepository.getTodayDailyProgress()
            choiceProgressRepository.getChoiceProgressByDailyProgressId(dailyProgress.id)
            vocabularyViewRepository.getWordList(
                dailyProgress.start, plan.wordListSize, plan.vocabularyName
            )
            wrongRepository.addWrong(
                expectWord.id, Wrong.Type.CHOOSE
            )
            choiceProgressRepository.updateChoiceProgress(choiceProgress)
        }

        confirmVerified(
            studyPlanRepository,
            dailyProgressRepository,
            vocabularyViewRepository,
            wrongRepository,
            choiceProgressRepository
        )
    }


}