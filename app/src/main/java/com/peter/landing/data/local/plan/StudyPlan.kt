package com.peter.landing.data.local.plan

import androidx.room.*
import com.peter.landing.data.local.vocabulary.Vocabulary
import java.util.*

@Entity(
    tableName = "study_plan",
    foreignKeys = [
        ForeignKey(
            entity = Vocabulary::class,
            parentColumns = ["name"], childColumns = ["vocabulary_name"]
        )
    ],
    indices = [Index(value = ["vocabulary_name"], unique = false)]
)
data class StudyPlan(
    @ColumnInfo(name = "vocabulary_name")
    var vocabularyName: Vocabulary.Name,

    @ColumnInfo(name = "word_list_size")
    var wordListSize: Int,

    @ColumnInfo(name = "start_date")
    var startDate: Calendar?
) {
    constructor() : this(
        Vocabulary.Name.NONE, 0, null
    )

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}