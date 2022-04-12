package com.peter.landing.ui.home.typing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.word.Word
import com.peter.landing.domain.study.TypingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TypingViewModel @Inject constructor(
    private val useCase: TypingUseCase
) : ViewModel() {

    private val typingWord = MutableLiveData<Word>()
    val currentTypingWord: LiveData<Word> = typingWord

    fun typing(input: String) =
        useCase.typing(input, viewModelScope)

    fun getNextWord() {
        useCase.nextWord()
        typingWord.postValue(useCase.getCurrentWord())
    }

    fun getCurrentNum() =
        useCase.getCurrentNum()

    fun getTotalNum() =
        useCase.getTotalNum()

    fun isLastWord() =
        useCase.isLastWord()

    fun setAnswer(spelling: String) =
        useCase.setAnswer(spelling)

    fun setStudyState(studyState: Boolean) {
        useCase.setStudyState(studyState, viewModelScope)
    }

    suspend fun initTodayTyping() {
        useCase.initTodayTyping()
        typingWord.postValue(useCase.getCurrentWord())
    }

}