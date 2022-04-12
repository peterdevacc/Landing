package com.peter.landing.data.local.word

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "word_list",
    indices = [Index(value = ["spelling"], unique = true)]
)
data class Word(
    val spelling: String,

    val ipa: String,

    val cn: String,

    val en: String,

    @ColumnInfo(name = "pron_name")
    val pronName: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}