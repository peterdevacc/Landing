package com.peter.landing.dao

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.peter.landing.data.local.LandingDatabase
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.local.word.WordDAO
import com.peter.landing.data.local.wrong.Wrong
import com.peter.landing.data.local.wrong.WrongDAO
import com.peter.landing.data.local.wrong.WrongWord
import com.peter.landing.data.util.DATABASE_NAME
import com.peter.landing.data.util.DATABASE_PATH
import com.peter.landing.debug.expectWrongA
import com.peter.landing.debug.expectWrongB
import com.peter.landing.debug.getWrongWordListForTest
import com.peter.landing.util.getTodayDateTime
import com.peter.landing.util.getTomorrowDateTime
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
class WrongDAOTest {

    private lateinit var database: LandingDatabase
    private lateinit var wordDAO: WordDAO
    private lateinit var wrongDAO: WrongDAO

    private val today = getTodayDateTime()
    private val tomorrow = getTomorrowDateTime()

    @Test
    fun wrongCRUD() = runBlocking {
        wrongDAO.insertWrong(expectWrongA)
        var result = wrongDAO.getWrongById(expectWrongA.wordId)

        assertNotNull(result)
        wrongCheck(expectWrongA, result!!)

        result.spelledWrong = true
        wrongDAO.updateWrong(result)
        result = wrongDAO.getWrongById(expectWrongA.wordId)!!
        assertTrue(result.spelledWrong)

        wrongDAO.deleteWrong()
        result = wrongDAO.getWrongById(expectWrongA.wordId)
        assertNull(result)
    }

    @Test
    fun wrongListCU() = runBlocking {
        val expect = listOf(expectWrongA)
        wrongDAO.insertWrongList(expect)

        var result = wrongDAO.getWrongById(expectWrongA.wordId)
        wrongCheck(expect.first(), result!!)

        expect.forEach {
            it.spelledWrong = true
        }
        wrongDAO.updateWrongList(expect)
        result = wrongDAO.getWrongById(expectWrongA.wordId)!!
        assertTrue(result.spelledWrong)

        wrongDAO.deleteWrong()
    }

    @Test
    fun countWrongByReviseTimesLessThanFour() = runBlocking {
        expectWrongB.reviseTimes = 5
        val wrongList = listOf(expectWrongA, expectWrongB)
        wrongDAO.insertWrongList(wrongList)
        val expect = wrongList.size - 1

        val result = wrongDAO.countWrongByReviseTimesLessThanThree()
        assertEquals(expect, result)

        expectWrongB.reviseTimes = 4
        wrongDAO.deleteWrong()
    }

    @Test
    fun countWrongByReviseDate() = runBlocking {
        val today = getTodayDateTime()
        expectWrongA.reviseDate = today
        val wrongList = listOf(expectWrongA, expectWrongB)
        wrongDAO.insertWrongList(wrongList)
        val expect = wrongList.size - 1

        val result = wrongDAO.countWrongByReviseDateAndReviseTimesLessThanThree(today)
        assertEquals(expect, result)

        expectWrongB.reviseDate = getTomorrowDateTime()
        wrongDAO.deleteWrong()
    }

    @Test
    fun getTotalWrongWordPaging() = runBlocking {
        val expect = getWrongWordListForTest()
        val wrongList = expect.map { it.wrong }
        wrongDAO.insertWrongList(wrongList)
        val resultPage = wrongDAO.getWrongWordPaging().load(
            PagingSource.LoadParams.Refresh(
                null,
                30,
                false
            )
        ) as PagingSource.LoadResult.Page

        val result = resultPage.data
        assertEquals(expect.size, result.size)
        result.zip(expect).forEach {
            wrongWordCheck(it.second, it.first)
        }

        wrongDAO.deleteWrong()
    }

    @Test
    fun getWrongWordListFlowByAddDateAndChosenWrong() = runBlocking {
        val expect = getWrongWordListForTest()
        val wrongList = expect.map { it.wrong }
        wrongDAO.insertWrongList(wrongList)
        val resultFlow = wrongDAO.getWrongWordListByAddDateAndChosenWrongFlow(today)
        val result = resultFlow.take(1).toList().first()
        result.zip(expect).forEach {
            wrongWordCheck(it.first, it.second)
        }

        wrongDAO.deleteWrong()
    }

    @Test
    fun getWrongWordListFlowByAddDateAndSpelledWrong() = runBlocking {
        val expect = getWrongWordListForTest()
        expect.forEach {
            it.wrong.spelledWrong = true
        }
        val wrongList = expect.map { it.wrong }
        wrongDAO.insertWrongList(wrongList)
        val resultFlow = wrongDAO.getWrongWordListByAddDateAndSpelledWrongFlow(today)
        val result = resultFlow.take(1).toList().first()
        result.zip(expect).forEach {
            wrongWordCheck(it.first, it.second)
        }

        wrongDAO.deleteWrong()
    }

    @Test
    fun getWrongWordListByReviseDateAndReviseTimeLessThanFour() = runBlocking {
        val expect = getWrongWordListForTest()
        val wrongList = expect.map { it.wrong }
        wrongDAO.insertWrongList(wrongList)
        val result = wrongDAO
            .getWrongWordListByReviseDateAndReviseTimeLessThanFour(tomorrow)
        result.zip(expect).forEach {
            wrongWordCheck(it.first, it.second)
        }

        wrongDAO.deleteWrong()
    }

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.databaseBuilder(
            context,
            LandingDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset(DATABASE_PATH).build()
        wordDAO = database.getWordDAO()
        wrongDAO = database.getWrongDAO()
    }

    @After
    fun clean() {
        database.close()
    }

    private fun wrongWordCheck(expect: WrongWord, result: WrongWord) {
        wrongCheck(expect.wrong, result.wrong)
        wordCheck(expect.word, result.word)
    }

    private fun wrongCheck(expect: Wrong, result: Wrong) {
        assertEquals(expect.chosenWrong, result.chosenWrong)
        assertEquals(expect.spelledWrong, result.spelledWrong)
        assertEquals(expect.reviseDate, result.reviseDate)
        assertEquals(expect.reviseTimes, result.reviseTimes)
        assertEquals(expect.addDate, result.addDate)
        assertEquals(expect.isNoted, result.isNoted)
    }

    private fun wordCheck(expect: Word, result: Word) {
        assertEquals(expect.spelling, result.spelling)
        assertEquals(expect.ipa, result.ipa)
        assertEquals(expect.cn, result.cn)
        assertEquals(expect.en, result.en)
        assertEquals(expect.pronName, result.pronName)
    }

}