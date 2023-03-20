package com.peter.landing.ui.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.progress.StudyProgress
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.pref.PreferencesRepository
import com.peter.landing.data.repository.progress.StudyProgressRepository
import com.peter.landing.data.util.DataResult
import com.peter.landing.data.util.ThemeMode
import com.peter.landing.util.getTodayDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val studyPlanRepository: StudyPlanRepository,
    private val studyProgressRepository: StudyProgressRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val homeUiState: MutableState<HomeUiState> = mutableStateOf(HomeUiState.Loading)
    val uiState: State<HomeUiState> = homeUiState

    fun setThemeMode(themeMode: ThemeMode) {
        val state = homeUiState.value
        if (state is HomeUiState.Success) {
            viewModelScope.launch {
                val result = preferencesRepository.setTheme(themeMode)
                if (result is DataResult.Error) {
                    homeUiState.value = HomeUiState.Error(
                        code = result.code
                    )
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            try {

                val planFlow = studyPlanRepository.getStudyPlanFlow()
                val progressFlow = studyProgressRepository.getStudyProgressLatestFlow()
                planFlow.combine(progressFlow) { plan: StudyPlan?, progress: StudyProgress? ->
                    plan to progress
                }.collectLatest { (plan, latestProgress) ->
                    if (plan != null) {
                        if (plan.finished) {
                            homeUiState.value = HomeUiState.Success(
                                studyState = StudyState.PlanFinished
                            )
                        } else {
                            if (latestProgress != null) {
                                if (latestProgress.finishedDate == null) {
                                    homeUiState.value = HomeUiState.Success(
                                        studyState = StudyState.Learning(
                                            latestProgress.progressState
                                        )
                                    )
                                } else {
                                    if (latestProgress.finishedDate == getTodayDateTime()) {
                                        homeUiState.value = HomeUiState.Success(
                                            studyState = StudyState.Learning(
                                                latestProgress.progressState
                                            )
                                        )
                                    } else {
                                        val currentProgress = StudyProgress(
                                            id = latestProgress.id + 1L,
                                            vocabularyName = plan.vocabularyName,
                                            start = latestProgress.start + plan.wordListSize,
                                            wordListSize = plan.wordListSize
                                        )
                                        studyProgressRepository.insertStudyProgress(
                                            currentProgress
                                        )

                                        homeUiState.value = HomeUiState.Success(
                                            studyState = StudyState.Learning(
                                                currentProgress.progressState
                                            )
                                        )
                                    }
                                }
                            } else {
                                val firstProgress = StudyProgress(
                                    id = 1,
                                    vocabularyName = plan.vocabularyName,
                                    start = 0,
                                    wordListSize = plan.wordListSize
                                )
                                studyProgressRepository.insertStudyProgress(
                                    firstProgress
                                )

                                homeUiState.value = HomeUiState.Success(
                                    studyState = StudyState.Learning(
                                        firstProgress.progressState
                                    )
                                )
                            }
                        }

                    } else {
                        homeUiState.value = HomeUiState.Success(
                            studyState = StudyState.None
                        )
                    }
                }

            } catch (exception: Exception) {
                homeUiState.value = HomeUiState.Error(
                    DataResult.Error.Code.UNKNOWN
                )
            }
        }
    }

}