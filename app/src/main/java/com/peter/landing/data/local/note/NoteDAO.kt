package com.peter.landing.data.local.note

import androidx.paging.PagingSource
import androidx.room.*
import com.peter.landing.data.local.word.Word

@Dao
interface NoteDAO {

    @Query("SELECT * FROM note WHERE word_id = :wordId")
    suspend fun getNoteByWordId(wordId: Long): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("DELETE FROM note WHERE word_id = :wordId")
    suspend fun deleteNoteByWordId(wordId: Long)

    @Query(
        """
        SELECT b.id AS id, spelling, ipa, cn, en, pron_name
        FROM note a 
        INNER JOIN word_list b 
        ON a.word_id = b.id ORDER BY b.spelling COLLATE NOCASE ASC"""
    )
    fun getNotedWordListOrderBySpellingWithNoCasePaging(): PagingSource<Int, Word>

}