package com.peter.landing.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.peter.landing.data.local.LandingDatabase
import com.peter.landing.data.local.plan.StudyPlanDAO
import com.peter.landing.data.local.progress.DailyProgress
import com.peter.landing.data.local.progress.DailyProgressDAO
import com.peter.landing.data.local.progress.TypingProgress
import com.peter.landing.data.local.progress.TypingProgressDAO
import com.peter.landing.data.util.DATABASE_NAME
import com.peter.landing.data.util.DATABASE_PATH
import com.peter.landing.debug.expectStudyPlanBeginner
import com.peter.landing.util.getTodayDateTime
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
class TypingProgressDAOTest {

    private lateinit var database: LandingDatabase
    private lateinit var studyPlanDAO: StudyPlanDAO
    private lateinit var dailyProgressDAO: DailyProgressDAO
    private lateinit var typingProgressDAO: TypingProgressDAO

    private val today = getTodayDateTime()

    @Test
    fun typingProgressCRU() = runBlocking {
        val dailyProgress = dailyProgressDAO.getDailyProgressByCreateDate(today)!!
        val expectTypingProgress = TypingProgress(dailyProgress.id)
        typingProgressDAO.insertTypingProgress(expectTypingProgress)
        var result = typingProgressDAO.getTypingProgressByDailyProgressId(dailyProgress.id)

        assertEquals(expectTypingProgress.dailyProgressId, result.dailyProgressId)
        assertEquals(expectTypingProgress.typed, result.typed)

        result.typed = 1
        typingProgressDAO.updateTypingProgress(result)
        result = typingProgressDAO.getTypingProgressByDailyProgressId(dailyProgress.id)
        assertEquals(1, result.typed)
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
        typingProgressDAO = database.getTypingProgressDAO()

        runBlocking {
            studyPlanDAO.insertStudyPlan(expectStudyPlanBeginner)
            val plan = studyPlanDAO.getStudyPlan()!!
            val expectDailyProgress = DailyProgress(plan.id, 0)
            expectDailyProgress.createDate = today
            dailyProgressDAO.insertDailyProgress(expectDailyProgress)
        }
    }

    @After
    fun clean() {
        runBlocking {
            studyPlanDAO.deleteStudyPlan()
        }
        database.close()
    }

}