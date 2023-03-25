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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    private val planFlow = planRepository.getStudyPlanFlow()
    private val planDialogUiState: MutableState<PlanDialogUiState> =
        mutableStateOf(PlanDialogUiState.None)
    val dialogUiState: State<PlanDialogUiState> = planDialogUiState

    val planUiState = planFlow.map { plan ->
        if (plan == null) {
            PlanUiState.Empty
        } else {
            PlanUiState.Existed(
                studyPlan = plan,
                progressReport = progressRepository.getLatestLessonReport(
                    wordListSize = plan.wordListSize
                ),
                totalReport = progressRepository.getTotalReport(
                    vocabularySize = plan.vocabularySize
                )
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2_000),
        initialValue = PlanUiState.Loading
    )

    fun updateNewPlanVocabulary(vocabulary: Vocabulary) {
        val state = planDialogUiState.value
        if (state is PlanDialogUiState.NewPlan) {
            planDialogUiState.value = state.copy(
                vocabulary = vocabulary,
                endDate = getEndDate(
                    state.startDate, vocabulary.size, state.wordListSize
                )
            )
        }
    }

    fun updateNewPlanWordListSize(wordListSize: Int) {
        val state = planDialogUiState.value
        if (state is PlanDialogUiState.NewPlan) {
            planDialogUiState.value = state.copy(
                wordListSize = wordListSize,
                endDate = getEndDate(
                    state.startDate, state.vocabulary.size, wordListSize
                )
            )
        }
    }

    fun updateNewPlanStartDate(startDate: Calendar? = null) {
        val state = planDialogUiState.value
        if (state is PlanDialogUiState.NewPlan) {
            planDialogUiState.value = state.copy(
                startDate = startDate,
                endDate = getEndDate(
                    startDate, state.vocabulary.size, state.wordListSize
                )
            )
        }
    }

    fun completeNewPlan() {
        val state = planDialogUiState.value
        if (state is PlanDialogUiState.NewPlan) {
            planDialogUiState.value = PlanDialogUiState.Processing
            viewModelScope.launch {
                val studyPlan = StudyPlan(
                    vocabularyName = state.vocabulary.name,
                    vocabularySize = state.vocabulary.size,
                    wordListSize = state.wordListSize,
                    startDate = state.startDate
                )
                planRepository.upsertStudyPlan(studyPlan)
                planDialogUiState.value = PlanDialogUiState.None
            }

        }
    }

    fun deletePlan() {
        val state = planUiState.value
        if (state is PlanUiState.Existed) {
            planDialogUiState.value = PlanDialogUiState.Processing
            viewModelScope.launch {
                vocabularyViewRepository.emptyWordListCache()
                planRepository.removeStudyPlan()
                progressRepository.removeStudyProgress()
                wrongRepository.removeWrong()
                planDialogUiState.value = PlanDialogUiState.None
            }
        }
    }

    fun openNewPlanDialog() {
        val state = planUiState.value
        if (state is PlanUiState.Empty) {
            viewModelScope.launch {
                val vocabularyList = vocabularyRepository.getVocabularyList()
                planDialogUiState.value = PlanDialogUiState.NewPlan(
                    vocabularyList = vocabularyList
                )
            }
        }
    }

    fun closeNewPlanDialog() {
        val state = planDialogUiState.value
        if (state is PlanDialogUiState.NewPlan) {
            planDialogUiState.value = PlanDialogUiState.None
        }
    }

    fun openDeleteDialog() {
        val state = planUiState.value
        if (state is PlanUiState.Existed) {
            planDialogUiState.value = PlanDialogUiState.DeletePlan
        }
    }

    fun closeDeleteDialog() {
        val state = planDialogUiState.value
        if (state is PlanDialogUiState.DeletePlan) {
            planDialogUiState.value = PlanDialogUiState.None
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

}