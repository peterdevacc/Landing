package com.peter.landing.ui.home.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.domain.study.WordListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WordListViewModel @Inject constructor(
    private val useCase: WordListUseCase
) : ViewModel() {

    suspend fun getWordList() =
        useCase.getWordList()

    fun setStudyState(studyState: Boolean) {
        useCase.setStudyState(studyState, viewModelScope)
    }

    init {
        useCase.initWordList(viewModelScope)
    }

}