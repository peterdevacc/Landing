package com.peter.landing.data.repository.progress

import com.peter.landing.data.local.progress.StudyProgress
import com.peter.landing.data.local.progress.StudyProgressDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudyProgressRepository @Inject constructor(
    private val studyProgressDAO: StudyProgressDAO
) {

    fun getStudyProgressLatestFlow() =
        studyProgressDAO.getStudyProgressLatestFlow()
            .flowOn(Dispatchers.IO)

    suspend fun getStudyProgressLatest() =
        studyProgressDAO.getStudyProgressLatest()

    suspend fun getLatestLessonReport(wordListSize: Int): List<Float> {

        val currentLesson = studyProgressDAO.getStudyProgressLatest()
        if (currentLesson != null) {
            val size = wordListSize.toFloat()
            val learnedPercentage = String.format(
                "%.1f", ((currentLesson.learned.toFloat() / size) * 100)
            ).toFloat()
            val chosenPercentage = String.format(
                "%.1f", ((currentLesson.chosen.toFloat() / size) * 100)
            ).toFloat()
            val spelledPercentage = String.format(
                "%.1f", ((currentLesson.spelled.toFloat() / size) * 100)
            ).toFloat()

            return listOf(
                learnedPercentage,
                chosenPercentage,
                spelledPercentage
            )
        }

        return listOf(0.0f, 0.0f, 0.0f)
    }

    suspend fun getTotalReport(vocabularySize: Int): List<Float> {
        val completed = studyProgressDAO.getCountSpelledInStudyProgress()

        return listOf(
            completed.toFloat(),
            vocabularySize.toFloat()
        )
    }

    suspend fun insertStudyProgress(studyProgress: StudyProgress) =
        studyProgressDAO.insertStudyProgress(studyProgress)

    suspend fun updateStudyProgress(studyProgress: StudyProgress) =
        studyProgressDAO.updateStudyProgress(studyProgress)

    suspend fun removeStudyProgress() =
        studyProgressDAO.deleteStudyProgress()

}