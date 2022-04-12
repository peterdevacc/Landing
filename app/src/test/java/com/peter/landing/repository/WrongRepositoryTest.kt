package com.peter.landing.repository

import com.peter.landing.data.local.wrong.Wrong
import com.peter.landing.data.local.wrong.WrongDAO
import com.peter.landing.data.repository.wrong.WrongRepository
import com.peter.landing.debug.expectWrongA
import com.peter.landing.util.LandingCoroutineScope
import com.peter.landing.util.getTodayDateTime
import io.mockk.*
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WrongRepositoryTest {

    private val scope = LandingCoroutineScope()
    private val dao = mockk<WrongDAO>(relaxed = true)
    private val repository = WrongRepository(scope, dao)

    private val today = getTodayDateTime()

    @Test
    fun getTodayReviseNum() = runBlocking {

        coEvery {
            dao.countWrongByReviseDateAndReviseTimesLessThanThree(today)
        } returns 0

        repository.getTodayReviseNum()

        coVerify(exactly = 1) {
            dao.countWrongByReviseDateAndReviseTimesLessThanThree(today)
        }

        confirmVerified(dao)
    }

    @Test
    fun getTotalReviseNum() = runBlocking {
        coEvery {
            dao.countWrongByReviseTimesLessThanThree()
        } returns 0

        repository.getTotalReviseNum()

        coVerify(exactly = 1) {
            dao.countWrongByReviseTimesLessThanThree()
        }

        confirmVerified(dao)
    }

    @Test
    fun `markWrongNoted - wrong not exist`() = runBlocking {
        val id: Long = 1
        val wrong = Wrong(id, chosenWrong = true)

        coEvery {
            dao.getWrongById(id)
        } returns null

        coExcludeRecords {
            dao.getWrongById(id)
        }

        repository.markWrongNoted(id)

        coVerify(exactly = 0) {
            dao.updateWrong(wrong)
        }

        confirmVerified(dao)
    }

    @Test
    fun `markWrongNoted - wrong exist`() = runBlocking {
        val id: Long = 1
        val wrong = Wrong(id, chosenWrong = true)

        coEvery {
            dao.getWrongById(id)
        } returns wrong

        coExcludeRecords {
            dao.getWrongById(id)
        }

        repository.markWrongNoted(id)

        coVerify(exactly = 1) {
            wrong.isNoted = true
            dao.updateWrong(wrong)
        }

        confirmVerified(dao)
    }

    @Test
    fun `unMarkWrongNoted - wrong not exist`() = runBlocking {
        val id: Long = 1
        val wrong = Wrong(id, chosenWrong = true)
        wrong.isNoted = true

        coEvery {
            dao.getWrongById(id)
        } returns null

        coExcludeRecords {
            dao.getWrongById(id)
        }

        repository.unMarkWrongNoted(id)

        coVerify(exactly = 0) {
            dao.updateWrong(wrong)
        }

        confirmVerified(dao)
    }

    @Test
    fun `unMarkWrongNoted - wrong exist`() = runBlocking {
        val id: Long = 1
        val wrong = Wrong(id, chosenWrong = true)
        wrong.isNoted = true

        coEvery {
            dao.getWrongById(id)
        } returns wrong

        coExcludeRecords {
            dao.getWrongById(id)
        }

        repository.unMarkWrongNoted(id)

        coVerify(exactly = 1) {
            wrong.isNoted = false
            dao.updateWrong(wrong)
        }

        confirmVerified(dao)
    }

    @Test
    fun `addWrong - choose wrong only`() = runBlocking {
        val id: Long = 1
        val type = Wrong.Type.CHOOSE
        val wrong = Wrong(id, chosenWrong = true)

        repository.addWrong(id, type)

        coVerify(exactly = 1) {
            dao.insertWrong(wrong)
        }

        coVerify(exactly = 0) {
            dao.updateWrong(wrong)
        }

        confirmVerified(dao)
    }

    @Test
    fun `addWrong - spell wrong only`() = runBlocking {
        val id: Long = 1
        val type = Wrong.Type.SPELLING
        val wrong = Wrong(id, spelledWrong = true)

        coEvery {
            dao.getWrongById(id)
        } returns null

        repository.addWrong(id, type)

        coVerify(exactly = 1) {
            dao.getWrongById(id)
            dao.insertWrong(wrong)
        }

        coVerify(exactly = 0) {
            dao.updateWrong(wrong)
        }

        confirmVerified(dao)
    }

    @Test
    fun `addWrong - spell wrong after choose wrong`() = runBlocking {
        val id: Long = 1
        val type = Wrong.Type.SPELLING
        val wrong = Wrong(id, chosenWrong = true)

        coEvery {
            dao.getWrongById(id)
        } returns wrong

        repository.addWrong(id, type)

        coVerify(exactly = 1) {
            dao.getWrongById(id)
        }

        coVerify(exactly = 0) {
            dao.insertWrong(wrong)
        }

        coVerify(exactly = 1) {
            wrong.spelledWrong = true
            dao.updateWrong(wrong)
        }

        confirmVerified(dao)
    }

    @Test
    fun addWrongList() = runBlocking {
        val wrongList = listOf(expectWrongA)

        repository.addWrongList(wrongList)

        coVerify(exactly = 1) {
            dao.insertWrongList(wrongList)
        }

        confirmVerified(dao)
    }

    @Test
    fun updateWrong() = runBlocking {
        repository.updateWrong(expectWrongA)

        coVerify(exactly = 1) {
            dao.updateWrong(expectWrongA)
        }

        confirmVerified(dao)
    }

    @Test
    fun updateWrongList() = runBlocking {
        val wrongList = listOf(expectWrongA)

        repository.updateWrongList(wrongList)

        coVerify(exactly = 1) {
            dao.updateWrongList(wrongList)
        }

        confirmVerified(dao)
    }

    @Test
    fun removeWrong() = runBlocking {
        repository.removeWrong()

        coVerify(exactly = 1) {
            dao.deleteWrong()
        }

        confirmVerified(dao)
    }

    @Test
    fun getTodayChosenWrongWordListFlow() = runBlocking {
        repository.getTodayChosenWrongWordListFlow()

        coVerify(exactly = 1) {
            dao.getWrongWordListByAddDateAndChosenWrongFlow(today)
        }

        confirmVerified(dao)
    }

    @Test
    fun getTodaySpelledWrongWordListFlow() = runBlocking {
        repository.getTodaySpelledWrongWordListFlow()

        coVerify(exactly = 1) {
            dao.getWrongWordListByAddDateAndSpelledWrongFlow(today)
        }

        confirmVerified(dao)
    }

    @Test
    fun getWrongWordListFlow() = runBlocking {
        repository.getWrongWordFlow().take(1).toList()

        coVerify(exactly = 1) {
            dao.getWrongWordPaging()
        }

        confirmVerified(dao)
    }

    @Test
    fun getTodayReviseWordList() = runBlocking {
        repository.getTodayReviseWordList()

        coVerify(exactly = 1) {
            dao.getWrongWordListByReviseDateAndReviseTimeLessThanFour(today)
        }

        confirmVerified(dao)
    }

}