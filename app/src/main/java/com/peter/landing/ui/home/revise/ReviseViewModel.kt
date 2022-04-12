package com.peter.landing.ui.home.revise

import androidx.lifecycle.ViewModel
import com.peter.landing.domain.study.ReviseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviseViewModel @Inject constructor(
    private val useCase: ReviseUseCase
) : ViewModel() {

    fun getTodayReviseWordList() =
        useCase.getTodayReviseWordList()

    suspend fun initTodayRevise() =
        useCase.initTodayRevise()

}
