package com.peter.landing.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.peter.landing.data.local.LandingDatabase
import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.data.local.affix.AffixCatalogDAO
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
class AffixCatalogDAOTest {

    private lateinit var database: LandingDatabase
    private lateinit var dao: AffixCatalogDAO

    private lateinit var inputBuffer: BufferedReader
    private var jsonStr = ""

    @Test
    fun getAffixCatalogList() = runBlocking {
        val expectAffixCatalogList = getExpectAffixCatalogList()
        val affixCatalogList = dao.getAffixCatalogList()
        assertEquals(expectAffixCatalogList, affixCatalogList)
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.databaseBuilder(
            context,
            LandingDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset(DATABASE_PATH).build()
        dao = database.getAffixCatalogDAO()
        inputBuffer = context.resources.assets
            .open("affix_catalog_test.json")
            .bufferedReader()
        jsonStr = inputBuffer.readText()
    }

    @After
    fun clean() {
        database.close()
        inputBuffer.close()
    }

    private fun getExpectAffixCatalogList() : List<AffixCatalog> =
        Gson().fromJson(jsonStr, object : TypeToken<List<AffixCatalog>>() {}.type)

}