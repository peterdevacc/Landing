package com.peter.landing.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.peter.landing.data.local.LandingDatabase
import com.peter.landing.data.local.affix.Affix
import com.peter.landing.data.local.affix.AffixDAO
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
class AffixDAOTest {

    private lateinit var database: LandingDatabase
    private lateinit var dao: AffixDAO

    private lateinit var inputBuffer: BufferedReader
    private var jsonStr = ""

    @Test
    fun getAffixListByCatalogId() = runBlocking {
        val id: Long = 1
        val expectAffixList = getExpectAffixList().filter { it.catalogId == id }
        val affixList = dao.getAffixListByCatalogId(id)
        assertEquals(expectAffixList, affixList)
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.databaseBuilder(
            context,
            LandingDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset(DATABASE_PATH).build()
        dao = database.getAffixDAO()
        inputBuffer = context.resources.assets
            .open("affix_test.json")
            .bufferedReader()
        jsonStr = inputBuffer.readText()
    }

    @After
    fun clean() {
        database.close()
        inputBuffer.close()
    }

    private fun getExpectAffixList() : List<Affix> =
        Gson().fromJson(jsonStr, object : TypeToken<List<Affix>>() {}.type)

}