package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import paufregi.connectfeed.core.models.Weight
import paufregi.connectfeed.core.utils.FitWriter
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.GarminRepository
import paufregi.connectfeed.data.repository.StravaRepository
import java.util.Date


class SyncWeightTest {
    @Rule
    @JvmField
    var folder: TemporaryFolder = TemporaryFolder()

    private val isStravaConnected = mockk<IsStravaConnected>()
    private val garminRepo = mockk<GarminRepository>()
    private val stravaRepo = mockk<StravaRepository>()
    private lateinit var useCase: SyncWeight

    @Before
    fun setup(){
        mockkObject(Formatter)
        mockkObject(FitWriter)

        every { Formatter.dateTimeForFilename(any()).format(any()) } returns "20240101_000000"
        every { FitWriter.weights(any(), any()) } returns Unit

        useCase = SyncWeight(isStravaConnected, garminRepo, stravaRepo, folder.newFolder())
    }

    @After
    fun tearDown(){
        verify {
            isStravaConnected()
            Formatter.dateTimeForFilename(any()).format(any())
            FitWriter.weights(any(), weights)
        }
        confirmVerified(isStravaConnected, garminRepo, stravaRepo, Formatter, FitWriter)
        clearAllMocks()
        unmockkObject(Formatter::class)
        unmockkObject(FitWriter::class)
    }

    val today = Date()
    val yesterday = Date(today.time - 24 * 60 * 60 * 1000)

    val weights = listOf(Weight(
        timestamp = today,
        weight = 76.15f,
        bmi = 23.8f,
        fat = 23.2f,
        visceralFat = 7,
        water = 55.4f,
        muscle = 55.59f,
        bone = 2.89f,
        basalMet = 1618f,
        metabolicAge = 35,
    ),
        Weight(
            timestamp = yesterday,
            weight = 75.15f,
            bmi = 23.8f,
            fat = 23.2f,
            visceralFat = 7,
            water = 55.4f,
            muscle = 55.59f,
            bone = 2.89f,
            basalMet = 1618f,
            metabolicAge = 35,
        )
    )

    @Test
    fun `Sync weight`() = runTest {
        every { isStravaConnected() } returns flowOf(false)
        coEvery { garminRepo.uploadFile(any()) } returns Result.success(Unit)

        val res = useCase(weights, today)

        assertThat(res.isSuccess).isTrue()

        coVerify { garminRepo.uploadFile(any()) }
    }

    @Test
    fun `Sync weight with Strava`() = runTest {
        every { isStravaConnected() } returns flowOf(true)
        coEvery { garminRepo.uploadFile(any()) } returns Result.success(Unit)
        coEvery { stravaRepo.updateAthlete(any()) } returns Result.success(Unit)

        val res = useCase(weights, today)

        assertThat(res.isSuccess).isTrue()

        coVerify { garminRepo.uploadFile(any()) }
        coVerify { stravaRepo.updateAthlete(weights[0].weight) }
    }

    @Test
    fun `Sync weight - garmin failed`() = runTest {
        every { isStravaConnected() } returns flowOf(true)
        coEvery { garminRepo.uploadFile(any()) } returns Result.failure("error")
        coEvery { stravaRepo.updateAthlete(any()) } returns Result.success(Unit)

        val res = useCase(weights, today)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Couldn't update Garmin")


        coVerify { garminRepo.uploadFile(any()) }
        coVerify { stravaRepo.updateAthlete(weights[0].weight) }
    }

    @Test
    fun `Sync weight - strava failed`() = runTest {
        every { isStravaConnected() } returns flowOf(true)
        coEvery { garminRepo.uploadFile(any()) } returns Result.success(Unit)
        coEvery { stravaRepo.updateAthlete(any()) } returns Result.failure("error")

        val res = useCase(weights, today)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Couldn't update Strava")

        coVerify {
            garminRepo.uploadFile(any())
            stravaRepo.updateAthlete(weights[0].weight)
        }
    }

    @Test
    fun `Sync weight - both failed`() = runTest {
        every { isStravaConnected() } returns flowOf(true)
        coEvery { garminRepo.uploadFile(any()) } returns Result.failure("error")
        coEvery { stravaRepo.updateAthlete(any()) } returns Result.failure("error")

        val res = useCase(weights, today)

        assertThat(res.isSuccess).isFalse()
        assertThat(res.exceptionOrNull()?.message).isEqualTo("Couldn't update Garmin & Strava")

        coVerify {
            garminRepo.uploadFile(any())
            stravaRepo.updateAthlete(weights[0].weight)
        }
    }
}
