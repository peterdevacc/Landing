package com.peter.landing.ui.home.revise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.data.local.wrong.WrongWord
import com.peter.landing.domain.study.ReviseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val useCase: ReviseUseCase
) : ViewModel() {

    private val wrongWord = MutableLiveData<WrongWord>()
    val currentWrongAndWord: LiveData<WrongWord> = wrongWord

    fun revised() = useCase.revised(viewModelScope)

    fun getNextWord() {
        useCase.nextWord()
        wrongWord.postValue(useCase.getCurrentWrongWord())
    }

    fun getCurrentNum() = useCase.getCurrentNum()

    fun getTotalNum() = useCase.getTotalNum()

    fun isLastWord() = useCase.isLastWord()

    fun isCorrect(answer: String) = useCase.isCorrect(answer)

    fun setArranged(arranged: Boolean) = useCase.setArranged(arranged)

    fun isArranged() = useCase.isArranged()

    suspend fun initTodayRevise() {
        useCase.initTodayRevise()
        wrongWord.postValue(useCase.getCurrentWrongWord())
    }

}
