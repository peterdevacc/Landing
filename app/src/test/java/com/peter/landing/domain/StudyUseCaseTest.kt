package com.peter.landing.domain

import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.progress.*
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.local.wrong.Wrong
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.pref.SettingPreferencesRepository
import com.peter.landing.data.repository.progress.*
import com.peter.landing.data.repository.vocabulary.VocabularyRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import com.peter.landing.debug.expectVocabularyBeginner
import com.peter.landing.debug.expectWord
import com.peter.landing.domain.study.PlanState
import com.peter.landing.domain.study.StudyProgressState
import com.peter.landing.domain.study.StudyUseCase
import com.peter.landing.util.getTodayDateTime
import com.peter.landing.util.getTomorrowDateTime
import com.peter.landing.util.getYesterdayDateTime
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StudyUseCaseTest {
    private val vocabularyRepository = mockk<VocabularyRepository>(relaxed = true)
    private val vocabularyViewRepository = mockk<VocabularyViewRepository>(relaxed = true)
    private val studyPlanRepository = mockk<StudyPlanRepository>(relaxed = true)
    private val dailyProgressRepository = mockk<DailyProgressRepository>(relaxed = true)
    private val learnProgressRepository = mockk<LearnProgressRepository>(relaxed = true)
    private val choiceProgressRepository = mockk<ChoiceProgressRepository>(relaxed = true)
    private val typingProgressRepository = mockk<TypingProgressRepository>(relaxed = true)
    private val spellingProgressRepository = mockk<SpellingProgressRepository>(relaxed = true)
    private val reviseProgressRepository = mockk<ReviseProgressRepository>(relaxed = true)
    private val wrongRepository = mockk<WrongRepository>(relaxed = true)
    private val settingPreferencesRepository = mockk<SettingPreferencesRepository>(relaxed = true)

    private val useCase = StudyUseCase(
        vocabularyRepository,
        vocabularyViewRepository,
        studyPlanRepository,
        dailyProgressRepository,
        learnProgressRepository,
        choiceProgressRepository,
        typingProgressRepository,
        spellingProgressRepository,
        reviseProgressRepository,
        wrongRepository,
        settingPreferencesRepository
    )

    private val today = getTodayDateTime()

    @Test
    fun `initStudy PlanState-NONE`() = runBlocking {
        coEvery {
            studyPlanRepository.getStudyPlan()
        } returns null

        val result = useCase.initStudy()
        assertEquals(PlanState.NONE, result)

        coVerify(exactly = 1) {
            studyPlanRepository.getStudyPlan()
        }

        confirmVerified(studyPlanRepository)
    }

    @Test
    fun `initStudy PlanState-FINISHED`() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        coEvery {
            studyPlanRepository.getStudyPlan()
        } returns studyPlan
        coEvery {
            spellingProgressRepository.getTotalSpelled()
        } returns expectVocabularyBeginner.size
        coEvery {
            vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
        } returns expectVocabularyBeginner
        coEvery {
            wrongRepository.getTotalReviseNum()
        } returns 0

        val result = useCase.initStudy()
        assertEquals(PlanState.FINISHED, result)

        coVerify(exactly = 1) {
            studyPlanRepository.getStudyPlan()
            spellingProgressRepository.getTotalSpelled()
            wrongRepository.getTotalReviseNum()
            vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
            reviseProgressRepository.removeReviseProgress()
            vocabularyViewRepository.emptyWordListCache()
        }

        confirmVerified(
            studyPlanRepository,
            spellingProgressRepository,
            wrongRepository,
            vocabularyRepository,
            reviseProgressRepository,
            vocabularyViewRepository
        )
    }

    @Test
    fun `initStudy PlanState-COMPLETED_BUT_HAVE_REVISE_REMAIN`() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        coEvery {
            studyPlanRepository.getStudyPlan()
        } returns studyPlan
        coEvery {
            spellingProgressRepository.getTotalSpelled()
        } returns expectVocabularyBeginner.size
        coEvery {
            vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
        } returns expectVocabularyBeginner
        coEvery {
            wrongRepository.getTotalReviseNum()
        } returns 1
        coEvery {
            wrongRepository.getTodayReviseNum()
        } returns 0

        val result = useCase.initStudy()
        assertEquals(PlanState.COMPLETED_BUT_HAVE_REVISE_REMAIN, result)

        coVerify(exactly = 1) {
            studyPlanRepository.getStudyPlan()
            spellingProgressRepository.getTotalSpelled()
            wrongRepository.getTotalReviseNum()
            vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
            wrongRepository.getTodayReviseNum()
        }

        confirmVerified(
            studyPlanRepository,
            spellingProgressRepository,
            wrongRepository,
            vocabularyRepository
        )
    }

    @Test
    fun `initStudy PlanState-REVISE spelled all, TodayReviseProgress exist`() =
        runBlocking {
            val studyPlan = StudyPlan(
                Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
            )

            coEvery {
                studyPlanRepository.getStudyPlan()
            } returns studyPlan
            coEvery {
                spellingProgressRepository.getTotalSpelled()
            } returns expectVocabularyBeginner.size
            coEvery {
                vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
            } returns expectVocabularyBeginner
            coEvery {
                wrongRepository.getTotalReviseNum()
            } returns 1
            coEvery {
                wrongRepository.getTodayReviseNum()
            } returns 1
            coEvery {
                reviseProgressRepository.getTodayReviseProgress()
            } returns ReviseProgress()

            val result = useCase.initStudy()
            assertEquals(PlanState.REVISE, result)

            coVerify(exactly = 1) {
                studyPlanRepository.getStudyPlan()
                spellingProgressRepository.getTotalSpelled()
                wrongRepository.getTotalReviseNum()
                vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
                wrongRepository.getTodayReviseNum()
                reviseProgressRepository.getTodayReviseProgress()
            }

            confirmVerified(
                studyPlanRepository,
                spellingProgressRepository,
                wrongRepository,
                vocabularyRepository,
                reviseProgressRepository
            )
        }

    @Test
    fun `initStudy PlanState-REVISE spelled all, TodayReviseProgress not exist`() =
        runBlocking {
            val studyPlan = StudyPlan(
                Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
            )

            coEvery {
                studyPlanRepository.getStudyPlan()
            } returns studyPlan
            coEvery {
                spellingProgressRepository.getTotalSpelled()
            } returns expectVocabularyBeginner.size
            coEvery {
                vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
            } returns expectVocabularyBeginner
            coEvery {
                wrongRepository.getTotalReviseNum()
            } returns 1
            coEvery {
                wrongRepository.getTodayReviseNum()
            } returns 1
            coEvery {
                reviseProgressRepository.getTodayReviseProgress()
            } returns null

            val result = useCase.initStudy()
            assertEquals(PlanState.REVISE, result)

            coVerify(exactly = 1) {
                studyPlanRepository.getStudyPlan()
                spellingProgressRepository.getTotalSpelled()
                wrongRepository.getTotalReviseNum()
                vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
                wrongRepository.getTodayReviseNum()
                reviseProgressRepository.getTodayReviseProgress()
                reviseProgressRepository.addTodayReviseProgress(ReviseProgress())
            }

            confirmVerified(
                studyPlanRepository,
                spellingProgressRepository,
                wrongRepository,
                vocabularyRepository,
                reviseProgressRepository
            )
        }

    @Test
    fun `initStudy PlanState-REVISE not spelled all, TodayReviseProgress exist, needn't update revise data`() =
        runBlocking {
            val studyPlan = StudyPlan(
                Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
            )

            coEvery {
                studyPlanRepository.getStudyPlan()
            } returns studyPlan
            coEvery {
                spellingProgressRepository.getTotalSpelled()
            } returns (expectVocabularyBeginner.size - studyPlan.wordListSize)
            coEvery {
                vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
            } returns expectVocabularyBeginner
            coEvery {
                dailyProgressRepository.getLastDailyProgress()
            } returns null
            coEvery {
                wrongRepository.getTodayReviseNum()
            } returns 1
            coEvery {
                reviseProgressRepository.getTodayReviseProgress()
            } returns ReviseProgress()

            val result = useCase.initStudy()
            assertEquals(PlanState.REVISE, result)

            coVerify(exactly = 1) {
                studyPlanRepository.getStudyPlan()
                spellingProgressRepository.getTotalSpelled()
                vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
                dailyProgressRepository.getLastDailyProgress()
                wrongRepository.getTodayReviseNum()
                reviseProgressRepository.getTodayReviseProgress()
            }

            confirmVerified(
                studyPlanRepository,
                spellingProgressRepository,
                wrongRepository,
                vocabularyRepository,
                dailyProgressRepository,
                reviseProgressRepository
            )
        }

    @Test
    fun `initStudy PlanState-REVISE not spelled all, TodayReviseProgress not exist, needn't update revise data`() =
        runBlocking {
            val studyPlan = StudyPlan(
                Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
            )

            coEvery {
                studyPlanRepository.getStudyPlan()
            } returns studyPlan
            coEvery {
                spellingProgressRepository.getTotalSpelled()
            } returns (expectVocabularyBeginner.size - studyPlan.wordListSize)
            coEvery {
                vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
            } returns expectVocabularyBeginner
            coEvery {
                dailyProgressRepository.getLastDailyProgress()
            } returns null
            coEvery {
                wrongRepository.getTodayReviseNum()
            } returns 1
            coEvery {
                reviseProgressRepository.getTodayReviseProgress()
            } returns null

            val result = useCase.initStudy()
            assertEquals(PlanState.REVISE, result)

            coVerify(exactly = 1) {
                studyPlanRepository.getStudyPlan()
                spellingProgressRepository.getTotalSpelled()
                vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
                dailyProgressRepository.getLastDailyProgress()
                wrongRepository.getTodayReviseNum()
                reviseProgressRepository.getTodayReviseProgress()
                reviseProgressRepository.addTodayReviseProgress(ReviseProgress())
            }

            confirmVerified(
                studyPlanRepository,
                spellingProgressRepository,
                wrongRepository,
                vocabularyRepository,
                dailyProgressRepository,
                reviseProgressRepository
            )
        }

    @Test
    fun `initStudy PlanState-REVISE need update revise data, gap == 0`() =
        runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )
        val lastDailyProgress = DailyProgress(studyPlan.id, 0)
        lastDailyProgress.isFinished = true
        lastDailyProgress.createDate = getYesterdayDateTime()
        val spellingProgress = SpellingProgress(lastDailyProgress.id)
        spellingProgress.spelled = studyPlan.wordListSize

        coEvery {
            studyPlanRepository.getStudyPlan()
        } returns studyPlan
        coEvery {
            spellingProgressRepository.getTotalSpelled()
        } returns (expectVocabularyBeginner.size - studyPlan.wordListSize)
        coEvery {
            vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
        } returns expectVocabularyBeginner

        coEvery {
            dailyProgressRepository.getLastDailyProgress()
        } returns lastDailyProgress
        coEvery {
            spellingProgressRepository
                .getSpellingProgressByDailyProgressId(lastDailyProgress.id)
        } returns spellingProgress

        coEvery {
            wrongRepository.getTodayReviseNum()
        } returns 1
        coEvery {
            reviseProgressRepository.getTodayReviseProgress()
        } returns ReviseProgress()

        val result = useCase.initStudy()
        assertEquals(PlanState.REVISE, result)

        coVerify(exactly = 1) {
            studyPlanRepository.getStudyPlan()
            spellingProgressRepository.getTotalSpelled()
            vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
            dailyProgressRepository.getLastDailyProgress()
            spellingProgressRepository
                .getSpellingProgressByDailyProgressId(lastDailyProgress.id)
            wrongRepository.getTodayReviseNum()
            reviseProgressRepository.getTodayReviseProgress()
        }

        confirmVerified(
            studyPlanRepository,
            spellingProgressRepository,
            wrongRepository,
            vocabularyRepository,
            dailyProgressRepository,
            reviseProgressRepository
        )
    }

    @Test
    fun `initStudy PlanState-REVISE need update revise data, gap greater than 0`() =
        runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )
        val lastDailyProgress = DailyProgress(studyPlan.id, 0)
        lastDailyProgress.isFinished = true
        lastDailyProgress.createDate = getYesterdayDateTime()
        val spellingProgress = SpellingProgress(lastDailyProgress.id)
        val learnProgress = LearnProgress(lastDailyProgress.id)
        val choiceProgress = ChoiceProgress(lastDailyProgress.id)
        val typingProgress = TypingProgress(lastDailyProgress.id)
        val start = lastDailyProgress.start + spellingProgress.spelled
        val wordList = listOf(expectWord)

        coEvery {
            studyPlanRepository.getStudyPlan()
        } returns studyPlan
        coEvery {
            spellingProgressRepository.getTotalSpelled()
        } returns (expectVocabularyBeginner.size - studyPlan.wordListSize)
        coEvery {
            vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
        } returns expectVocabularyBeginner

        coEvery {
            dailyProgressRepository.getLastDailyProgress()
        } returns lastDailyProgress
        coEvery {
            spellingProgressRepository
                .getSpellingProgressByDailyProgressId(lastDailyProgress.id)
        } returns spellingProgress
        coEvery {
            learnProgressRepository
                .getLearnProgressByDailyProgressId(lastDailyProgress.id)
        } returns learnProgress
        coEvery {
            choiceProgressRepository
                .getChoiceProgressByDailyProgressId(lastDailyProgress.id)
        } returns choiceProgress
        coEvery {
            typingProgressRepository
                .getTypingProgressByDailyProgressId(lastDailyProgress.id)
        } returns typingProgress
        coEvery {
            vocabularyViewRepository
                .getWordList(start, studyPlan.wordListSize, studyPlan.vocabularyName)
        } returns wordList

        coEvery {
            wrongRepository.getTodayReviseNum()
        } returns 1
        coEvery {
            reviseProgressRepository.getTodayReviseProgress()
        } returns ReviseProgress()

        val result = useCase.initStudy()
        assertEquals(PlanState.REVISE, result)

        coVerify(exactly = 1) {
            studyPlanRepository.getStudyPlan()
            spellingProgressRepository.getTotalSpelled()
            vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
            dailyProgressRepository.getLastDailyProgress()
            spellingProgressRepository
                .getSpellingProgressByDailyProgressId(lastDailyProgress.id)
            learnProgressRepository
                .getLearnProgressByDailyProgressId(lastDailyProgress.id)
            choiceProgressRepository
                .getChoiceProgressByDailyProgressId(lastDailyProgress.id)
            typingProgressRepository
                .getTypingProgressByDailyProgressId(lastDailyProgress.id)
            learnProgressRepository.updateLearnProgress(learnProgress)
            choiceProgressRepository.updateChoiceProgress(choiceProgress)
            typingProgressRepository.updateTypingProgress(typingProgress)
            spellingProgressRepository.updateSpellingProgress(spellingProgress)
            vocabularyViewRepository
                .getWordList(start, studyPlan.wordListSize, studyPlan.vocabularyName)
            wrongRepository.addWrongList(
                wordList.map {
                    Wrong(
                        it.id, chosenWrong = true, spelledWrong = true,
                        reviseDate = getTodayDateTime(),
                        addDate = getYesterdayDateTime()
                    )
                }
            )

            wrongRepository.getTodayReviseNum()
            reviseProgressRepository.getTodayReviseProgress()
        }

        confirmVerified(
            studyPlanRepository,
            spellingProgressRepository,
            wrongRepository,
            vocabularyRepository,
            dailyProgressRepository,
            learnProgressRepository,
            choiceProgressRepository,
            typingProgressRepository,
            spellingProgressRepository,
            reviseProgressRepository
        )
    }

    @Test
    fun `initStudy PlanState-LEARN TodayDailyProgress exist`() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )
        val dailyProgress = DailyProgress(studyPlan.id, 0)

        coEvery {
            studyPlanRepository.getStudyPlan()
        } returns studyPlan
        coEvery {
            spellingProgressRepository.getTotalSpelled()
        } returns (expectVocabularyBeginner.size - studyPlan.wordListSize)
        coEvery {
            vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
        } returns expectVocabularyBeginner
        coEvery {
            dailyProgressRepository.getLastDailyProgress()
        } returns null
        coEvery {
            wrongRepository.getTodayReviseNum()
        } returns 0
        coEvery {
            dailyProgressRepository.getTodayDailyProgress()
        } returns dailyProgress

        val result = useCase.initStudy()
        assertEquals(PlanState.LEARN, result)

        coVerify(exactly = 1) {
            studyPlanRepository.getStudyPlan()
            spellingProgressRepository.getTotalSpelled()
            vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
            dailyProgressRepository.getLastDailyProgress()
            wrongRepository.getTodayReviseNum()
            dailyProgressRepository.getTodayDailyProgress()
        }

        confirmVerified(
            studyPlanRepository,
            spellingProgressRepository,
            vocabularyRepository,
            dailyProgressRepository,
            wrongRepository
        )
    }

    @Test
    fun `initStudy PlanState-LEARN TodayDailyProgress not exist`() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )
        val dailyProgress = DailyProgress(studyPlan.id, 0)
        dailyProgress.id = 1

        coEvery {
            studyPlanRepository.getStudyPlan()
        } returns studyPlan
        coEvery {
            spellingProgressRepository.getTotalSpelled()
        } returns (expectVocabularyBeginner.size - studyPlan.wordListSize)
        coEvery {
            vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
        } returns expectVocabularyBeginner
        coEvery {
            dailyProgressRepository.getLastDailyProgress()
        } returns null
        coEvery {
            wrongRepository.getTodayReviseNum()
        } returns 0
        coEvery {
            dailyProgressRepository.getTodayDailyProgress()
        } returnsMany listOf(null, dailyProgress)
        coEvery {
            dailyProgressRepository.getTotalDailyProgressNum()
        } returns 0
        coEvery {
            dailyProgressRepository.getTotalDailyProgressNum()
        } returns 0
        coEvery {
            dailyProgressRepository.addTodayDailyProgress(dailyProgress)
        } returns dailyProgress.id

        val result = useCase.initStudy()
        assertEquals(PlanState.LEARN, result)

        coVerify(exactly = 1) {
            studyPlanRepository.getStudyPlan()
            spellingProgressRepository.getTotalSpelled()
            vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
            dailyProgressRepository.getLastDailyProgress()
            wrongRepository.getTodayReviseNum()
            dailyProgressRepository.getTotalDailyProgressNum()
            dailyProgressRepository.getTodayDailyProgress()
            dailyProgressRepository.addTodayDailyProgress(DailyProgress(studyPlan.id, 0))
            learnProgressRepository.addLearnProgress(LearnProgress(dailyProgress.id))
            choiceProgressRepository.addChoiceProgress(ChoiceProgress(dailyProgress.id))
            typingProgressRepository.addTypingProgress(TypingProgress(dailyProgress.id))
            spellingProgressRepository.addSpellingProgress(SpellingProgress(dailyProgress.id))
        }

        confirmVerified(
            studyPlanRepository,
            spellingProgressRepository,
            vocabularyRepository,
            dailyProgressRepository,
            wrongRepository,
            learnProgressRepository,
            choiceProgressRepository,
            typingProgressRepository
        )
    }

    @Test
    fun `initStudy PlanState-NON_START`() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTomorrowDateTime()
        )
        coEvery {
            studyPlanRepository.getStudyPlan()
        } returns studyPlan
        coEvery {
            spellingProgressRepository.getTotalSpelled()
        } returns 0
        coEvery {
            vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
        } returns expectVocabularyBeginner

        val result = useCase.initStudy()
        assertEquals(PlanState.NON_START, result)

        coVerify(exactly = 1) {
            studyPlanRepository.getStudyPlan()
            spellingProgressRepository.getTotalSpelled()
            vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
        }

        confirmVerified(
            studyPlanRepository,
            spellingProgressRepository,
            vocabularyRepository
        )
    }

    @Test
    fun `getStudyProgressState StudyProgressState-None`() = runBlocking {
        coEvery {
            dailyProgressRepository.getTodayDailyProgress()
        } returns null

        val result = useCase.getStudyProgressState()
        assertEquals(StudyProgressState.None, result)

        coVerify(exactly = 1) {
            dailyProgressRepository.getTodayDailyProgress()
        }

        confirmVerified(
            dailyProgressRepository
        )
    }

    @Test
    fun `getStudyProgressState StudyProgressState-FreshStart`() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        val dailyProgress = DailyProgress(studyPlan.id, 0)
        dailyProgress.id = 1

        val learnProgress = LearnProgress(dailyProgress.id)
        val choiceProgress = ChoiceProgress(dailyProgress.id)
        val typingProgress = TypingProgress(dailyProgress.id)
        val spellingProgress = SpellingProgress(dailyProgress.id)

        studyProgressStateTest(
            studyPlan,
            dailyProgress,
            learnProgress, choiceProgress, typingProgress, spellingProgress,
            StudyProgressState.FreshStart
        )
    }

    @Test
    fun `getStudyProgressState StudyProgressState-Learn`() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        val dailyProgress = DailyProgress(studyPlan.id, 0)
        dailyProgress.id = 1

        val learnProgress = LearnProgress(dailyProgress.id)
        learnProgress.learned = 1

        val choiceProgress = ChoiceProgress(dailyProgress.id)
        val typingProgress = TypingProgress(dailyProgress.id)
        val spellingProgress = SpellingProgress(dailyProgress.id)

        studyProgressStateTest(
            studyPlan,
            dailyProgress,
            learnProgress, choiceProgress, typingProgress, spellingProgress,
            StudyProgressState.Learn
        )
    }

    @Test
    fun `getStudyProgressState StudyProgressState-LearnCompleted`() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        val dailyProgress = DailyProgress(studyPlan.id, 0)
        dailyProgress.id = 1

        val learnProgress = LearnProgress(dailyProgress.id)
        learnProgress.learned = studyPlan.wordListSize

        val choiceProgress = ChoiceProgress(dailyProgress.id)
        val typingProgress = TypingProgress(dailyProgress.id)
        val spellingProgress = SpellingProgress(dailyProgress.id)

        studyProgressStateTest(
            studyPlan,
            dailyProgress,
            learnProgress, choiceProgress, typingProgress, spellingProgress,
            StudyProgressState.LearnCompleted
        )
    }

    @Test
    fun `getStudyProgressState StudyProgressState-Choice`() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        val dailyProgress = DailyProgress(studyPlan.id, 0)
        dailyProgress.id = 1

        val learnProgress = LearnProgress(dailyProgress.id)
        learnProgress.learned = studyPlan.wordListSize

        val choiceProgress = ChoiceProgress(dailyProgress.id)
        choiceProgress.chosen = 1

        val typingProgress = TypingProgress(dailyProgress.id)
        val spellingProgress = SpellingProgress(dailyProgress.id)

        studyProgressStateTest(
            studyPlan,
            dailyProgress,
            learnProgress, choiceProgress, typingProgress, spellingProgress,
            StudyProgressState.Choice
        )
    }

    @Test
    fun `getStudyProgressState StudyProgressState-ChoiceCompleted`() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        val dailyProgress = DailyProgress(studyPlan.id, 0)
        dailyProgress.id = 1

        val learnProgress = LearnProgress(dailyProgress.id)
        learnProgress.learned = studyPlan.wordListSize

        val choiceProgress = ChoiceProgress(dailyProgress.id)
        choiceProgress.chosen = studyPlan.wordListSize

        val typingProgress = TypingProgress(dailyProgress.id)
        val spellingProgress = SpellingProgress(dailyProgress.id)

        studyProgressStateTest(
            studyPlan,
            dailyProgress,
            learnProgress, choiceProgress, typingProgress, spellingProgress,
            StudyProgressState.ChoiceCompleted
        )
    }

    @Test
    fun `getStudyProgressState StudyProgressState-Typing`() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        val dailyProgress = DailyProgress(studyPlan.id, 0)
        dailyProgress.id = 1

        val learnProgress = LearnProgress(dailyProgress.id)
        learnProgress.learned = studyPlan.wordListSize

        val choiceProgress = ChoiceProgress(dailyProgress.id)
        choiceProgress.chosen = studyPlan.wordListSize

        val typingProgress = TypingProgress(dailyProgress.id)
        typingProgress.typed = 1

        val spellingProgress = SpellingProgress(dailyProgress.id)

        studyProgressStateTest(
            studyPlan,
            dailyProgress,
            learnProgress, choiceProgress, typingProgress, spellingProgress,
            StudyProgressState.Typing
        )
    }

    @Test
    fun `getStudyProgressState StudyProgressState-TypingCompleted`() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        val dailyProgress = DailyProgress(studyPlan.id, 0)
        dailyProgress.id = 1

        val learnProgress = LearnProgress(dailyProgress.id)
        learnProgress.learned = studyPlan.wordListSize

        val choiceProgress = ChoiceProgress(dailyProgress.id)
        choiceProgress.chosen = studyPlan.wordListSize

        val typingProgress = TypingProgress(dailyProgress.id)
        typingProgress.typed = studyPlan.wordListSize

        val spellingProgress = SpellingProgress(dailyProgress.id)

        studyProgressStateTest(
            studyPlan,
            dailyProgress,
            learnProgress, choiceProgress, typingProgress, spellingProgress,
            StudyProgressState.TypingCompleted
        )
    }

    @Test
    fun `getStudyProgressState StudyProgressState-Spelling`() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        val dailyProgress = DailyProgress(studyPlan.id, 0)
        dailyProgress.id = 1

        val learnProgress = LearnProgress(dailyProgress.id)
        learnProgress.learned = studyPlan.wordListSize

        val choiceProgress = ChoiceProgress(dailyProgress.id)
        choiceProgress.chosen = studyPlan.wordListSize

        val typingProgress = TypingProgress(dailyProgress.id)
        typingProgress.typed = studyPlan.wordListSize

        val spellingProgress = SpellingProgress(dailyProgress.id)
        spellingProgress.spelled = 1

        studyProgressStateTest(
            studyPlan,
            dailyProgress,
            learnProgress, choiceProgress, typingProgress, spellingProgress,
            StudyProgressState.Spelling
        )
    }

    @Test
    fun `getStudyProgressState StudyProgressState-SpellingCompleted`() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        val dailyProgress = DailyProgress(studyPlan.id, 0)
        dailyProgress.id = 1

        val learnProgress = LearnProgress(dailyProgress.id)
        learnProgress.learned = studyPlan.wordListSize

        val choiceProgress = ChoiceProgress(dailyProgress.id)
        choiceProgress.chosen = studyPlan.wordListSize

        val typingProgress = TypingProgress(dailyProgress.id)
        typingProgress.typed = studyPlan.wordListSize

        val spellingProgress = SpellingProgress(dailyProgress.id)
        spellingProgress.spelled = studyPlan.wordListSize

        studyProgressStateTest(
            studyPlan,
            dailyProgress,
            learnProgress, choiceProgress, typingProgress, spellingProgress,
            StudyProgressState.SpellingCompleted
        )
    }

    @Test
    fun `getStudyProgressState StudyProgressState-TodayFinished`() = runBlocking {
        val studyPlan = StudyPlan(
            Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
        )

        val dailyProgress = DailyProgress(studyPlan.id, 0)
        dailyProgress.id = 1
        dailyProgress.isFinished = true

        val learnProgress = LearnProgress(dailyProgress.id)
        learnProgress.learned = studyPlan.wordListSize

        val choiceProgress = ChoiceProgress(dailyProgress.id)
        choiceProgress.chosen = studyPlan.wordListSize

        val typingProgress = TypingProgress(dailyProgress.id)
        typingProgress.typed = studyPlan.wordListSize

        val spellingProgress = SpellingProgress(dailyProgress.id)
        spellingProgress.spelled = studyPlan.wordListSize

        studyProgressStateTest(
            studyPlan,
            dailyProgress,
            learnProgress, choiceProgress, typingProgress, spellingProgress,
            StudyProgressState.TodayFinished
        )
    }

    @Test
    fun refreshDate() {
        useCase.refreshDate()

        verify(exactly = 1) {
            dailyProgressRepository.refreshDateTime(today)
            reviseProgressRepository.refreshDateTime(today)
            wrongRepository.refreshDateTime(today)
        }

        confirmVerified(
            dailyProgressRepository,
            reviseProgressRepository,
            wrongRepository
        )
    }

    private fun studyProgressStateTest(
        studyPlan: StudyPlan,
        dailyProgress: DailyProgress,
        learnProgress: LearnProgress,
        choiceProgress: ChoiceProgress,
        typingProgress: TypingProgress,
        spellingProgress: SpellingProgress,
        studyProgressState: StudyProgressState
    ) = runBlocking {
        prepareStudyProgressStateTest(
            studyPlan,
            dailyProgress, learnProgress, choiceProgress, typingProgress, spellingProgress
        )

        val result = useCase.getStudyProgressState()
        assertEquals(studyProgressState, result)

        verifyStudyProgressStateTest(studyPlan, dailyProgress)

        confirmVerifiedStudyProgressStateTest()
    }

    private fun prepareStudyProgressStateTest(
        studyPlan: StudyPlan,
        dailyProgress: DailyProgress,
        learnProgress: LearnProgress,
        choiceProgress: ChoiceProgress,
        typingProgress: TypingProgress,
        spellingProgress: SpellingProgress
    ) {
        coEvery {
            studyPlanRepository.getStudyPlan()
        } returns studyPlan

        coEvery {
            vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
        } returns expectVocabularyBeginner

        val dailyProgressId = dailyProgress.id
        coEvery {
            dailyProgressRepository.getTodayDailyProgress()
        } returns dailyProgress

        coEvery {
            learnProgressRepository.getLearnProgressByDailyProgressId(dailyProgressId)
        } returns learnProgress

        coEvery {
            choiceProgressRepository.getChoiceProgressByDailyProgressId(dailyProgressId)
        } returns choiceProgress

        coEvery {
            typingProgressRepository.getTypingProgressByDailyProgressId(dailyProgressId)
        } returns typingProgress

        coEvery {
            spellingProgressRepository.getSpellingProgressByDailyProgressId(dailyProgressId)
        } returns spellingProgress
    }

    private fun verifyStudyProgressStateTest(
        studyPlan: StudyPlan,
        dailyProgress: DailyProgress
    ) {
        val dailyProgressId = dailyProgress.id

        coVerify(exactly = 1) {
            studyPlanRepository.getStudyPlan()
            vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
            dailyProgressRepository.getTodayDailyProgress()
            learnProgressRepository.getLearnProgressByDailyProgressId(dailyProgressId)
            choiceProgressRepository.getChoiceProgressByDailyProgressId(dailyProgressId)
            typingProgressRepository.getTypingProgressByDailyProgressId(dailyProgressId)
            spellingProgressRepository.getSpellingProgressByDailyProgressId(dailyProgressId)
        }
    }

    private fun confirmVerifiedStudyProgressStateTest() {
        confirmVerified(
            studyPlanRepository,
            vocabularyRepository,
            dailyProgressRepository,
            learnProgressRepository,
            choiceProgressRepository,
            typingProgressRepository,
            spellingProgressRepository
        )
    }

}