package com.peter.landing.domain.note

import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import com.peter.landing.data.local.note.Note
import com.peter.landing.data.repository.note.NoteRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class NoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val wrongRepository: WrongRepository
) {

    val noteWordList: Flow<PagingData<NoteItem.Data>>
        get() = noteRepository.getNotedWordFlow()
            .map { pagingData ->
                pagingData.map {
                    NoteItem.Data.ItemNote(it)
                }
            }.map {
                it.insertSeparators { before, after ->
                    if (before == null) {
                        after?.let {
                            val alphabet = getAlphabet(after.word.spelling)
                            return@insertSeparators NoteItem.Data.AlphabetHeader(alphabet)
                        }
                    }
                    if (after != null && before != null) {
                        val beforeAlphabet = getAlphabet(before.word.spelling)
                        val afterAlphabet = getAlphabet(after.word.spelling)
                        if (beforeAlphabet != afterAlphabet) {
                            return@insertSeparators NoteItem.Data.AlphabetHeader(afterAlphabet)
                        }
                    }
                    return@insertSeparators null
                }
            }

    fun getTotalNoteNum() = noteRepository.getTotalNoteNumFlow()

    fun addNote(wordId: Long, scope: CoroutineScope) = scope.launch {
        noteRepository.addNote(Note(wordId))
        wrongRepository.markWrongNoted(wordId)
    }

    fun removeNote(wordId: Long, scope: CoroutineScope) = scope.launch {
        noteRepository.removeNote(wordId)
        wrongRepository.unMarkWrongNoted(wordId)
    }

    private fun getAlphabet(spelling: String): String {
        return spelling.first().lowercase(Locale.ENGLISH)
    }

}