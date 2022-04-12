package com.peter.landing.data.local.vocabulary

import androidx.room.DatabaseView
import androidx.room.Embedded
import com.peter.landing.data.local.word.Word

@DatabaseView(
    value = """
        SELECT beginner.word_id AS id, spelling, ipa, cn, en, pron_name
        FROM word_list a 
        INNER JOIN vocabulary_beginner beginner 
        ON a.id = beginner.word_id""",
    viewName = "vocabulary_view_beginner"
)
data class VocabularyBeginnerView(
    @Embedded
    val word: Word
)