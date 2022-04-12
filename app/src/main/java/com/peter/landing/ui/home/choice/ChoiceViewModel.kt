package com.peter.landing.ui.home.choice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.word.Word
import com.peter.landing.domain.study.ChoiceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChoiceViewModel @Inject constructor(
    private val useCase: ChoiceUseCase
) : ViewModel() {

    private val choiceWord = MutableLiveData<Word>()
    val currentChoiceWord: LiveData<Word> = choiceWord

    fun choose(option: String) =
        useCase.choose(option, viewModelScope)

    fun getNextWord() {
        useCase.nextWord()
        choiceWord.postValue(useCase.getCurrentWord())
    }

    fun getAnswer() = useCase.getAnswer()

    fun getOptions() = useCase.getOptions()

    fun setOptions() = useCase.setOptions()

    fun getCurrentNum() = useCase.getCurrentNum()

    fun getTotalNum() = useCase.getTotalNum()

    fun isLastWord() = useCase.isLastWord()

    fun setStudyState(studyState: Boolean) {
        useCase.setStudyState(studyState, viewModelScope)
    }

    suspend fun initTodayChoice() {
        useCase.initTodayChoice()
        choiceWord.postValue(useCase.getCurrentWord())
    }

}