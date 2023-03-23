package com.peter.landing.ui.study.spelling

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.progress.ProgressState
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.repository.plan.StudyPlanRepository
import com.peter.landing.data.repository.progress.StudyProgressRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import com.peter.landing.data.util.DataResult
import com.peter.landing.util.getTodayDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpellingViewModel @Inject constructor(
    private val vocabularyViewRepository: VocabularyViewRepository,
    private val studyPlanRepository: StudyPlanRepository,
    private val studyProgressRepository: StudyProgressRepository,
    private val wrongRepository: WrongRepository
) : ViewModel() {

    private val spellingUiState: MutableState<SpellingUiState> =
        mutableStateOf(SpellingUiState.Loading)
    val uiState: State<SpellingUiState> = spellingUiState

    private var currentWordList = emptyList<Word>()

    fun submit() {
        val state = spellingUiState.value
        if (state is SpellingUiState.Success) {
            spellingUiState.value = state.copy(
                dialog = SpellingUiState.Success.Dialog.Processing
            )

            viewModelScope.launch {
                val plan = studyPlanRepository.getStudyPlan()
                val progress = studyProgressRepository.getStudyProgressLatest()
                if (progress != null && plan != null) {

                    progress.spelled += 1

                    if (state.input != state.word.spelling) {
                        wrongRepository.addSpelledWrong(
                            wordId = state.word.id,
                            studyProgressId = progress.id
                        )
                    }

                    var wrongList = emptyList<Word>()
                    if (progress.spelled == progress.wordListSize) {
                        progress.progressState = ProgressState.FINISHED
                        progress.finishedDate = getTodayDateTime()

                        wrongList = wrongRepository.getSpelledWrong(progress.id)
                    }

                    studyProgressRepository.updateStudyProgress(progress)

                    if (progress.spelled == progress.wordListSize) {
                        val totalData = studyProgressRepository.getTotalReport(plan.vocabularySize)
                        if (totalData[0] == totalData[1]) {
                            plan.finished = true
                            studyPlanRepository.upsertStudyPlan(plan)
                        }
                    }

                    spellingUiState.value = state.copy(
                        submitted = true,
                        wrongList = wrongList,
                        dialog = SpellingUiState.Success.Dialog.None
                    )

                } else {
                    spellingUiState.value = SpellingUiState
                        .Error(DataResult.Error.Code.UNKNOWN)
                }
            }
        }
    }

    fun getNextWord() {
        val state = spellingUiState.value
        if (state is SpellingUiState.Success) {
            val currentWord = currentWordList[state.current + 1]

            spellingUiState.value = state.copy(
                current = state.current + 1,
                word = currentWord,
                input = "",
                submitted = false,
            )
        }
    }

    fun write(alphabet: String) {
        val state = spellingUiState.value
        if (state is SpellingUiState.Success) {
            var updated = state.input

            if (updated == "") {
                updated = alphabet
            } else {
                updated += alphabet
            }

            spellingUiState.value = state.copy(
                input = updated
            )
        }
    }

    fun remove() {
        val state = spellingUiState.value
        if (state is SpellingUiState.Success) {
            var updated = state.input

            updated = if (updated != "") {
                updated.dropLast(1)
            } else {
                ""
            }

            spellingUiState.value = state.copy(
                input = updated
            )
        }
    }

    fun showWrongList() {
        val state = spellingUiState.value
        if (state is SpellingUiState.Success) {
            spellingUiState.value = state.copy(
                showWrongList = true
            )
        }
    }

    fun hideWrongList() {
        val state = spellingUiState.value
        if (state is SpellingUiState.Success) {
            spellingUiState.value = state.copy(
                showWrongList = false
            )
        }
    }

    fun openDictionaryDialog(word: Word) {
        val state = spellingUiState.value
        if (state is SpellingUiState.Success && state.showWrongList) {
            spellingUiState.value = state.copy(
                clickedWrongWord = word,
                dialog = SpellingUiState.Success.Dialog.Dictionary
            )
        }
    }

    fun closeDialog() {
        val state = spellingUiState.value
        if (state is SpellingUiState.Success) {
            spellingUiState.value = state.copy(
                dialog = SpellingUiState.Success.Dialog.None
            )
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
                currentWordList = wordList
                val word = currentWordList[progress.spelled]

                spellingUiState.value = SpellingUiState.Success(
                    current = progress.spelled,
                    totalNum = progress.wordListSize,
                    word = word
                )
            } else {
                spellingUiState.value = SpellingUiState
                    .Error(DataResult.Error.Code.UNKNOWN)
            }

        }
    }

}