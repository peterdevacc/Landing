package com.peter.landing.ui.home.learn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.word.Word
import com.peter.landing.domain.study.LearnUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LearnViewModel @Inject constructor(
    private val useCase: LearnUseCase
) : ViewModel() {

    private val learnWord = MutableLiveData<Word>()
    val currentLearnWord: LiveData<Word> = learnWord

    fun learn() = useCase.learn(viewModelScope)

    fun isCurrentPositionBiggerThanLearned() =
        useCase.isCurrentIndexBiggerThanLearned()

    fun getNextWord() {
        useCase.getNextWord()
        learnWord.postValue(useCase.getCurrentWord())
    }

    fun getPreviousWord() {
        useCase.getPreviousWord()
        learnWord.postValue(useCase.getCurrentWord())
    }

    fun getCurrentNum() = useCase.getCurrentNum()

    fun getTotalNum() = useCase.getTotalNum()

    fun isLastWord() = useCase.isLastWord()

    fun setStudyState(studyState: Boolean) {
        useCase.setStudyState(studyState, viewModelScope)
    }

    suspend fun initTodayLearn() {
        useCase.initTodayLearn()
        learnWord.postValue(useCase.getCurrentWord())
    }

}