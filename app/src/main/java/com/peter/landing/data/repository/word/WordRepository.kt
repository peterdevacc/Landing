package com.peter.landing.data.repository.word

import com.peter.landing.data.local.word.WordDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordRepository @Inject constructor(
    private val wordDAO: WordDAO
) {

    suspend fun searchWord(spelling: String) =
        wordDAO.getWordBySpelling(spelling)

    suspend fun getSearchSuggestions(spelling: String) =
        wordDAO.getWordSuggestionsBySimilarSpelling("$spelling%")

}