package com.peter.landing.data.repository.wrong

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.peter.landing.data.local.wrong.Wrong
import com.peter.landing.data.local.wrong.WrongDAO
import com.peter.landing.data.local.wrong.ProgressWrongWord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WrongRepository @Inject constructor(
    private val wrongDAO: WrongDAO
) {

    suspend fun getChosenWrong(studyProgressId: Long) =
        wrongDAO.getWrongByStudyProgressIdAndChosenWrong(studyProgressId)

    suspend fun getSpelledWrong(studyProgressId: Long) =
        wrongDAO.getWrongByStudyProgressIdAndSpelledWrong(studyProgressId)

    suspend fun addChosenWrong(wordId: Long, studyProgressId: Long) {
        wrongDAO.insertWrong(
            Wrong(
                wordId = wordId,
                studyProgressId = studyProgressId,
                chosenWrong = true
            )
        )
    }

    suspend fun addSpelledWrong(wordId: Long, studyProgressId: Long) {
        val wrong = wrongDAO.getWrongById(wordId)
        if (wrong != null) {
            wrong.spelledWrong = true
            wrongDAO.updateWrong(wrong)
        } else {
            wrongDAO.insertWrong(
                Wrong(
                    wordId = wordId,
                    studyProgressId = studyProgressId,
                    spelledWrong = true
                )
            )
        }
    }

    fun getWrongWordFlow(): Flow<PagingData<ProgressWrongWord>> {
        return Pager(
            PagingConfig(
                pageSize = 30,
                enablePlaceholders = false,
                maxSize = 100
            )
        ) { wrongDAO.getWrongWordPaging() }.flow
    }

    suspend fun removeWrong() =
        wrongDAO.deleteWrong()

}