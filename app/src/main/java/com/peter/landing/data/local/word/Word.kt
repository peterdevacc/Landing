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
    @ColumnInfo(name = "spelling")
    val spelling: String,

    @ColumnInfo(name = "ipa")
    val ipa: String,

    @ColumnInfo(name = "cn")
    val cn: Map<String, List<String>>,

    @ColumnInfo(name = "en")
    val en: Map<String, List<String>>,

    @ColumnInfo(name = "pron_name")
    val pronName: String
) : Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

}