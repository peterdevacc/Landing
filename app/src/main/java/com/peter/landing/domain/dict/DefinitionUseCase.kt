package com.peter.landing.domain.dict

import com.peter.landing.data.local.note.Note
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.repository.note.NoteRepository
import com.peter.landing.data.repository.word.WordRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefinitionUseCase @Inject constructor(
    private val wordRepository: WordRepository,
    private val noteRepository: NoteRepository,
    private val wrongRepository: WrongRepository
) {

    private lateinit var wordDeferred: Deferred<Word?>

    suspend fun getWord(): Word? {
        try {
            return wordDeferred.await()
        } catch (exception: Exception) {
        }

        return null
    }

    fun search(spelling: String, scope: CoroutineScope) {
        wordDeferred = scope.async {
            wordRepository.searchWord(spelling)
        }
    }

    suspend fun isWordNoted(wordId: Long): Boolean {
        return noteRepository.checkNoteExist(wordId)
    }

    fun addNote(wordId: Long, scope: CoroutineScope) {
        scope.launch {
            noteRepository.addNote(Note(wordId))
            wrongRepository.markWrongNoted(wordId)
        }
    }

    fun removeNote(wordId: Long, scope: CoroutineScope) {
        scope.launch {
            noteRepository.removeNote(wordId)
            wrongRepository.unMarkWrongNoted(wordId)
        }
    }

}