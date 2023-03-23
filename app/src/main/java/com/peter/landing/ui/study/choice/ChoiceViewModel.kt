package com.peter.landing.ui.study.choice

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.progress.ProgressState
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.repository.progress.StudyProgressRepository
import com.peter.landing.data.repository.vocabulary.VocabularyViewRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import com.peter.landing.data.util.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChoiceViewModel @Inject constructor(
    private val vocabularyViewRepository: VocabularyViewRepository,
    private val studyProgressRepository: StudyProgressRepository,
    private val wrongRepository: WrongRepository
): ViewModel() {

    private val choiceUiState: MutableState<ChoiceUiState> =
        mutableStateOf(ChoiceUiState.Loading)
    val uiState: State<ChoiceUiState> = choiceUiState

    private var currentWordList = emptyList<Word>()

    fun submit() {
        val state = choiceUiState.value
        if (state is ChoiceUiState.Success) {
            choiceUiState.value = state.copy(
                dialog = ChoiceUiState.Success.Dialog.Processing
            )

            viewModelScope.launch {
                val progress = studyProgressRepository.getStudyProgressLatest()
                if (progress != null) {

                    progress.chosen += 1

                    if (state.chosenIndex != state.correctIndex) {
                        wrongRepository.addChosenWrong(
                            wordId = state.wordId,
                            studyProgressId = progress.id
                        )
                    }

                    var wrongList = emptyList<Word>()
                    if (progress.chosen == progress.wordListSize) {
                        progress.progressState = ProgressState.SPELLING
                        wrongList = wrongRepository.getChosenWrong(progress.id)
                    }

                    studyProgressRepository.updateStudyProgress(progress)

                    choiceUiState.value = state.copy(
                        submitted = true,
                        wrongList = wrongList,
                        dialog = ChoiceUiState.Success.Dialog.None
                    )

                } else {
                    choiceUiState.value = ChoiceUiState
                        .Error(DataResult.Error.Code.UNKNOWN)
                }
            }

        }
    }

    fun choose(index: Int) {
        val state = choiceUiState.value
        if (state is ChoiceUiState.Success) {
            if (state.chosenIndex == index) {
                choiceUiState.value = state.copy(
                    chosenIndex = -2
                )
            } else {
                choiceUiState.value = state.copy(
                    chosenIndex = index
                )
            }
        }
    }

    fun getNextWord() {
        val state = choiceUiState.value
        if (state is ChoiceUiState.Success) {
            val nextWord = currentWordList[state.current + 1]
            val optionWordList = getShuffleListWithoutWord(nextWord)
            val correctIndex = optionWordList.indexOf(nextWord)
            val optionList = optionWordList.map {
                it.cn to it.en
            }

            choiceUiState.value = state.copy(
                current = state.current + 1,
                wordId = nextWord.id,
                spelling = nextWord.spelling,
                ipa = nextWord.ipa,
                pronName = nextWord.pronName,
                optionList = optionList,
                correctIndex = correctIndex,
                chosenIndex = -2,
                submitted = false,
            )
        }
    }

    fun showWrongList() {
        val state = choiceUiState.value
        if (state is ChoiceUiState.Success) {
            choiceUiState.value = state.copy(
                showWrongList = true
            )
        }
    }

    fun hideWrongList() {
        val state = choiceUiState.value
        if (state is ChoiceUiState.Success) {
            choiceUiState.value = state.copy(
                showWrongList = false
            )
        }
    }

    fun openDictionaryDialog(word: Word) {
        val state = choiceUiState.value
        if (state is ChoiceUiState.Success && state.showWrongList) {
            choiceUiState.value = state.copy(
                clickedWrongWord = word,
                dialog = ChoiceUiState.Success.Dialog.Dictionary
            )
        }
    }

    fun closeDialog() {
        val state = choiceUiState.value
        if (state is ChoiceUiState.Success) {
            choiceUiState.value = state.copy(
                dialog = ChoiceUiState.Success.Dialog.None
            )
        }
    }

    private fun getShuffleListWithoutWord(word: Word): List<Word> {
        val choiceList = currentWordList.toMutableList()
        choiceList.remove(word)
        choiceList.shuffle()
        val wordOptions = choiceList.take(3).toMutableList()
        wordOptions.add(word)
        wordOptions.shuffle()
        return wordOptions.toList()
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
                val word = currentWordList[progress.chosen]
                val optionWordList = getShuffleListWithoutWord(word)
                val correctIndex = optionWordList.indexOf(word)
                val optionList = optionWordList.map {
                    it.cn to it.en
                }

                choiceUiState.value = ChoiceUiState.Success(
                    current = progress.chosen,
                    wordId = word.id,
                    spelling = word.spelling,
                    ipa = word.ipa,
                    pronName = word.pronName,
                    optionList = optionList,
                    correctIndex = correctIndex,
                    totalNum = progress.wordListSize
                )
            } else {
                choiceUiState.value = ChoiceUiState
                    .Error(DataResult.Error.Code.UNKNOWN)
            }

        }
    }

}