package com.peter.landing.ui.study.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.progress.ProgressState
import com.peter.landing.data.repository.progress.StudyProgressRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import com.peter.landing.data.util.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordListViewModel @Inject constructor(
    private val vocabularyViewRepository: VocabularyViewRepository,
    private val studyProgressRepository: StudyProgressRepository
): ViewModel() {

    private val wordListUiState: MutableState<WordListUiState> =
        mutableStateOf(WordListUiState.Loading)
    val uiState: State<WordListUiState> = wordListUiState

    fun startLearning() {
        val state = wordListUiState.value
        if (state is WordListUiState.Success) {
            viewModelScope.launch {
                val currentLesson = studyProgressRepository.getStudyProgressLatest()
                if (currentLesson != null) {
                    currentLesson.progressState = ProgressState.LEARN
                    studyProgressRepository.updateStudyProgress(currentLesson)

                    wordListUiState.value = state.copy(
                        start = true
                    )
                } else {
                    wordListUiState.value = WordListUiState
                        .Error(DataResult.Error.Code.UNKNOWN)
                }
            }
        }
    }

    init {
        viewModelScope.launch {

            val progress = studyProgressRepository.getStudyProgressLatest()
            if (progress != null) {
                val wordList = vocabularyViewRepository.getWordList(
                    start = progress.start,
                    wordListSize = progress.wordListSize,
                    vocabularyName = progress.vocabularyName
                )
                wordListUiState.value = WordListUiState
                    .Success(
                        wordList = wordList
                    )
            } else {
                wordListUiState.value = WordListUiState
                    .Error(DataResult.Error.Code.UNKNOWN)
            }

        }
    }

}