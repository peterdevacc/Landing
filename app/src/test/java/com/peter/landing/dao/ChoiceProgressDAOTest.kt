package com.peter.landing.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.peter.landing.data.local.LandingDatabase
import com.peter.landing.data.local.plan.StudyPlanDAO
import com.peter.landing.data.local.progress.ChoiceProgress
import com.peter.landing.data.local.progress.ChoiceProgressDAO
import com.peter.landing.data.local.progress.DailyProgress
import com.peter.landing.data.local.progress.DailyProgressDAO
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
class ChoiceProgressDAOTest {

    private lateinit var database: LandingDatabase
    private lateinit var studyPlanDAO: StudyPlanDAO
    private lateinit var dailyProgressDAO: DailyProgressDAO
    private lateinit var choiceProgressDAO: ChoiceProgressDAO

    private val today = getTodayDateTime()

    @Test
    fun choiceProgressCRU() = runBlocking {
        val dailyProgress = dailyProgressDAO.getDailyProgressByCreateDate(today)!!
        val expectChoiceProgress = ChoiceProgress(dailyProgress.id)
        choiceProgressDAO.insertChoiceProgress(expectChoiceProgress)
        var result = choiceProgressDAO.getChoiceProgressByDailyProgressId(dailyProgress.id)

        assertEquals(expectChoiceProgress.dailyProgressId, result.dailyProgressId)
        assertEquals(expectChoiceProgress.chosen, result.chosen)
        assertEquals(expectChoiceProgress.chooseCorrect, result.chooseCorrect)

        result.chosen = 1
        result.chooseCorrect = 1
        choiceProgressDAO.updateChoiceProgress(result)
        result = choiceProgressDAO.getChoiceProgressByDailyProgressId(dailyProgress.id)
        assertEquals(1, result.chosen)
        assertEquals(1, result.chooseCorrect)
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
        choiceProgressDAO = database.getChoiceProgressDAO()

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