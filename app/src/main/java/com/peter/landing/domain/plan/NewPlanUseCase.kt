package com.peter.landing.domain.plan

import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.vocabulary.VocabularyRepository
import com.peter.landing.util.getEndDate
import com.peter.landing.util.getTodayDateTime
import com.peter.landing.util.getTomorrowDateTime
import java.util.*
import javax.inject.Inject

class NewPlanUseCase @Inject constructor(
    private val vocabularyRepository: VocabularyRepository,
    private val planRepository: StudyPlanRepository
) {

    private var isToday = true
    private val plan = StudyPlan()
    private var vocabulary = Vocabulary()
    private lateinit var vocabularyList: List<Vocabulary>

    suspend fun addPlan() = planRepository.addStudyPlan(plan)

    suspend fun initVocabularyList() {
        vocabularyList = vocabularyRepository.getVocabularyList()
    }

    fun getVocabularyList() = vocabularyList

    fun setPlanStartToday(isToday: Boolean) {
        this.isToday = isToday
    }

    fun setPlanStartDate() {
        if (isToday) {
            plan.startDate = getTodayDateTime()
        } else {
            plan.startDate = getTomorrowDateTime()
        }
    }

    fun setPlanVocabulary(position: Int) {
        this.vocabulary = vocabularyList[position]
        plan.vocabularyName = vocabularyList[position].name
    }

    fun setPlanWordListSize(wordListSize: Int) {
        plan.wordListSize = wordListSize
    }

    fun isPlanVocabularyExist(): Boolean {
        return this.vocabulary.name == Vocabulary.Name.NONE
    }

    fun getPlanVocabularyName(): String {
        return this.vocabulary.name.cnValue
    }

    fun getPlanWordListSize() = plan.wordListSize

    fun getPlanEndDate(): String {
        return if (plan.startDate != null &&
            plan.vocabularyName != Vocabulary.Name.NONE &&
            plan.wordListSize > 0
        ) {
            val startDate = plan.startDate!!.clone() as Calendar
            getEndDate(
                this.vocabulary.size,
                startDate,
                plan.wordListSize
            )
        } else {
            ""
        }
    }

}