package com.peter.landing.domain

import com.peter.landing.data.local.progress.ReviseProgress
import com.peter.landing.data.repository.progress.ReviseProgressRepository
import com.peter.landing.data.repository.wrong.WrongRepository
import com.peter.landing.debug.getWrongWordListForTest
import com.peter.landing.domain.study.ReviseUseCase
import com.peter.landing.util.LandingCoroutineScope
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ReviseUseCaseTest {

    private val reviseProgressRepository = mockk<ReviseProgressRepository>(relaxed = true)
    private val wrongRepository = mockk<WrongRepository>(relaxed = true)

    private val useCase = ReviseUseCase(
        reviseProgressRepository,
        wrongRepository
    )

    private val scope = LandingCoroutineScope()

    @Test
    fun revised() = runBlocking {
        val reviseProgress = ReviseProgress()
        // list size = 2
        val wrongWordList = getWrongWordListForTest()
        wrongWordList[1].wrong.reviseTimes = 1

        coEvery {
            reviseProgressRepository.getTodayReviseProgress()
        } returns reviseProgress
        coEvery {
            wrongRepository.getTodayReviseWordList()
        } returns wrongWordList

        useCase.initTodayRevise()

        useCase.revised(scope)
        useCase.nextWord()
        coVerify(exactly = 1) {
            wrongRepository.updateWrong(wrongWordList[0].wrong)
        }

        useCase.revised(scope)
        useCase.nextWord()
        coVerify(exactly = 1) {
            wrongRepository.updateWrong(wrongWordList[1].wrong)
            reviseProgressRepository.getTodayReviseProgress()
            wrongRepository.getTodayReviseWordList()
            wrongRepository.updateWrongList(wrongWordList.map { it.wrong })
        }

        coVerify(exactly = 2) {
            reviseProgressRepository.updateReviseProcess(reviseProgress)
        }

        confirmVerified(
            reviseProgressRepository,
            wrongRepository
        )
    }


}