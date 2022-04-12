package com.peter.landing.data.repository.vocabulary

import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.local.vocabulary.VocabularyViewDAO
import com.peter.landing.data.local.word.Word
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VocabularyViewRepository @Inject constructor(
    private val vocabularyViewDAO: VocabularyViewDAO
) {

    private var wordListCache = emptyList<Word>()
    private var beginCache = -1
    private var numCache = -1

    suspend fun getWordList(start: Int, num: Int, name: Vocabulary.Name): List<Word> {
        if (beginCache != start || numCache != num) {
            beginCache = start
            numCache = num
            wordListCache = when (name) {
                Vocabulary.Name.BEGINNER -> getWordListBeginner(start, num)
                Vocabulary.Name.INTERMEDIATE -> getWordListIntermediate(start, num)
                Vocabulary.Name.NONE -> emptyList()
            }
        }
        return wordListCache
    }

    private suspend fun getWordListBeginner(start: Int, num: Int) =
        vocabularyViewDAO.getWordListFromBeginnerViewByRange(start, num)

    private suspend fun getWordListIntermediate(start: Int, num: Int) =
        vocabularyViewDAO.getWordListFromIntermediateViewByRange(start, num)

    fun emptyWordListCache() {
        if (wordListCache.isNotEmpty()) {
            wordListCache = emptyList()
            beginCache = -1
            numCache = -1
        }
    }

}