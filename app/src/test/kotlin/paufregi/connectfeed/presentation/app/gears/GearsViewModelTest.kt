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
        confirmVerified(syncGear, notification)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        every { getGears() } returns flowOf(gears)

        viewModel = GearsViewModel(getGears, syncGear, notification)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.gears).isEqualTo(gears)
            assertThat(state.loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Initial state - empty list`() = runTest {
        every { getGears() } returns flowOf(emptyList())

        viewModel = GearsViewModel(getGears, syncGear, notification)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.gears).isEqualTo(emptyList<Gear>())
            assertThat(state.loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Sync gears - processing`() = runTest {
        val syncResult = CompletableDeferred<Result<Unit>>()
        every { getGears() } returns flowOf(gears)
        coEvery { syncGear() } coAnswers { syncResult.await() }

        viewModel = GearsViewModel(getGears, syncGear, notification)

        viewModel.state.test {
            skipItems(1)
            viewModel.sync()

            assertThat(awaitItem().loading).isTrue()
            syncResult.complete(Result.success(Unit))
            assertThat(awaitItem().loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { syncGear() }
        verify { notification.show("Gears synced") }
    }

    @Test
    fun `Sync gears - success`() = runTest {
        val syncResult = CompletableDeferred<Result<Unit>>()
        every { getGears() } returns flowOf(gears)
        coEvery { syncGear() } coAnswers { syncResult.await() }

        viewModel = GearsViewModel(getGears, syncGear, notification)

        viewModel.state.test {
            skipItems(1)
            viewModel.sync()

            assertThat(awaitItem().loading).isTrue()
            syncResult.complete(Result.success(Unit))
            assertThat(awaitItem().loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { syncGear() }
        verify { notification.show("Gears synced") }
    }

    @Test
    fun `Sync gears - failure`() = runTest {
        val syncResult = CompletableDeferred<Result<Unit>>()
        every { getGears() } returns flowOf(gears)
        coEvery { syncGear() } coAnswers { syncResult.await() }

        viewModel = GearsViewModel(getGears, syncGear, notification)

        viewModel.state.test {
            skipItems(1)
            viewModel.sync()

            assertThat(awaitItem().loading).isTrue()
            syncResult.complete(Result.failure(Exception("error")))
            assertThat(awaitItem().loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
        coVerify { syncGear() }
        verify { notification.show("error") }
    }
}

