package com.peter.landing.data.local.progress

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peter.landing.util.getTodayDateTime
import java.util.*

@Entity(tableName = "revise_progress")
data class ReviseProgress(
    var revised: Int
) {
    constructor() : this(0)

    @PrimaryKey
    @ColumnInfo(name = "create_date")
    var createDate: Calendar = getTodayDateTime()
}