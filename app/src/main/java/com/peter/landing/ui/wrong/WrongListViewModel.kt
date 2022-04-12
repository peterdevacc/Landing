package com.peter.landing.ui.wrong

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.peter.landing.domain.wrong.WrongWordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class WrongListViewModel @Inject constructor(
    private val useCase: WrongWordUseCase
) : ViewModel() {

    val wrongWordList = useCase
        .wrongWordList
        .cachedIn(viewModelScope)

    val isAllValue = useCase
        .isAllCurrentValue

    fun todayOrAllSwitcher() =
        useCase.todayOrAllSwitcher(viewModelScope)


    fun addNote(wordId: Long) =
        useCase.addNote(wordId, viewModelScope)

    fun removeNote(wordId: Long) =
        useCase.removeNote(wordId, viewModelScope)

}