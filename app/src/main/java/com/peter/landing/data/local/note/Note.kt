package com.peter.landing.data.local.note

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peter.landing.data.local.word.Word

@Entity(
    tableName = "note",
    foreignKeys = [
        ForeignKey(
            entity = Word::class,
            parentColumns = ["id"], childColumns = ["word_id"],
            onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Note(
    @PrimaryKey
    @ColumnInfo(name = "word_id")
    val wordId: Long
)