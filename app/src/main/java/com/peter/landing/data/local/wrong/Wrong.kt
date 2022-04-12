package com.peter.landing.data.local.wrong

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peter.landing.data.local.word.Word
import com.peter.landing.util.getTodayDateTime
import com.peter.landing.util.getTomorrowDateTime
import java.util.*

@Entity(
    tableName = "wrong",
    foreignKeys = [
        ForeignKey(
            entity = Word::class,
            parentColumns = ["id"], childColumns = ["word_id"],
            onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Wrong(
    @PrimaryKey
    @ColumnInfo(name = "word_id")
    val wordId: Long,

    @ColumnInfo(name = "chosen_wrong")
    val chosenWrong: Boolean = false,

    @ColumnInfo(name = "spelled_wrong")
    var spelledWrong: Boolean = false,

    @ColumnInfo(name = "revise_date")
    var reviseDate: Calendar = getTomorrowDateTime(),

    @ColumnInfo(name = "add_date")
    var addDate: Calendar = getTodayDateTime()
) {
    @ColumnInfo(name = "revise_times")
    var reviseTimes = 0

    @ColumnInfo(name = "is_noted")
    var isNoted: Boolean = false

    enum class Type {
        CHOOSE, SPELLING
    }

}