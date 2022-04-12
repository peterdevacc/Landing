package com.peter.landing.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.peter.landing.data.local.LandingDatabase
import com.peter.landing.data.local.ipa.Ipa
import com.peter.landing.data.local.ipa.IpaDAO
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
class IpaDAOTest {

    private lateinit var database: LandingDatabase
    private lateinit var dao: IpaDAO

    private lateinit var inputBufferConsonants: BufferedReader
    private lateinit var inputBufferVowels: BufferedReader
    private var jsonStrConsonants = ""
    private var jsonStrVowels = ""

    @Test
    fun ipaTest() = runBlocking {
        val ipaConsonants = getIpaConsonants()
        val ipaVowels = getIpaVowels()
        val expectIpaList = ipaConsonants.toMutableList()
        expectIpaList.addAll(ipaVowels)
        val ipaList = dao.getIpaList()
        assertEquals(expectIpaList, ipaList)
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.databaseBuilder(
            context,
            LandingDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset(DATABASE_PATH).build()
        dao = database.getIpaDAO()
        inputBufferConsonants = context.resources.assets
            .open("ipa_consonants_test.json")
            .bufferedReader()
        jsonStrConsonants = inputBufferConsonants.readText()
        inputBufferVowels = context.resources.assets
            .open("ipa_vowels_test.json")
            .bufferedReader()
        jsonStrVowels = inputBufferVowels.readText()
    }

    @After
    fun clean() {
        database.close()
        inputBufferConsonants.close()
        inputBufferVowels.close()
    }

    private fun getIpaConsonants() : List<Ipa> =
        Gson().fromJson(jsonStrConsonants, object : TypeToken<List<Ipa>>() {}.type)

    private fun getIpaVowels() : List<Ipa> =
        Gson().fromJson(jsonStrVowels, object : TypeToken<List<Ipa>>() {}.type)
}