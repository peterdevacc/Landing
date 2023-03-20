package com.peter.landing.ui.plan

import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.util.DataResult
import java.util.*

sealed interface PlanUiState {

    object Loading : PlanUiState

    data class Existed(
        val studyPlan: StudyPlan,
        val progressReport: List<Float> = emptyList(),
        val totalReport: List<Float> = emptyList(),
        val dialog: Dialog = Dialog.None,
    ) : PlanUiState {

        enum class Dialog {
            DeletePlan, None
        }

    }

    data class Empty(
        val vocabularyList: List<Vocabulary> = emptyList(),
        val dialog: Dialog = Dialog.None,
        val vocabulary: Vocabulary = Vocabulary(),
        val wordListSize: Int = 0,
        val startDate: Calendar? = null,
        val endDate: String = "",
    ) : PlanUiState {

        enum class Dialog {
            NewPlan, None
        }

    }

    data class Error(
        val code: DataResult.Error.Code
    ) : PlanUiState

}