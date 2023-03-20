package com.peter.landing.ui.plan

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.progress.StudyProgressRepository
import com.peter.landing.data.repository.vocabulary.VocabularyRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import com.peter.landing.util.calculateEndDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PlanViewModel @Inject constructor(
    private val vocabularyRepository: VocabularyRepository,
    private val planRepository: StudyPlanRepository,
    private val vocabularyViewRepository: VocabularyViewRepository,
    private val wrongRepository: WrongRepository,
    private val progressRepository: StudyProgressRepository
): ViewModel() {

    private val planUiState: MutableState<PlanUiState> = mutableStateOf(PlanUiState.Loading)
    val uiState: State<PlanUiState> = planUiState

    fun updateNewPlanVocabulary(
        vocabulary: Vocabulary
    ) {
        val state = planUiState.value
        if (state is PlanUiState.Empty) {
            planUiState.value = state.copy(
                vocabulary = vocabulary,
                endDate = getEndDate(
                    state.startDate, vocabulary.size, state.wordListSize
                )
            )
        }
    }

    fun updateNewPlanWordListSize(
        wordListSize: Int,
    ) {
        val state = planUiState.value
        if (state is PlanUiState.Empty) {
            planUiState.value = state.copy(
                wordListSize = wordListSize,
                endDate = getEndDate(
                    state.startDate, state.vocabulary.size, wordListSize
                )
            )
        }
    }

    fun updateNewPlanStartDate(
        startDate: Calendar? = null
    ) {
        val state = planUiState.value
        if (state is PlanUiState.Empty) {
            planUiState.value = state.copy(
                startDate = startDate,
                endDate = getEndDate(
                    startDate, state.vocabulary.size, state.wordListSize
                )
            )
        }
    }

    fun completeNewPlan() {
        val state = planUiState.value
        if (state is PlanUiState.Empty) {
            planUiState.value = PlanUiState.Loading
            viewModelScope.launch {
                val studyPlan = StudyPlan(
                    vocabularyName = state.vocabulary.name,
                    vocabularySize = state.vocabulary.size,
                    wordListSize = state.wordListSize,
                    startDate = state.startDate
                )
                planRepository.upsertStudyPlan(studyPlan)
                planUiState.value = PlanUiState.Existed(
                    studyPlan = studyPlan,
                    progressReport = progressRepository.getLatestLessonReport(
                        wordListSize = studyPlan.wordListSize
                    ),
                    totalReport = progressRepository.getTotalReport(
                        vocabularySize = studyPlan.vocabularySize
                    )
                )
            }

        }
    }

    fun deletePlan() {
        val state = planUiState.value
        if (state is PlanUiState.Existed) {
            planUiState.value = PlanUiState.Loading
            viewModelScope.launch {
                vocabularyViewRepository.emptyWordListCache()
                planRepository.removeStudyPlan()
                progressRepository.removeStudyProgress()
                wrongRepository.removeWrong()
                planUiState.value = PlanUiState.Empty()
            }
        }
    }

    fun openNewPlanDialog() {
        val state = planUiState.value
        if (state is PlanUiState.Empty) {
            viewModelScope.launch {
                val vocabularyList = vocabularyRepository.getVocabularyList()
                planUiState.value = state.copy(
                    vocabularyList = vocabularyList,
                    dialog = PlanUiState.Empty.Dialog.NewPlan
                )
            }
        }
    }

    fun closeNewPlanDialog() {
        val state = planUiState.value
        if (state is PlanUiState.Empty) {
            planUiState.value = state.copy(
                dialog = PlanUiState.Empty.Dialog.None,
                vocabulary = Vocabulary(),
                wordListSize = 0,
                startDate = null,
                endDate = "",
            )
        }
    }

    fun openDeleteDialog() {
        val state = planUiState.value
        if (state is PlanUiState.Existed) {
            planUiState.value = state.copy(
                dialog = PlanUiState.Existed.Dialog.DeletePlan,
            )
        }
    }

    fun closeDeleteDialog() {
        val state = planUiState.value
        if (state is PlanUiState.Existed) {
            planUiState.value = state.copy(
                dialog = PlanUiState.Existed.Dialog.None,
            )
        }
    }

    private fun getEndDate(
        startDate: Calendar?, vocabularySize: Int, wordListSize: Int
    ): String {
        if (startDate != null && vocabularySize != 0 && wordListSize != 0) {
            return calculateEndDate(vocabularySize, startDate, wordListSize)
        }

        return ""
    }

    init {

        val planFlow = planRepository.getStudyPlanFlow()
        viewModelScope.launch {
            planFlow.collectLatest { plan ->
                if (plan == null) {
                    planUiState.value = PlanUiState.Empty()
                } else {
                    planUiState.value = PlanUiState.Existed(
                        studyPlan = plan,
                        progressReport = progressRepository.getLatestLessonReport(
                            wordListSize = plan.wordListSize
                        ),
                        totalReport = progressRepository.getTotalReport(
                            vocabularySize = plan.vocabularySize
                        )
                    )
                }
            }
        }

    }

}