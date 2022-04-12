package com.peter.landing.ui.home.spelling

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.word.Word
import com.peter.landing.domain.study.SpellingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpellingViewModel @Inject constructor(
    private val useCase: SpellingUseCase
) : ViewModel() {

    private val spellWord = MutableLiveData<Word>()
    val currentSpellWord: LiveData<Word> = spellWord

    fun spell(input: String) =
        useCase.spell(input, viewModelScope)

    fun getNextWord() {
        useCase.nextWord()
        spellWord.postValue(useCase.getCurrentWord())
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

    suspend fun initTodaySpelling() {
        useCase.initTodaySpelling()
        spellWord.postValue(useCase.getCurrentWord())
    }

}