package com.peter.landing.data.local.ipa

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "ipa"
)
data class Ipa(
    val type: Type,

    val text: String,

    @ColumnInfo(name = "example_word_spelling")
    val exampleWordSpelling: String,

    @ColumnInfo(name = "example_word_ipa")
    val exampleWordIpa: String,

    @ColumnInfo(name = "example_word_pron_name")
    val exampleWordPronName: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    enum class Type(val cnValue: String) {
        CONSONANTS("辅音"),
        VOWELS("元音"),
        NONE("无")
    }
}