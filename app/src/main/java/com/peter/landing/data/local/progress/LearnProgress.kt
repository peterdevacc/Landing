package com.peter.landing.data.local.progress

import androidx.room.*

@Entity(
    tableName = "learn_progress",
    foreignKeys = [
        ForeignKey(
            entity = DailyProgress::class,
            parentColumns = ["id"], childColumns = ["daily_progress_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["daily_progress_id"], unique = false)]
)
data class LearnProgress(
    @ColumnInfo(name = "daily_progress_id")
    val dailyProgressId: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var learned: Int = 0
}