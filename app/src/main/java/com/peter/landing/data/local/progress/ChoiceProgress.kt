package com.peter.landing.data.local.progress

import androidx.room.*

@Entity(
    tableName = "choice_progress",
    foreignKeys = [
        ForeignKey(
            entity = DailyProgress::class,
            parentColumns = ["id"], childColumns = ["daily_progress_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["daily_progress_id"], unique = false)]
)
data class ChoiceProgress(
    @ColumnInfo(name = "daily_progress_id")
    val dailyProgressId: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var chosen: Int = 0

    @ColumnInfo(name = "choose_correct")
    var chooseCorrect = 0
}