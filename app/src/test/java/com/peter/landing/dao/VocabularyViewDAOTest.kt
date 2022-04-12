package com.peter.landing.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.peter.landing.data.local.LandingDatabase
import com.peter.landing.data.local.vocabulary.VocabularyViewDAO
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.util.DATABASE_NAME
import com.peter.landing.data.util.DATABASE_PATH
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.BufferedReader

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class VocabularyViewDAOTest {

    private lateinit var database: LandingDatabase
    private lateinit var dao: VocabularyViewDAO

    private lateinit var inputBufferBeginner: BufferedReader
    private lateinit var inputBufferIntermediate: BufferedReader
    private var jsonStrBeginner = ""
    private var jsonStrIntermediate = ""

    private val start = 0
    private val end = 20

    @Test
    fun getWordListFromBeginnerViewByRange() = runBlocking {
        val result = dao.getWordListFromBeginnerViewByRange(start, end)

        val expect = getExpectBeginnerWordList()

        wordListCheck(expect, result)
    }

    @Test
    fun getWordListFromIntermediateViewByRange() = runBlocking {
        val result = dao.getWordListFromIntermediateViewByRange(start, end)

        val expect = getExpectIntermediateWordList()

        wordListCheck(expect, result)
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.databaseBuilder(
            context,
            LandingDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset(DATABASE_PATH).build()
        dao = database.getVocabularyViewDAO()
        inputBufferBeginner = context.resources.assets
            .open("vocabulary_view_beginner_test.json")
            .bufferedReader()
        jsonStrBeginner = inputBufferBeginner.readText()
        inputBufferIntermediate = context.resources.assets
            .open("vocabulary_view_intermediate_test.json")
            .bufferedReader()
        jsonStrIntermediate = inputBufferIntermediate.readText()
    }

    @After
    fun clean() {
        database.close()
        inputBufferBeginner.close()
        inputBufferIntermediate.close()
    }

    private fun wordListCheck(expect: List<Word>, result: List<Word>) {
        assertEquals(expect.size, result.size)
        result.zip(expect).forEach {
            wordCheck(it.first, it.second)
        }
    }

    private fun wordCheck(expect: Word, result: Word) {
        assertEquals(expect.spelling, result.spelling)
        assertEquals(expect.ipa, result.ipa)
        assertEquals(expect.cn, result.cn)
        assertEquals(expect.en, result.en)
        assertEquals(expect.pronName, result.pronName)
    }

    private fun getExpectBeginnerWordList() : List<Word> =
        Gson().fromJson(jsonStrBeginner, object : TypeToken<List<Word>>() {}.type)

    private fun getExpectIntermediateWordList() : List<Word> =
        Gson().fromJson(jsonStrIntermediate, object : TypeToken<List<Word>>() {}.type)

}