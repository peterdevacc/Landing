package com.peter.landing.ui.study.learn

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.progress.ProgressState
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.repository.progress.StudyProgressRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import com.peter.landing.data.util.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearnViewModel @Inject constructor(
    private val vocabularyViewRepository: VocabularyViewRepository,
    private val studyProgressRepository: StudyProgressRepository
) : ViewModel() {

    private val learnUiState: MutableState<LearnUiState> =
        mutableStateOf(LearnUiState.Loading)
    val uiState: State<LearnUiState> = learnUiState

    private var currentWordList = emptyList<Word>()

    fun submit() {
        val state = learnUiState.value
        if (state is LearnUiState.Success) {
            learnUiState.value = state.copy(
                dialog = LearnUiState.Success.Dialog.Processing
            )

            viewModelScope.launch {
                val progress = studyProgressRepository.getStudyProgressLatest()
                if (progress != null) {
                    val correct = state.input == state.word.spelling
                    val notLearned = state.current == progress.learned
                    if (correct) {
                        if (notLearned) {
                            progress.learned += 1
                            studyProgressRepository.updateStudyProgress(progress)
                        }

                        if (progress.learned == progress.wordListSize) {
                            progress.progressState = ProgressState.CHOICE
                            studyProgressRepository.updateStudyProgress(progress)

                            learnUiState.value = state.copy(
                                submitted = true,
                                correct = true,
                                learned = true,
                                finished = true,
                                dialog = LearnUiState.Success.Dialog.None
                            )
                        } else {
                            learnUiState.value = state.copy(
                                submitted = true,
                                correct = true,
                                learned = true,
                                dialog = LearnUiState.Success.Dialog.None
                            )
                        }

                    } else {
                        learnUiState.value = state.copy(
                            submitted = true,
                            correct = false,
                            dialog = LearnUiState.Success.Dialog.None
                        )
                    }

                } else {
                    learnUiState.value = LearnUiState
                        .Error(DataResult.Error.Code.UNKNOWN)
                }
            }
        }
    }

    fun reset() {
        val state = learnUiState.value
        if (state is LearnUiState.Success) {
            learnUiState.value = state.copy(
                input = "",
                submitted = false
            )
        }
    }

    fun write(alphabet: Char) {
        val state = learnUiState.value
        if (state is LearnUiState.Success) {
            if (state.input.length < state.word.spelling.length) {
                var updated = state.input

                if (updated == "") {
                    updated = alphabet.toString()
                } else {
                    updated += alphabet
                }

                learnUiState.value = state.copy(
                    input = updated
                )
            }
        }
    }

    fun remove() {
        val state = learnUiState.value
        if (state is LearnUiState.Success) {
            var updated = state.input

            updated = if (updated != "") {
                updated.dropLast(1)
            } else {
                ""
            }

            learnUiState.value = state.copy(
                input = updated
            )
        }
    }

    fun getNextWord() {
        val state = learnUiState.value
        if (state is LearnUiState.Success) {
            if (state.current < state.totalNum - 1) {
                learnUiState.value = state.copy(
                    dialog = LearnUiState.Success.Dialog.Processing
                )

                viewModelScope.launch {
                    val progress = studyProgressRepository.getStudyProgressLatest()
                    if (progress != null) {

                        val currentWord = currentWordList[state.current + 1]
                        val quiz = getQuiz(currentWord.spelling)

                        learnUiState.value = state.copy(
                            current = state.current + 1,
                            word = currentWord,
                            quiz = quiz,
                            input = "",
                            correct = false,
                            submitted = false,
                            learned = false,
                            dialog = LearnUiState.Success.Dialog.None
                        )
                    } else {
                        learnUiState.value = LearnUiState
                            .Error(DataResult.Error.Code.UNKNOWN)
                    }
                }

            }
        }
    }

    private fun getQuiz(spelling: String): String {
        var quiz = ""
        val shuffled = spelling.toMutableList().shuffled()
        shuffled.forEach {
            quiz += it
        }
        return quiz
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
                currentWordList = wordList
                val word = currentWordList[progress.learned]

                learnUiState.value = LearnUiState.Success(
                    current = progress.learned,
                    word = word,
                    quiz = getQuiz(word.spelling),
                    totalNum = progress.wordListSize
                )
            } else {
                learnUiState.value = LearnUiState
                    .Error(DataResult.Error.Code.UNKNOWN)
            }

        }
    }

}