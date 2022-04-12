package com.peter.landing.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.domain.study.StudyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: StudyUseCase
) : ViewModel() {

    suspend fun initStudy() =
        useCase.initStudy()

    suspend fun getProgressState() =
        useCase.getStudyProgressState()

    suspend fun getCurrentTheme() = useCase
        .currentThemeData.first()

    fun saveCurrentTheme(theme: String) =
        useCase.saveCurrentTheme(theme, viewModelScope)

    suspend fun getCurrentStudyState() = useCase
        .currentStudyStateData.first()

    fun refreshDate() =
        useCase.refreshDate()

}
