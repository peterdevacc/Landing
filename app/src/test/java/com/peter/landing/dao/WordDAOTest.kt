package com.peter.landing.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.peter.landing.data.local.LandingDatabase
import com.peter.landing.data.local.word.WordDAO
import com.peter.landing.data.util.DATABASE_NAME
import com.peter.landing.data.util.DATABASE_PATH
import com.peter.landing.debug.expectWord
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class WordDAOTest {

    private lateinit var database: LandingDatabase
    private lateinit var dao: WordDAO

    @Test
    fun getWordBySpelling() = runBlocking {
        val result = dao.getWordBySpelling(expectWord.spelling)!!

        assertEquals(expectWord.spelling, result.spelling)
        assertEquals(expectWord.ipa, result.ipa)
        assertEquals(expectWord.cn, result.cn)
        assertEquals(expectWord.en, result.en)
        assertEquals(expectWord.pronName, result.pronName)
    }

    @Test
    fun getWordSuggestionsBySimilarSpelling() = runBlocking {
        val result = dao.getWordSuggestionsBySimilarSpelling("${expectWord.spelling}%")
        assertEquals(result.size, 2)

        assertEquals(expectWord.spelling, result.first())
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.databaseBuilder(
            context,
            LandingDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset(DATABASE_PATH).build()
        dao = database.getWordDAO()
    }

    @After
    fun clean() {
        database.close()
    }

}