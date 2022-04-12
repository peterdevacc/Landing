package com.peter.landing.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.peter.landing.data.local.LandingDatabase
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.local.vocabulary.VocabularyDAO
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
class VocabularyDAOTest {

    private lateinit var database: LandingDatabase
    private lateinit var dao: VocabularyDAO

    private lateinit var inputBuffer: BufferedReader
    private var jsonStr = ""

    @Test
    fun getVocabularyByName() = runBlocking {
        val expectedVocabularyList = getExpectedVocabularyList()
        val expectedVocabulary = expectedVocabularyList.first()
        val result = dao.getVocabularyByVocabularyName(expectedVocabulary.name)

        vocabularyCheck(expectedVocabulary, result)
    }

    @Test
    fun getVocabularyList() = runBlocking {
        val result = dao.getVocabularyList()

        val expectedVocabularyList = getExpectedVocabularyList()
        result.zip(expectedVocabularyList).forEach {
            vocabularyCheck(it.first, it.second)
        }
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.databaseBuilder(
            context,
            LandingDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset(DATABASE_PATH).build()
        dao = database.getVocabularyDAO()
        inputBuffer = context.resources.assets
            .open("vocabulary_test.json")
            .bufferedReader()
        jsonStr = inputBuffer.readText()
    }

    @After
    fun clean() {
        database.close()
        inputBuffer.close()
    }

    private fun getExpectedVocabularyList() : List<Vocabulary> =
        Gson().fromJson(jsonStr, object : TypeToken<List<Vocabulary>>() {}.type)

    private fun vocabularyCheck(result: Vocabulary, expect: Vocabulary) {
        assertEquals(expect.name, result.name)
        assertEquals(expect.size, result.size)
        assertEquals(expect.description, result.description)
        assertEquals(expect.image, result.image)
    }

}