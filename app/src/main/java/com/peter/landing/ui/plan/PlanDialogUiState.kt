package com.peter.landing.ui.plan

import com.peter.landing.data.local.vocabulary.Vocabulary
import java.util.*

sealed interface PlanDialogUiState {

    data class NewPlan(
        val vocabularyList: List<Vocabulary> = emptyList(),
        val vocabulary: Vocabulary = Vocabulary(),
        val wordListSize: Int = 0,
        val startDate: Calendar? = null,
        val endDate: String = "",
    ): PlanDialogUiState

    object DeletePlan: PlanDialogUiState

    object Processing: PlanDialogUiState

    object None: PlanDialogUiState

}