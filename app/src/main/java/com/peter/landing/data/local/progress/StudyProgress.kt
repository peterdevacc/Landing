package com.peter.landing.data.local.progress

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peter.landing.data.local.vocabulary.Vocabulary
import java.util.*

@Entity(tableName = "study_progress")
data class StudyProgress(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "vocabulary_name")
    val vocabularyName: Vocabulary.Name,

    @ColumnInfo(name = "start")
    val start: Int,

    @ColumnInfo(name = "word_list_size")
    val wordListSize: Int,
) {
    @ColumnInfo(name = "learned")
    var learned: Int = 0

    @ColumnInfo(name = "chosen")
    var chosen: Int = 0

    @ColumnInfo(name = "spelled")
    var spelled: Int = 0

    @ColumnInfo(name = "progress_state")
    var progressState: ProgressState = ProgressState.WORD_LIST

    @ColumnInfo(name = "finished_date")
    var finishedDate: Calendar? = null
}
