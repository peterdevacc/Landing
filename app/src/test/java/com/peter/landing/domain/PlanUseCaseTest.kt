package com.peter.landing.domain

import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.progress.*
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.progress.*
import com.peter.landing.data.repository.vocabulary.VocabularyRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import com.peter.landing.debug.expectStudyPlanBeginner
import com.peter.landing.debug.expectVocabularyBeginner
import com.peter.landing.domain.plan.PlanUseCase
import com.peter.landing.util.LandingCoroutineScope
import com.peter.landing.util.getEndDate
import com.peter.landing.util.getTodayDateTime
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PlanUseCaseTest {

    private val vocabularyRepository = mockk<VocabularyRepository>(relaxed = true)
    private val planRepository = mockk<StudyPlanRepository>(relaxed = true)
    private val vocabularyViewRepository = mockk<VocabularyViewRepository>(relaxed = true)
    private val dailyProgressRepository = mockk<DailyProgressRepository>(relaxed = true)
    private val learnProgressRepository = mockk<LearnProgressRepository>(relaxed = true)
    private val choiceProgressRepository = mockk<ChoiceProgressRepository>(relaxed = true)
    private val typingProgressRepository = mockk<TypingProgressRepository>(relaxed = true)
    private val spellingProgressRepository = mockk<SpellingProgressRepository>(relaxed = true)
    private val reviseProgressRepository = mockk<ReviseProgressRepository>(relaxed = true)
    private val wrongRepository = mockk<WrongRepository>(relaxed = true)

    private val useCase = PlanUseCase(
        vocabularyRepository,
        planRepository,
        vocabularyViewRepository,
        dailyProgressRepository,
        learnProgressRepository,
        choiceProgressRepository,
        typingProgressRepository,
        spellingProgressRepository,
        reviseProgressRepository,
        wrongRepository
    )

    private val scope = LandingCoroutineScope()

    @Test
    fun getStudyPlanFlow() {
        useCase.getStudyPlanFlow()

        coVerify(exactly = 1) {
            planRepository.getStudyPlanFlow()
        }

        confirmVerified(planRepository)
    }

    @Test
    fun getPlanEndDate() = runBlocking {
        coEvery {
            vocabularyRepository.getVocabularyByName(expectStudyPlanBeginner.vocabularyName)
        } returns expectVocabularyBeginner

        val expect = getEndDate(
            expectVocabularyBeginner.size,
            expectStudyPlanBeginner.startDate!!,
            expectStudyPlanBeginner.wordListSize
        )
        val result = useCase.getPlanEndDate(expectStudyPlanBeginner)
        assertEquals(expect, result)

        coVerify(exactly = 1) {
            vocabularyRepository.getVocabularyByName(expectStudyPlanBeginner.vocabularyName)
        }

        confirmVerified(planRepository)
    }

    @Test
    fun deletePlan() = runBlocking {
        useCase.deletePlan(scope)

        coVerify(exactly = 1) {
            vocabularyViewRepository.emptyWordListCache()
            reviseProgressRepository.removeReviseProgress()
            planRepository.removeStudyPlan()
            wrongRepository.removeWrong()
        }

        confirmVerified(
            vocabularyViewRepository,
            reviseProgressRepository,
            planRepository,
            wrongRepository
        )
    }

    @Test
    fun `getDailyPercentage DailyProgress not exist and plan not finished`() = runBlocking {
        coEvery {
            dailyProgressRepository.getTodayDailyProgress()
        } returns null
        coEvery {
            vocabularyRepository.getVocabularyByName(expectStudyPlanBeginner.vocabularyName)
        } returns expectVocabularyBeginner
        coEvery {
            spellingProgressRepository.getTotalSpelled()
        } returns 0

        val expect = listOf(0f, 0f, 0f, 0f)
        val result = useCase.getDailyPercentage(expectStudyPlanBeginner)
        assertEquals(expect, result)

        coVerify(exactly = 1) {
            dailyProgressRepository.getTodayDailyProgress()
            vocabularyRepository.getVocabularyByName(expectStudyPlanBeginner.vocabularyName)
            spellingProgressRepository.getTotalSpelled()
        }

        confirmVerified(
            dailyProgressRepository,
            vocabularyRepository,
            spellingProgressRepository
        )
    }

    @Test
    fun `getDailyPercentage DailyProgress not exist and plan finished`() = runBlocking {
        coEvery {
            dailyProgressRepository.getTodayDailyProgress()
        } returns null
        coEvery {
            vocabularyRepository.getVocabularyByName(expectStudyPlanBeginner.vocabularyName)
        } returns expectVocabularyBeginner
        coEvery {
            spellingProgressRepository.getTotalSpelled()
        } returns expectVocabularyBeginner.size

        val result = useCase.getDailyPercentage(expectStudyPlanBeginner)
        assertNull(result)

        coVerify(exactly = 1) {
            dailyProgressRepository.getTodayDailyProgress()
            vocabularyRepository.getVocabularyByName(expectStudyPlanBeginner.vocabularyName)
            spellingProgressRepository.getTotalSpelled()
        }

        confirmVerified(
            dailyProgressRepository,
            vocabularyRepository,
            spellingProgressRepository
        )
    }

    @Test
    fun `getDailyPercentage DailyProgress exist and plan not finished`() = runBlocking {
        val plan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        val dailyProgress = DailyProgress(plan.id, 0)
        dailyProgress.id = 1
        coEvery {
            dailyProgressRepository.getTodayDailyProgress()
        } returns dailyProgress

        coEvery {
            vocabularyRepository.getVocabularyByName(plan.vocabularyName)
        } returns expectVocabularyBeginner

        val learnProgress = LearnProgress(dailyProgress.id)
        learnProgress.learned = plan.wordListSize
        coEvery {
            learnProgressRepository.getLearnProgressByDailyProgressId(dailyProgress.id)
        } returns learnProgress

        val choiceProgress = ChoiceProgress(dailyProgress.id)
        choiceProgress.chosen = plan.wordListSize
        coEvery {
            choiceProgressRepository.getChoiceProgressByDailyProgressId(dailyProgress.id)
        } returns choiceProgress

        val typingProgress = TypingProgress(dailyProgress.id)
        typingProgress.typed = plan.wordListSize
        coEvery {
            typingProgressRepository.getTypingProgressByDailyProgressId(dailyProgress.id)
        } returns typingProgress

        val spellingProgress = SpellingProgress(dailyProgress.id)
        spellingProgress.spelled = plan.wordListSize
        coEvery {
            spellingProgressRepository.getSpellingProgressByDailyProgressId(dailyProgress.id)
        } returns spellingProgress

        val expect = listOf(100f, 100f, 100f, 100f)
        val result = useCase.getDailyPercentage(plan)
        assertEquals(expect, result)

        coVerify(exactly = 1) {
            dailyProgressRepository.getTodayDailyProgress()
            vocabularyRepository.getVocabularyByName(plan.vocabularyName)
            learnProgressRepository.getLearnProgressByDailyProgressId(dailyProgress.id)
            choiceProgressRepository.getChoiceProgressByDailyProgressId(dailyProgress.id)
            typingProgressRepository.getTypingProgressByDailyProgressId(dailyProgress.id)
            spellingProgressRepository.getSpellingProgressByDailyProgressId(dailyProgress.id)
        }

        coVerify(exactly = 0) {
            spellingProgressRepository.getTotalSpelled()
        }

        confirmVerified(
            dailyProgressRepository,
            vocabularyRepository,
            learnProgressRepository,
            choiceProgressRepository,
            typingProgressRepository,
            spellingProgressRepository
        )
    }

    @Test
    fun getTotalPercentage() = runBlocking {
        val learned = 2717
        val revise = 10
        coEvery {
            learnProgressRepository.getTotalLearned()
        } returns learned
        coEvery {
            wrongRepository.getTotalReviseNum()
        } returns revise
        coEvery {
            vocabularyRepository.getVocabularyByName(expectStudyPlanBeginner.vocabularyName)
        } returns expectVocabularyBeginner

        val remember = (learned - revise).toFloat()
        val notLearned = (expectVocabularyBeginner.size - remember - revise)
        val expect = Triple(
            first = remember,
            second = revise.toFloat(),
            third = notLearned
        )
        val result = useCase.getTotalPercentage(expectStudyPlanBeginner)
        assertEquals(expect, result)

        coVerify(exactly = 1) {
            learnProgressRepository.getTotalLearned()
            wrongRepository.getTotalReviseNum()
            vocabularyRepository.getVocabularyByName(expectStudyPlanBeginner.vocabularyName)
        }

        confirmVerified(
            learnProgressRepository,
            wrongRepository,
            vocabularyRepository
        )
    }

}