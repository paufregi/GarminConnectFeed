package paufregi.connectfeed.presentation.syncweight

import app.cash.turbine.test
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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.apache.commons.io.IOUtils
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.Weight
import paufregi.connectfeed.core.usecases.SyncStravaWeight
import paufregi.connectfeed.core.usecases.SyncWeight
import paufregi.connectfeed.core.utils.RenphoReader
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import java.time.Instant
import java.util.Date

@ExperimentalCoroutinesApi
class SyncWeightViewModelTest {

    private val syncWeight = mockk<SyncWeight>()
    private val syncStravaWeight = mockk<SyncStravaWeight>()

    private lateinit var viewModel: SyncWeightViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup(){
        mockkObject(RenphoReader)

        viewModel = SyncWeightViewModel(syncWeight, syncStravaWeight)
    }

    @After
    fun tearDown(){
        confirmVerified(syncWeight, RenphoReader)
        clearAllMocks()
        unmockkObject(RenphoReader)
    }

    @Test
    fun `Sync weight`() = runTest {
        val csvText = """
            Time of Measurement,Weight(kg),BMI,Body Fat(%),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat,Body Water(%),Skeletal Muscle(%),Muscle Mass(kg),Bone Mass(kg),Protein(%),BMR(kcal),Metabolic Age,Optimal weight(kg),Target to optimal weight(kg),Target to optimal fat mass(kg),Target to optimal muscle mass(kg),Body Type,Remarks
            2024-01-01 10:20:30,76.15,23.8,23.2,58.48,20.9,7.0,55.4,49.5,55.59,2.89,17.5,1618,35,,,,,,
        """.trimIndent()
        val inputStream = IOUtils.toInputStream(csvText, "UTF-8")
        val weights = listOf(Weight(
            timestamp = Date.from(Instant.parse("2024-01-01T10:20:30Z"),),
            weight = 76.15f,
            bmi = 23.8f,
            fat = 23.2f,
            visceralFat = 7,
            water = 55.4f,
            muscle = 55.59f,
            bone = 2.89f,
            basalMet = 1618f,
            metabolicAge = 35,
        ))

        every { RenphoReader.read(any()) } returns Result.success(weights)
        coEvery { syncWeight.invoke(any()) } returns Result.success(Unit)
        coEvery { syncStravaWeight.invoke(any(), any()) } returns Result.success(Unit)

        viewModel.state.test {
            viewModel.updateWeight(inputStream)
            skipItems(2)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success("Sync succeeded"))
            cancelAndIgnoreRemainingEvents()
        }
        coVerify {
            syncWeight.invoke(weights)
            syncStravaWeight.invoke(weights, any())
        }
        verify { RenphoReader.read(inputStream) }
    }

    @Test
    fun `Sync weight - failure no input stream`() = runTest {
        viewModel.state.test {
            viewModel.updateWeight(null)
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Nothing to sync"))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Sync weight - failure - empty file`() = runTest {
        val inputStream = IOUtils.toInputStream("", "UTF-8")

        every { RenphoReader.read(any()) } returns Result.success(emptyList())

        viewModel.state.test {
            viewModel.updateWeight(inputStream)
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Nothing to sync"))
            cancelAndIgnoreRemainingEvents()
        }
        verify { RenphoReader.read(inputStream) }
    }

    @Test
    fun `Sync weight - failure`() = runTest {
        val csvText = """
            Time of Measurement,Weight(kg),BMI,Body Fat(%),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat,Body Water(%),Skeletal Muscle(%),Muscle Mass(kg),Bone Mass(kg),Protein(%),BMR(kcal),Metabolic Age,Optimal weight(kg),Target to optimal weight(kg),Target to optimal fat mass(kg),Target to optimal muscle mass(kg),Body Type,Remarks
            2024-01-01 10:20:30,76.15,23.8,23.2,58.48,20.9,7.0,55.4,49.5,55.59,2.89,17.5,1618,35,,,,,,
        """.trimIndent()
        val inputStream = IOUtils.toInputStream(csvText, "UTF-8")
        val weights = listOf(Weight(
            timestamp = Date.from(Instant.parse("2024-01-01T10:20:30Z"),),
            weight = 76.15f,
            bmi = 23.8f,
            fat = 23.2f,
            visceralFat = 7,
            water = 55.4f,
            muscle = 55.59f,
            bone = 2.89f,
            basalMet = 1618f,
            metabolicAge = 35,
        ))

        every { RenphoReader.read(any()) } returns Result.success(weights)
        coEvery { syncWeight.invoke(any()) } returns Result.failure("error")
        coEvery { syncStravaWeight.invoke(any(), any()) } returns Result.success(Unit)

        viewModel.state.test {
            viewModel.updateWeight(inputStream)
            skipItems(2)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Garmin sync failed"))
            cancelAndIgnoreRemainingEvents()
        }
        coVerify {
            syncWeight.invoke(weights)
            syncStravaWeight.invoke(weights, any())
        }
        verify { RenphoReader.read(inputStream) }
    }

    @Test
    fun `Sync weight - Strava failure`() = runTest {
        val csvText = """
            Time of Measurement,Weight(kg),BMI,Body Fat(%),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat,Body Water(%),Skeletal Muscle(%),Muscle Mass(kg),Bone Mass(kg),Protein(%),BMR(kcal),Metabolic Age,Optimal weight(kg),Target to optimal weight(kg),Target to optimal fat mass(kg),Target to optimal muscle mass(kg),Body Type,Remarks
            2024-01-01 10:20:30,76.15,23.8,23.2,58.48,20.9,7.0,55.4,49.5,55.59,2.89,17.5,1618,35,,,,,,
        """.trimIndent()
        val inputStream = IOUtils.toInputStream(csvText, "UTF-8")
        val weights = listOf(Weight(
            timestamp = Date.from(Instant.parse("2024-01-01T10:20:30Z"),),
            weight = 76.15f,
            bmi = 23.8f,
            fat = 23.2f,
            visceralFat = 7,
            water = 55.4f,
            muscle = 55.59f,
            bone = 2.89f,
            basalMet = 1618f,
            metabolicAge = 35,
        ))

        every { RenphoReader.read(any()) } returns Result.success(weights)
        coEvery { syncWeight.invoke(any()) } returns Result.success(Unit)
        coEvery { syncStravaWeight.invoke(any(), any()) } returns Result.failure("error")

        viewModel.state.test {
            viewModel.updateWeight(inputStream)
            skipItems(2)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Strava sync failed"))
            cancelAndIgnoreRemainingEvents()
        }
        coVerify {
            syncWeight.invoke(weights)
            syncStravaWeight.invoke(weights, any())
        }
        verify { RenphoReader.read(inputStream) }
    }

    @Test
    fun `Sync weight - all failure`() = runTest {
        val csvText = """
            Time of Measurement,Weight(kg),BMI,Body Fat(%),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat,Body Water(%),Skeletal Muscle(%),Muscle Mass(kg),Bone Mass(kg),Protein(%),BMR(kcal),Metabolic Age,Optimal weight(kg),Target to optimal weight(kg),Target to optimal fat mass(kg),Target to optimal muscle mass(kg),Body Type,Remarks
            2024-01-01 10:20:30,76.15,23.8,23.2,58.48,20.9,7.0,55.4,49.5,55.59,2.89,17.5,1618,35,,,,,,
        """.trimIndent()
        val inputStream = IOUtils.toInputStream(csvText, "UTF-8")
        val weights = listOf(Weight(
            timestamp = Date.from(Instant.parse("2024-01-01T10:20:30Z"),),
            weight = 76.15f,
            bmi = 23.8f,
            fat = 23.2f,
            visceralFat = 7,
            water = 55.4f,
            muscle = 55.59f,
            bone = 2.89f,
            basalMet = 1618f,
            metabolicAge = 35,
        ))

        every { RenphoReader.read(any()) } returns Result.success(weights)
        coEvery { syncWeight.invoke(any()) } returns Result.failure("error")
        coEvery { syncStravaWeight.invoke(any(), any()) } returns Result.failure("error")

        viewModel.state.test {
            viewModel.updateWeight(inputStream)
            skipItems(2)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Garmin & Strava sync failed"))
            cancelAndIgnoreRemainingEvents()
        }
        coVerify {
            syncWeight.invoke(weights)
            syncStravaWeight.invoke(weights, any())
        }
        verify { RenphoReader.read(inputStream) }
    }
}