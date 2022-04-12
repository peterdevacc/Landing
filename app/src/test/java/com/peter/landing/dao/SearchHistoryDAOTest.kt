package com.peter.landing.dao

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.peter.landing.data.local.LandingDatabase
import com.peter.landing.data.local.history.SearchHistoryDAO
import com.peter.landing.data.util.DATABASE_NAME
import com.peter.landing.data.util.DATABASE_PATH
import com.peter.landing.debug.expectSearchHistoryA
import com.peter.landing.debug.expectSearchHistoryB
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
class SearchHistoryDAOTest {

    private lateinit var database: LandingDatabase
    private lateinit var dao: SearchHistoryDAO

    @Test
    fun searchHistoryTest() = runBlocking {
        dao.insertSearchHistory(expectSearchHistoryA)
        dao.insertSearchHistory(expectSearchHistoryB)

        var totalNum = dao.countSearchHistory()
        assertEquals(2, totalNum)

        val resultPage = dao.getSearchHistoryListOrderBySearchDatePaging().load(
            PagingSource.LoadParams.Refresh(
                null,
                30,
                false
            )
        ) as PagingSource.LoadResult.Page

        val result = resultPage.data
        assertEquals(2, result.size)
        assertEquals(expectSearchHistoryA.input, result[0].input)
        assertEquals(expectSearchHistoryA.searchDate, result[0].searchDate)
        assertEquals(expectSearchHistoryB.input, result[1].input)
        assertEquals(expectSearchHistoryB.searchDate, result[1].searchDate)

        dao.deleteSearchHistoryList()

        totalNum = dao.countSearchHistory()
        assertEquals(0, totalNum)
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.databaseBuilder(
            context,
            LandingDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset(DATABASE_PATH).build()
        dao = database.getSearchHistoryDAO()
    }

    @After
    fun clean() {
        database.close()
    }

}