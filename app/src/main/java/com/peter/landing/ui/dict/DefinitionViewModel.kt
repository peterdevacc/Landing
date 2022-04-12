package com.peter.landing.ui.dict

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peter.landing.domain.dict.DefinitionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class DefinitionViewModel @Inject constructor(
    private val useCase: DefinitionUseCase
) : ViewModel() {

    fun search(spelling: String) =
        useCase.search(spelling, viewModelScope)

    suspend fun getWord() =
        useCase.getWord()

    suspend fun isWordNoted(wordId: Long) =
        useCase.isWordNoted(wordId)

    fun addNote(wordId: Long) =
        useCase.addNote(wordId, viewModelScope)

    fun removeNote(wordId: Long) =
        useCase.removeNote(wordId, viewModelScope)

}