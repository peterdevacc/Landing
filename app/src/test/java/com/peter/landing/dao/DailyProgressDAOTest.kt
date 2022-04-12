package com.peter.landing.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.peter.landing.data.local.LandingDatabase
import com.peter.landing.data.local.plan.StudyPlanDAO
import com.peter.landing.data.local.progress.DailyProgress
import com.peter.landing.data.local.progress.DailyProgressDAO
import com.peter.landing.data.util.DATABASE_NAME
import com.peter.landing.data.util.DATABASE_PATH
import com.peter.landing.debug.expectStudyPlanBeginner
import com.peter.landing.util.getTodayDateTime
import com.peter.landing.util.getYesterdayDateTime
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class DailyProgressDAOTest {

    private lateinit var database: LandingDatabase
    private lateinit var studyPlanDAO: StudyPlanDAO
    private lateinit var dailyProgressDAO: DailyProgressDAO

    private val today = getTodayDateTime()

    @Test
    fun insertAndGetDailyProgressByCreateDate() = runBlocking {
        var result = dailyProgressDAO.getDailyProgressByCreateDate(today)
        assertNull(result)

        val plan = studyPlanDAO.getStudyPlan()!!
        val expect = DailyProgress(plan.id, 0)
        expect.createDate = today
        dailyProgressDAO.insertDailyProgress(expect)
        result = dailyProgressDAO.getDailyProgressByCreateDate(today)

        assertNotNull(result)
        checkDailyProgress(expect, result!!)
    }

    @Test
    fun getDailyProgressBeforeDateAndLastOne() = runBlocking {
        val plan = studyPlanDAO.getStudyPlan()!!
        val expect = DailyProgress(plan.id, 0)
        expect.createDate = getYesterdayDateTime()
        dailyProgressDAO.insertDailyProgress(expect)
        val result = dailyProgressDAO.getDailyProgressBeforeDateAndLastOne(today)!!

        checkDailyProgress(expect, result)
    }

    @Test
    fun getCountInDailyProgress() = runBlocking {
        val result = dailyProgressDAO.getCountInDailyProgress()

        assertEquals(0, result)
    }

    @Test
    fun updateDailyProgressIsFinishedToTrueByCreateDate() = runBlocking {
        val plan = studyPlanDAO.getStudyPlan()!!
        val dailyProgress = DailyProgress(plan.id, 0)
        dailyProgress.createDate = today
        dailyProgressDAO.insertDailyProgress(dailyProgress)
        dailyProgressDAO.updateDailyProgressIsFinishedToTrueByCreateDate(today)

        val result = dailyProgressDAO.getDailyProgressByCreateDate(today)!!
        assertTrue(result.isFinished)
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.databaseBuilder(
            context,
            LandingDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset(DATABASE_PATH).build()
        studyPlanDAO = database.getStudyPlanDAO()
        dailyProgressDAO = database.getDailyProgressDAO()

        runBlocking {
            studyPlanDAO.insertStudyPlan(expectStudyPlanBeginner)
        }
    }

    @After
    fun clean() {
        runBlocking {
            studyPlanDAO.deleteStudyPlan()
        }
        database.close()
    }

    private fun checkDailyProgress(expect: DailyProgress, result: DailyProgress) {
        assertEquals(expect.studyPlanId, result.studyPlanId)
        assertEquals(expect.start, result.start)
        assertEquals(expect.isFinished, result.isFinished)
        assertEquals(expect.createDate, result.createDate)
    }

}