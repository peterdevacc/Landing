package com.peter.landing.data.local.vocabulary

import androidx.room.DatabaseView
import androidx.room.Embedded
import com.peter.landing.data.local.word.Word

@DatabaseView(
    value = """
        SELECT intermediate.word_id AS id, spelling, ipa, cn, en, pron_name
        FROM word_list a 
        INNER JOIN vocabulary_intermediate intermediate 
        ON a.id = intermediate.word_id""",
    viewName = "vocabulary_view_intermediate"
)
data class VocabularyIntermediateView(
    @Embedded
    val word: Word
)