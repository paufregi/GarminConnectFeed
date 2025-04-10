package paufregi.connectfeed.presentation.syncweight

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.apache.commons.io.IOUtils
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.usecases.SyncStravaWeight
import paufregi.connectfeed.core.usecases.SyncWeight
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class SyncWeightViewModelTest {

    private val syncWeight = mockk<SyncWeight>()
    private val syncStravaWeight = mockk<SyncStravaWeight>()

    private lateinit var viewModel: SyncWeightViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup(){
        viewModel = SyncWeightViewModel(syncWeight, syncStravaWeight)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Sync weight`() = runTest {
        val csvText = """
            Time of Measurement,Weight(kg),BMI,Body Fat(%),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat,Body Water(%),Skeletal Muscle(%),Muscle Mass(kg),Bone Mass(kg),Protein(%),BMR(kcal),Metabolic Age,Optimal weight(kg),Target to optimal weight(kg),Target to optimal fat mass(kg),Target to optimal muscle mass(kg),Body Type,Remarks
            2024-01-01 10:20:30,76.15,23.8,23.2,58.48,20.9,7.0,55.4,49.5,55.59,2.89,17.5,1618,35,,,,,,
        """.trimIndent()

        val inputStream = IOUtils.toInputStream(csvText, "UTF-8")

        coEvery { syncWeight.invoke(any()) } returns Result.Success(Unit)
        coEvery { syncStravaWeight.invoke(any(), any()) } returns Result.Success(Unit)

        viewModel.state.test {
            viewModel.updateWeight(inputStream)
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success())
            cancelAndIgnoreRemainingEvents()
        }
        coVerify {
            syncWeight.invoke(inputStream)
            syncStravaWeight.invoke(inputStream, any())
        }
        confirmVerified(syncWeight)
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
        confirmVerified(syncWeight)
    }

    @Test
    fun `Sync weight - failure`() = runTest {
        val csvText = """
            Time of Measurement,Weight(kg),BMI,Body Fat(%),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat,Body Water(%),Skeletal Muscle(%),Muscle Mass(kg),Bone Mass(kg),Protein(%),BMR(kcal),Metabolic Age,Optimal weight(kg),Target to optimal weight(kg),Target to optimal fat mass(kg),Target to optimal muscle mass(kg),Body Type,Remarks
            2024-01-01 10:20:30,76.15,23.8,23.2,58.48,20.9,7.0,55.4,49.5,55.59,2.89,17.5,1618,35,,,,,,
        """.trimIndent()

        val inputStream = IOUtils.toInputStream(csvText, "UTF-8")

        coEvery { syncWeight.invoke(any()) } returns Result.Failure("error")
        coEvery { syncStravaWeight.invoke(any(), any()) } returns Result.Success(Unit)

        viewModel.state.test {
            viewModel.updateWeight(inputStream)
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Garmin sync failed"))
            cancelAndIgnoreRemainingEvents()
        }
        coVerify {
            syncWeight.invoke(inputStream)
            syncStravaWeight.invoke(inputStream, any())
        }
        confirmVerified(syncWeight)
    }

    @Test
    fun `Sync weight - Strava failure`() = runTest {
        val csvText = """
            Time of Measurement,Weight(kg),BMI,Body Fat(%),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat,Body Water(%),Skeletal Muscle(%),Muscle Mass(kg),Bone Mass(kg),Protein(%),BMR(kcal),Metabolic Age,Optimal weight(kg),Target to optimal weight(kg),Target to optimal fat mass(kg),Target to optimal muscle mass(kg),Body Type,Remarks
            2024-01-01 10:20:30,76.15,23.8,23.2,58.48,20.9,7.0,55.4,49.5,55.59,2.89,17.5,1618,35,,,,,,
        """.trimIndent()

        val inputStream = IOUtils.toInputStream(csvText, "UTF-8")

        coEvery { syncWeight.invoke(any()) } returns Result.Success(Unit)
        coEvery { syncStravaWeight.invoke(any(), any()) } returns Result.Failure("error")

        viewModel.state.test {
            viewModel.updateWeight(inputStream)
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Strava sync failed"))
            cancelAndIgnoreRemainingEvents()
        }
        coVerify {
            syncWeight.invoke(inputStream)
            syncStravaWeight.invoke(inputStream, any())
        }
        confirmVerified(syncWeight)
    }

    @Test
    fun `Sync weight - all failure`() = runTest {
        val csvText = """
            Time of Measurement,Weight(kg),BMI,Body Fat(%),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat,Body Water(%),Skeletal Muscle(%),Muscle Mass(kg),Bone Mass(kg),Protein(%),BMR(kcal),Metabolic Age,Optimal weight(kg),Target to optimal weight(kg),Target to optimal fat mass(kg),Target to optimal muscle mass(kg),Body Type,Remarks
            2024-01-01 10:20:30,76.15,23.8,23.2,58.48,20.9,7.0,55.4,49.5,55.59,2.89,17.5,1618,35,,,,,,
        """.trimIndent()

        val inputStream = IOUtils.toInputStream(csvText, "UTF-8")

        coEvery { syncWeight.invoke(any()) } returns Result.Failure("error")
        coEvery { syncStravaWeight.invoke(any(), any()) } returns Result.Failure("error")

        viewModel.state.test {
            viewModel.updateWeight(inputStream)
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Garmin & Strava sync failed"))
            cancelAndIgnoreRemainingEvents()
        }
        coVerify {
            syncWeight.invoke(inputStream)
            syncStravaWeight.invoke(inputStream, any())
        }
        confirmVerified(syncWeight)
    }
}