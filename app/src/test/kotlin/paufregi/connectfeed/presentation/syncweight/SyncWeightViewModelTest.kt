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
import paufregi.connectfeed.core.usecases.SyncWeight
import paufregi.connectfeed.core.utils.Formatter
import paufregi.connectfeed.core.utils.RenphoReader
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import java.io.InputStream
import java.time.Instant
import java.util.Date
import java.util.Locale

@ExperimentalCoroutinesApi
class SyncWeightViewModelTest {

    private val syncWeight = mockk<SyncWeight>()

    private lateinit var viewModel: SyncWeightViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup(){
        mockkObject(RenphoReader)

        viewModel = SyncWeightViewModel(syncWeight)
    }

    @After
    fun tearDown(){
        confirmVerified(syncWeight, RenphoReader)
        clearAllMocks()
        unmockkObject(RenphoReader)
    }

    val formatter = Formatter.dateTimeForImport(Locale.getDefault())
    val csvText = """
            No.,Date,Time,Weight(kg),BMI,Body Fat Percentage(%),Body Fat Mass(kg),Muscle Percentage(%),Muscle Mass(kg),Skeletal Muscle Percentage(%),Skeletal Muscle Mass(kg),Bone Percentage(%),Bone Mass(kg),Protein Percentage(%),Protein Mass(kg),Body Water Percentage(%),Body Water Mass(kg),Fat-Free Mass(kg),Subcutaneous Fat(%),Visceral Fat,BMR(kcal),Metabolic Age,WHR (Waist-to-Hip Ratio),Optimal Weight(kg),Weight Level,Body Type,Target to optimal weight(kg),Target to optimal muscle mass(kg),Target to optimal fat mass(kg),Remarks,
            1,2026.08.16,07:59:46,74.15,23.1,14.1,10.46,81.6,60.51,55.5,41.15,4.3,3.18,19.6,14.53,62.0,45.97,63.69,12.3,6,1743,36,--,--,--,--,--,--,--,,
        """.trimIndent()
    val inputStream: InputStream = IOUtils.toInputStream(csvText, "UTF-8")
    val weights = listOf(Weight(
        timestamp = formatter.parse("2026.08.16 07:59:46")!!,
        weight = 74.15f,
        bmi = 23.1f,
        fat = 14.1f,
        visceralFat = 6,
        water = 62.0f,
        muscle = 60.51f,
        bone = 3.18f,
        basalMet = 1743f,
        metabolicAge = 36,
    ))

    @Test
    fun `Sync weight`() = runTest {
        every { RenphoReader.read(any()) } returns Result.success(weights)
        coEvery { syncWeight(any(), any()) } returns Result.success(Unit)

        viewModel.state.test {
            viewModel.updateWeight(inputStream)
            skipItems(1)
            assertThat(awaitItem().process).isEqualTo(ProcessState.Success("Sync succeeded"))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { syncWeight(weights, any()) }
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
            assertThat(awaitItem().process).isEqualTo(ProcessState.Failure("Nothing to sync"))
            cancelAndIgnoreRemainingEvents()
        }
        verify { RenphoReader.read(inputStream) }
    }

    @Test
    fun `Sync weight - failure`() = runTest {
        every { RenphoReader.read(any()) } returns Result.success(weights)
        coEvery { syncWeight(any(), any()) } returns Result.failure("error")

        viewModel.state.test {
            viewModel.updateWeight(inputStream)
            skipItems(1)
            assertThat(awaitItem().process).isEqualTo(ProcessState.Failure("error"))
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { syncWeight(weights, any()) }
        verify { RenphoReader.read(inputStream) }
    }
}