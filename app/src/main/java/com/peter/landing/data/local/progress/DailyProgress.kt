package com.peter.landing.data.local.progress

import androidx.room.*
import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.util.getTodayDateTime
import java.util.*

@Entity(
    tableName = "daily_progress",
    foreignKeys = [
        ForeignKey(
            entity = StudyPlan::class,
            parentColumns = ["id"], childColumns = ["study_plan_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["study_plan_id"], unique = false)]
)
data class DailyProgress(
    @ColumnInfo(name = "study_plan_id")
    val studyPlanId: Long,

    var start: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "is_finished")
    var isFinished: Boolean = false

    @ColumnInfo(name = "create_date")
    var createDate: Calendar = getTodayDateTime()
}