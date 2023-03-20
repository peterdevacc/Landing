package com.peter.landing.data.repository.vocabulary

import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.local.vocabulary.VocabularyDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VocabularyRepository @Inject constructor(
    private val vocabularyDAO: VocabularyDAO
) {

    private var vocabularyListCache = emptyList<Vocabulary>()

    suspend fun getVocabularyList(): List<Vocabulary> {
        if (vocabularyListCache.isEmpty()) {
            vocabularyListCache = vocabularyDAO.getVocabularyList()
        }
        return vocabularyListCache
    }

}