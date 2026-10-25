package paufregi.connectfeed.presentation.app.gears

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
import paufregi.connectfeed.presentation.ui.components.notification.NotificationManager
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class GearsViewModelTest {

    private val getGears = mockk<GetGears>()
    private val syncGear = mockk<SyncGear>()
    private val notification = mockk<NotificationManager>(relaxed = true)

    private val gears = listOf(
        Gear(id = "gear-1", name = "Daily trainers", type = GearType.Shoe, distance = 12345),
        Gear(id = "gear-2", name = "Weekend bike", type = GearType.Bike, distance = 67890),
    )

    private lateinit var viewModel: GearsViewModel

    fun createViewModel() = GearsViewModel(getGears, syncGear, notification)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {}

    @After
    fun tearDown() {
        confirmVerified(getGears, syncGear, notification)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        every { getGears() } returns flowOf(gears)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.gears).isEqualTo(gears)
            assertThat(state.loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getGears() }
    }

    @Test
    fun `Initial state - empty list`() = runTest {
        every { getGears() } returns flowOf(emptyList())

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.gears).isEmpty()
            assertThat(state.loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getGears() }
    }

    @Test
    fun `Initial state - gears updated`() = runTest {
        val gearsFlow = MutableStateFlow(gears)
        val updatedGears = gears + Gear(id = "gear-3", name = "Trail shoes", type = GearType.Shoe, distance = 100)
        every { getGears() } returns gearsFlow

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem().gears).isEqualTo(gears)

            gearsFlow.value = updatedGears

            val state = awaitItem()
            assertThat(state.gears).isEqualTo(updatedGears)
            assertThat(state.loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getGears() }
    }

    @Test
    fun `Sync gears - success`() = runTest {
        val syncResult = CompletableDeferred<Result<Unit>>()
        every { getGears() } returns flowOf(gears)
        coEvery { syncGear() } coAnswers { syncResult.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem().loading).isFalse()
            viewModel.sync()

            assertThat(awaitItem().loading).isTrue()
            syncResult.complete(Result.success(Unit))
            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.gears).isEqualTo(gears)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getGears() }
        verify { notification.show("Gears synced") }
        coVerify { syncGear() }
    }

    @Test
    fun `Sync gears - failure`() = runTest {
        val syncResult = CompletableDeferred<Result<Unit>>()
        every { getGears() } returns flowOf(gears)
        coEvery { syncGear() } coAnswers { syncResult.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem().loading).isFalse()
            viewModel.sync()

            assertThat(awaitItem().loading).isTrue()
            syncResult.complete(Result.failure(Exception("error")))
            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.gears).isEqualTo(gears)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getGears() }
        verify { notification.show("error") }
        coVerify { syncGear() }
    }

    @Test
    fun `Sync gears - failure without message`() = runTest {
        val syncResult = CompletableDeferred<Result<Unit>>()
        every { getGears() } returns flowOf(gears)
        coEvery { syncGear() } coAnswers { syncResult.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem().loading).isFalse()
            viewModel.sync()

            assertThat(awaitItem().loading).isTrue()
            syncResult.complete(Result.failure(Exception()))
            assertThat(awaitItem().loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getGears() }
        verify { notification.show("Sync failed") }
        coVerify { syncGear() }
    }
}
