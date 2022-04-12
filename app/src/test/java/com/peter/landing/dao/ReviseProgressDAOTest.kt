package com.peter.landing.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.peter.landing.data.local.LandingDatabase
import com.peter.landing.data.local.progress.ReviseProgress
import com.peter.landing.data.local.progress.ReviseProgressDAO
import com.peter.landing.data.util.DATABASE_NAME
import com.peter.landing.data.util.DATABASE_PATH
import com.peter.landing.util.getTodayDateTime
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class ReviseProgressDAOTest {

    private lateinit var database: LandingDatabase
    private lateinit var dao: ReviseProgressDAO

    private val today = getTodayDateTime()

    @Test
    fun reviseProgressCRUD() = runBlocking {
        val expectReviseProgress = ReviseProgress(0)
        dao.insertReviseProgress(expectReviseProgress)

        var result = dao.getReviseProgressByCreateDate(today)
        assertEquals(expectReviseProgress.revised, result!!.revised)

        expectReviseProgress.revised = 1
        dao.updateReviseProgress(expectReviseProgress)
        result = dao.getReviseProgressByCreateDate(today)!!
        assertEquals(1, result.revised)

        dao.deleteReviseProgress()
        result = dao.getReviseProgressByCreateDate(today)
        assertNull(result)
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.databaseBuilder(
            context,
            LandingDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset(DATABASE_PATH).build()
        dao = database.getReviseProgressDAO()
    }

    @After
    fun clean() {
        database.close()
    }

}