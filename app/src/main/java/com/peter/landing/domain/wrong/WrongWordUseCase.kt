package com.peter.landing.domain.wrong

import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.insertSeparators
import androidx.paging.map
import com.peter.landing.data.local.note.Note
import com.peter.landing.data.repository.note.NoteRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import com.peter.landing.util.getDate
import com.peter.landing.util.getTodayDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
class WrongWordUseCase @Inject constructor(
    private val wrongRepository: WrongRepository,
    private val noteRepository: NoteRepository
) {

    private val isAllMutableStateFlow = MutableStateFlow(false)
    private val wrongWordFlow = wrongRepository.getWrongWordFlow()

    val isAllCurrentValue = isAllMutableStateFlow
        .asStateFlow()

    val wrongWordList: Flow<PagingData<WrongWordItem.Data>> =
        isAllMutableStateFlow.asStateFlow().flatMapLatest { isAll ->
            if (isAll) {
                getAllWrongWord()
            } else {
                getTodayWrongWord()
            }
        }

    fun todayOrAllSwitcher(scope: CoroutineScope) = scope.launch {
        isAllMutableStateFlow.emit(!isAllMutableStateFlow.value)
    }

    fun addNote(wordId: Long, scope: CoroutineScope) = scope.launch {
        noteRepository.addNote(Note(wordId))
        wrongRepository.markWrongNoted(wordId)
    }

    fun removeNote(wordId: Long, scope: CoroutineScope) = scope.launch {
        noteRepository.removeNote(wordId)
        wrongRepository.unMarkWrongNoted(wordId)
    }

    private fun getTodayWrongWord(): Flow<PagingData<WrongWordItem.Data>> =
        wrongWordFlow
            .map { pagingData ->
                pagingData.filter { data ->
                    data.wrong.addDate.time.time == getTodayDateTime().time.time
                }.map {
                    WrongWordItem.Data.ItemWrongWord(it)
                }
            }.map {
                it.insertSeparators { before, after ->
                    if (before == null) {
                        after?.let {
                            val alphabet = getAlphabet(after.wrongWord.word.spelling)
                            return@insertSeparators WrongWordItem.Data.AlphabetHeader(alphabet)
                        }
                    }
                    if (after != null && before != null) {
                        val beforeAlphabet = getAlphabet(before.wrongWord.word.spelling)
                        val afterAlphabet = getAlphabet(after.wrongWord.word.spelling)
                        if (beforeAlphabet != afterAlphabet) {
                            return@insertSeparators WrongWordItem.Data.AlphabetHeader(afterAlphabet)
                        }
                    }
                    return@insertSeparators null
                }
            }

    private fun getAllWrongWord(): Flow<PagingData<WrongWordItem.Data>> =
        wrongWordFlow
            .map { pagingData ->
                pagingData.map {
                    WrongWordItem.Data.ItemWrongWord(it)
                }
            }.map { pagingData ->
                pagingData.insertSeparators { before, after ->
                    if (before == null) {
                        after?.let {
                            val wrong = it.wrongWord.wrong
                            val date = getDate(wrong.addDate.time)
                            return@insertSeparators WrongWordItem.Data.SeparatorAddDate(date)
                        }
                    }
                    if (after != null && before != null) {
                        val wrongA = before.wrongWord.wrong
                        val wrongB = after.wrongWord.wrong
                        if (isNeedSeparate(wrongA.addDate, wrongB.addDate)) {
                            val date = getDate(wrongB.addDate.time)
                            return@insertSeparators WrongWordItem.Data.SeparatorAddDate(date)
                        }
                    }
                    return@insertSeparators null
                }
            }

    private fun isNeedSeparate(dateA: Calendar, dateB: Calendar) =
        dateA.get(Calendar.DAY_OF_YEAR) != dateB.get(Calendar.DAY_OF_YEAR)

    private fun getAlphabet(spelling: String): String {
        return spelling.first().lowercase(Locale.ENGLISH)
    }

}