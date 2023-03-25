package com.peter.landing.ui.home

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    studyPlanRepository: StudyPlanRepository,
    private val studyProgressRepository: StudyProgressRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val today = getTodayDateTime()
    private val planFlow = studyPlanRepository.getStudyPlanFlow()
    private val progressFlow = studyProgressRepository.getStudyProgressLatestFlow()
    private val dateFlow = flow {
        delay(1_000)
        while (true) {
            val date = getTodayDateTime()
            if (today != date) {
                emit(date)
            } else {
                emit(today)
            }
            delay(1_000)
        }
    }.distinctUntilChanged()

    private val errorFlow = MutableStateFlow<DataResult.Error?>(null)

    val homeUiState = planFlow
        .combine(progressFlow) { plan: StudyPlan?, progress: StudyProgress? ->
            plan to progress
        }.combine(dateFlow) { studyData, date ->
            studyData to date
        }.combine(errorFlow) { data, hasError ->
            data to hasError
        }.map { (data, hasError) ->
            if (hasError == null) {
                val plan = data.first.first
                val latestProgress = data.first.second
                if (plan != null) {
                    if (plan.finished) {
                        HomeUiState.Success(StudyState.PlanFinished)
                    } else {
                        if (latestProgress != null) {
                            if (latestProgress.finishedDate == null) {
                                HomeUiState.Success(
                                    StudyState.Learning(latestProgress.progressState)
                                )
                            } else {
                                if (latestProgress.finishedDate == data.second) {
                                    HomeUiState.Success(
                                        StudyState.Learning(latestProgress.progressState)
                                    )
                                } else {
                                    val currentProgress = StudyProgress(
                                        id = latestProgress.id + 1L,
                                        vocabularyName = plan.vocabularyName,
                                        start = latestProgress.start + plan.wordListSize,
                                        wordListSize = plan.wordListSize
                                    )
                                    studyProgressRepository.insertStudyProgress(currentProgress)

                                    HomeUiState.Success(
                                        StudyState.Learning(currentProgress.progressState)
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
                            studyProgressRepository.insertStudyProgress(firstProgress)

                            HomeUiState.Success(StudyState.Learning(firstProgress.progressState))
                        }
                    }

                } else {
                    HomeUiState.Success(StudyState.None)
                }
            } else {
                HomeUiState.Error(
                    code = hasError.code
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000, 0),
            initialValue = HomeUiState.Loading,
        )

    fun setThemeMode(themeMode: ThemeMode) {
        val state = homeUiState.value
        if (state is HomeUiState.Success) {

            viewModelScope.launch {
                val result = preferencesRepository.setTheme(themeMode)
                if (result is DataResult.Error) {
                    errorFlow.value = result
                }
            }
        }
    }

}