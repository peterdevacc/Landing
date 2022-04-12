package com.peter.landing.data.local.vocabulary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.peter.landing.data.local.word.Word

@Entity(
    tableName = "vocabulary_intermediate",
    foreignKeys = [
        ForeignKey(
            entity = Word::class,
            parentColumns = ["id"], childColumns = ["word_id"],
            onDelete = CASCADE, onUpdate = CASCADE
        )
    ]
)
data class VocabularyIntermediate(
    @PrimaryKey
    @ColumnInfo(name = "word_id")
    val wordId: Long
)