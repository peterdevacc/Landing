package com.peter.landing.domain.plan

import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.progress.*
import com.peter.landing.data.repository.vocabulary.VocabularyRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import com.peter.landing.util.getEndDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlanUseCase @Inject constructor(
    private val vocabularyRepository: VocabularyRepository,
    private val planRepository: StudyPlanRepository,
    private val vocabularyViewRepository: VocabularyViewRepository,
    private val dailyProgressRepository: DailyProgressRepository,
    private val learnProgressRepository: LearnProgressRepository,
    private val choiceProgressRepository: ChoiceProgressRepository,
    private val typingProgressRepository: TypingProgressRepository,
    private val spellingProgressRepository: SpellingProgressRepository,
    private val reviseProgressRepository: ReviseProgressRepository,
    private val wrongRepository: WrongRepository,
) {

    fun getStudyPlanFlow() =
        planRepository.getStudyPlanFlow()

    suspend fun getPlanEndDate(plan: StudyPlan): String {
        val vocabulary = vocabularyRepository.getVocabularyByName(plan.vocabularyName)
        return getEndDate(vocabulary.size, plan.startDate!!, plan.wordListSize)
    }

    fun deletePlan(scope: CoroutineScope) = scope.launch {
        vocabularyViewRepository.emptyWordListCache()
        reviseProgressRepository.removeReviseProgress()
        planRepository.removeStudyPlan()
        wrongRepository.removeWrong()
    }

    fun getPlanVocabularyStrName(plan: StudyPlan): String {
        return when (plan.vocabularyName) {
            Vocabulary.Name.BEGINNER -> Vocabulary.Name.BEGINNER.cnValue
            Vocabulary.Name.INTERMEDIATE -> Vocabulary.Name.INTERMEDIATE.cnValue
            Vocabulary.Name.NONE -> Vocabulary.Name.NONE.cnValue
        }
    }

    suspend fun getDailyPercentage(studyPlan: StudyPlan): List<Float>? {
        val progress = dailyProgressRepository.getTodayDailyProgress()
        val vocabulary = vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
        progress?.let {
            val learnProgress = learnProgressRepository
                .getLearnProgressByDailyProgressId(it.id)
            val choiceProgress = choiceProgressRepository
                .getChoiceProgressByDailyProgressId(it.id)
            val typingProgress = typingProgressRepository
                .getTypingProgressByDailyProgressId(it.id)
            val spellingProgress = spellingProgressRepository
                .getSpellingProgressByDailyProgressId(it.id)
            val remain = vocabulary.size - it.start
            val wordListSize = if (remain < studyPlan.wordListSize) {
                remain
            } else {
                studyPlan.wordListSize
            }
            val learned = learnProgress.learned.toFloat()
            val chosen = choiceProgress.chosen.toFloat()
            val typed = typingProgress.typed.toFloat()
            val spelled = spellingProgress.spelled.toFloat()

            val learnedPercentage = String.format(
                "%.1f", ((learned / wordListSize) * 100)
            ).toFloat()
            val chosenPercentage = String.format(
                "%.1f", ((chosen / wordListSize) * 100)
            ).toFloat()
            val typedPercentage = String.format(
                "%.1f", ((typed / wordListSize) * 100)
            ).toFloat()
            val spelledPercentage = String.format(
                "%.1f", ((spelled / wordListSize) * 100)
            ).toFloat()

            return listOf(
                learnedPercentage,
                chosenPercentage,
                typedPercentage,
                spelledPercentage
            )
        }

        val spelledSum = spellingProgressRepository.getTotalSpelled()
        if (spelledSum != vocabulary.size) {
            return listOf(
                0f, 0f, 0f, 0f
            )
        }

        return null
    }

    suspend fun getTotalPercentage(studyPlan: StudyPlan): Triple<Float, Float, Float> {
        val learned = learnProgressRepository.getTotalLearned().toFloat()
        val revise = wrongRepository.getTotalReviseNum().toFloat()
        val vocabulary = vocabularyRepository.getVocabularyByName(studyPlan.vocabularyName)
        val size = vocabulary.size
        val remember = if (learned > revise) {
            learned - revise
        } else {
            0f
        }
        val notLearned = size - remember - revise
        return Triple(
            first = remember,
            second = revise,
            third = notLearned
        )
    }

}