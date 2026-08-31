package paufregi.connectfeed.presentation.gears

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.usecases.GetGears
import paufregi.connectfeed.core.usecases.SyncGear
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class GearsViewModelTest {

    private val getGears = mockk<GetGears>()
    private val syncGear = mockk<SyncGear>()

    private lateinit var viewModel: GearsViewModel

    private val gears = listOf(
        Gear(id = "gear-1", name = "Daily trainers", type = GearType.Shoe, distance = 12345),
        Gear(id = "gear-2", name = "Weekend bike", type = GearType.Bike, distance = 67890),
    )

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {}

    @After
    fun tearDown() {
        verify { getGears() }
        confirmVerified(getGears, syncGear)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        every { getGears() } returns flowOf(gears)

        viewModel = GearsViewModel(getGears, syncGear)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.gears).isEqualTo(gears)
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Initial state - empty list`() = runTest {
        every { getGears() } returns flowOf(emptyList())

        viewModel = GearsViewModel(getGears, syncGear)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.gears).isEqualTo(emptyList<Gear>())
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Sync gears - processing`() = runTest {
        every { getGears() } returns flowOf(gears)
        coEvery { syncGear() } coAnswers { Result.success(Unit) }

        viewModel = GearsViewModel(getGears, syncGear)

        viewModel.state.test {
            assertThat(awaitItem().process).isEqualTo(ProcessState.Idle)
            viewModel.onAction(GearsAction.Sync)
            assertThat(awaitItem().process).isEqualTo(ProcessState.Success("Gears synced"))
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { syncGear() }
    }

    @Test
    fun `Sync gears - success`() = runTest {
        every { getGears() } returns flowOf(gears)
        coEvery { syncGear() } returns Result.success(Unit)

        viewModel = GearsViewModel(getGears, syncGear)

        viewModel.state.test {
            assertThat(awaitItem().process).isEqualTo(ProcessState.Idle)
            viewModel.onAction(GearsAction.Sync)
            assertThat(awaitItem().process).isEqualTo(ProcessState.Success("Gears synced"))
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { syncGear() }
    }

    @Test
    fun `Sync gears - failure`() = runTest {
        every { getGears() } returns flowOf(gears)
        coEvery { syncGear() } returns Result.failure(Exception("error"))

        viewModel = GearsViewModel(getGears, syncGear)

        viewModel.state.test {
            assertThat(awaitItem().process).isEqualTo(ProcessState.Idle)
            viewModel.onAction(GearsAction.Sync)
            assertThat(awaitItem().process).isEqualTo(ProcessState.Failure("error"))
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { syncGear() }
    }

    @Test
    fun `Reset state`() = runTest {
        every { getGears() } returns flowOf(gears)
        coEvery { syncGear() } returns Result.success(Unit)

        viewModel = GearsViewModel(getGears, syncGear)

        viewModel.state.test {
            assertThat(awaitItem().process).isEqualTo(ProcessState.Idle)
            viewModel.onAction(GearsAction.Sync)
            assertThat(awaitItem().process).isEqualTo(ProcessState.Success("Gears synced"))
            viewModel.onAction(GearsAction.Reset)
            assertThat(awaitItem().process).isEqualTo(ProcessState.Idle)
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { syncGear() }
    }
}


