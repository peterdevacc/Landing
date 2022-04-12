package com.peter.landing.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.peter.landing.data.local.LandingDatabase
import com.peter.landing.data.local.plan.StudyPlanDAO
import com.peter.landing.data.local.progress.DailyProgress
import com.peter.landing.data.local.progress.DailyProgressDAO
import com.peter.landing.data.local.progress.SpellingProgress
import com.peter.landing.data.local.progress.SpellingProgressDAO
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
class SpellingProgressDAOTest {

    private lateinit var database: LandingDatabase
    private lateinit var studyPlanDAO: StudyPlanDAO
    private lateinit var dailyProgressDAO: DailyProgressDAO
    private lateinit var spellingProgressDAO: SpellingProgressDAO

    private val today = getTodayDateTime()

    @Test
    fun spellingProgressCRU() = runBlocking {
        val dailyProgress = dailyProgressDAO.getDailyProgressByCreateDate(today)!!
        val expectSpellingProgress = SpellingProgress(dailyProgress.id)
        spellingProgressDAO.insertSpellingProgress(expectSpellingProgress)
        var result = spellingProgressDAO.getSpellingProgressByDailyProgressId(dailyProgress.id)

        assertEquals(expectSpellingProgress.dailyProgressId, result.dailyProgressId)
        assertEquals(expectSpellingProgress.spelled, result.spelled)
        assertEquals(expectSpellingProgress.spellCorrect, result.spellCorrect)

        result.spelled = 1
        result.spellCorrect = 1
        spellingProgressDAO.updateSpellingProgress(result)
        result = spellingProgressDAO.getSpellingProgressByDailyProgressId(dailyProgress.id)
        assertEquals(1, result.spelled)
        assertEquals(1, result.spellCorrect)
    }

    @Test
    fun getSpelledSumInSpellingProgress() = runBlocking {
        val result = spellingProgressDAO.getSpelledSumInSpellingProgress()

        assertEquals(0, result)
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
        spellingProgressDAO = database.getSpellingProgressDAO()

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