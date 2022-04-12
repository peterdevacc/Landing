package com.peter.landing.data.local.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peter.landing.util.getTodayDateTimeFull
import java.util.*

@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey
    val input: String = "",

    @ColumnInfo(name = "search_date")
    val searchDate: Calendar = getTodayDateTimeFull()
)