package com.peter.landing.data.local.plan

import androidx.room.*
import com.peter.landing.data.local.vocabulary.Vocabulary
import java.util.*

@Entity(tableName = "study_plan")
data class StudyPlan(
    @ColumnInfo(name = "vocabulary_name")
    var vocabularyName: Vocabulary.Name,

    @ColumnInfo(name = "vocabulary_size")
    var vocabularySize: Int,

    @ColumnInfo(name = "word_list_size")
    var wordListSize: Int,

    @ColumnInfo(name = "start_date")
    var startDate: Calendar?
) {
    @PrimaryKey
    var id: Long = 1

    @ColumnInfo(name = "finished")
    var finished: Boolean = false
}