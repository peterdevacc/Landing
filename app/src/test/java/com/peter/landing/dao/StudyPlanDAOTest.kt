package com.peter.landing.dao

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.peter.landing.data.local.LandingDatabase
import com.peter.landing.data.local.plan.StudyPlanDAO
import com.peter.landing.data.util.DATABASE_NAME
import com.peter.landing.data.util.DATABASE_PATH
import com.peter.landing.debug.expectStudyPlanBeginner
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
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
class StudyPlanDAOTest {

    private lateinit var database: LandingDatabase
    private lateinit var dao: StudyPlanDAO

    @Test
    fun studyPlanCRD() = runBlocking {
        var result = dao.getStudyPlan()
        assertNull(result)

        dao.insertStudyPlan(expectStudyPlanBeginner)
        result = dao.getStudyPlan()
        assertNotNull(result)
        assertEquals(expectStudyPlanBeginner.vocabularyName, result!!.vocabularyName)
        assertEquals(expectStudyPlanBeginner.wordListSize, result.wordListSize)
        assertEquals(expectStudyPlanBeginner.startDate, result.startDate)

        dao.deleteStudyPlan()
        result = dao.getStudyPlan()
        assertNull(result)
    }

    @Test
    fun studyPlanFlow() = runBlocking {
        dao.insertStudyPlan(expectStudyPlanBeginner)

        val flow = dao.getStudyPlanFlow()
        val result = flow.take(1).toList().first()
        assertEquals(expectStudyPlanBeginner.vocabularyName, result.vocabularyName)
        assertEquals(expectStudyPlanBeginner.wordListSize, result.wordListSize)
        assertEquals(expectStudyPlanBeginner.startDate, result.startDate)

        dao.deleteStudyPlan()
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.databaseBuilder(
            context,
            LandingDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset(DATABASE_PATH).build()
        dao = database.getStudyPlanDAO()
    }

    @After
    fun clean() {
        database.close()
    }

}