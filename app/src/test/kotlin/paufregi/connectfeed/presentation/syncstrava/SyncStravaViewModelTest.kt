package paufregi.connectfeed.presentation.syncstrava

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.usecases.GetActivities
import paufregi.connectfeed.core.usecases.GetStravaActivities
import paufregi.connectfeed.core.usecases.SyncStravaActivity
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import kotlin.time.Instant

@ExperimentalCoroutinesApi
class SyncStravaViewModelTest {

    private val getActivities = mockk<GetActivities>()
    private val getStravaActivities = mockk<GetStravaActivities>()
    private val syncStravaActivity = mockk<SyncStravaActivity>()

    private lateinit var viewModel: SyncStravaViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    val activities = listOf(
        Activity(
            id = 1L,
            name = "Running",
            type = ActivityType.Running,
            eventType = EventType.Training,
            distance = 10234.00,
            trainingEffect = "recovery",
            date = Instant.parse("2025-01-01T01:00:00Z")
        ),
        Activity(
            id = 2L,
            name = "Cycling",
            type = ActivityType.Cycling,
            eventType = EventType.Training,
            distance = 17803.00,
            trainingEffect = "base",
            date = Instant.parse("2025-01-01T02:00:00Z")
        ),
        Activity(
            id = 3L,
            name = "Running2",
            type = ActivityType.Running,
            eventType = EventType.Training,
            distance = 5234.00,
            trainingEffect = "base",
            date = Instant.parse("2025-01-01T03:00:00Z")
        ),
    )

    val stravaActivities = listOf(
        Activity(
            id = 1L,
            name = "StravaRunning",
            type = ActivityType.Running,
            distance = 10234.00,
            date = Instant.parse("2025-01-01T01:00:00Z")
        ),
        Activity(
            id = 2L,
            name = "StravaCycling",
            type = ActivityType.Cycling,
            distance = 17803.00,
            date = Instant.parse("2025-01-01T02:00:00Z")
        ),
        Activity(
            id = 3L,
            name = "StravaRunning2",
            type = ActivityType.Running,
            distance = 5234.00,
            date = Instant.parse("2025-01-01T03:00:00Z")
        ),
    )

    @Before
    fun setup(){

    }

    @After
    fun tearDown(){
        confirmVerified(getActivities, getStravaActivities, syncStravaActivity)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Fails to load activities`() = runTest {
        coEvery { getActivities(any()) } returns Result.failure("error")
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load Garmin activities"))
            assertThat(state.activities).isEmpty()
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Fails to load strava activities`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.failure("error")

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load Strava activities"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEmpty()
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Fails to load all activities`() = runTest {
        coEvery { getActivities(any()) } returns Result.failure("error")
        coEvery { getStravaActivities(any()) } returns Result.failure("error")

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isInstanceOf(ProcessState.Failure::class.java)
            state.process as ProcessState.Failure
            assertThat(state.process.reason).contains("Garmin")
            assertThat(state.process.reason).contains("Strava")
            assertThat(state.activities).isEmpty()
            assertThat(state.stravaActivities).isEmpty()
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set activity`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            viewModel.onAction(SyncStravaAction.SetActivity(activities[0]))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set Strava activity`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            viewModel.onAction(SyncStravaAction.SetStravaActivity(stravaActivities[0]))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set activity & Strava activity - matching`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            viewModel.onAction(SyncStravaAction.SetActivity(activities[2]))
            viewModel.onAction(SyncStravaAction.SetStravaActivity(stravaActivities[0]))
            skipItems(2)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isEqualTo(activities[2])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set activity & Strava activity - no matching`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            viewModel.onAction(SyncStravaAction.SetActivity(activities[1]))
            viewModel.onAction(SyncStravaAction.SetStravaActivity(stravaActivities[0]))
            skipItems(2)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set Strava activity & activity - matching`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            viewModel.onAction(SyncStravaAction.SetStravaActivity(stravaActivities[2]))
            viewModel.onAction(SyncStravaAction.SetActivity(activities[0]))
            skipItems(2)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[2])
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set Strava activity & activity - no matching`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            viewModel.onAction(SyncStravaAction.SetStravaActivity(stravaActivities[1]))
            viewModel.onAction(SyncStravaAction.SetActivity(activities[0]))
            skipItems(2)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set description`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            viewModel.onAction(SyncStravaAction.SetDescription("description"))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.description).isEqualTo("description")
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set training effect`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            viewModel.onAction(SyncStravaAction.SetTrainingEffect(true))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Save activity`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { syncStravaActivity(any(), any(), any(), any()) } returns Result.success(Unit)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            viewModel.onAction(SyncStravaAction.SetActivity(activities[0]))
            viewModel.onAction(SyncStravaAction.SetDescription("description"))
            viewModel.onAction(SyncStravaAction.SetTrainingEffect(true))
            viewModel.onAction(SyncStravaAction.Save)
            skipItems(4)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success("Strava activity updated"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.description).isEqualTo("description")
            assertThat(state.trainingEffect).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            syncStravaActivity(activities[0], stravaActivities[0], "description", true)
        }
        confirmVerified(
            getActivities,
            getStravaActivities,
            syncStravaActivity
        )
    }

    @Test
    fun `Save activity - failure`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { syncStravaActivity(any(), any(), any(), any()) } returns Result.failure("failure")

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            viewModel.onAction(SyncStravaAction.SetActivity(activities[0]))
            viewModel.onAction(SyncStravaAction.SetDescription("description"))
            viewModel.onAction(SyncStravaAction.SetTrainingEffect(true))
            viewModel.onAction(SyncStravaAction.Save)
            skipItems(4)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't update Strava activity"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.description).isEqualTo("description")
            assertThat(state.trainingEffect).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            syncStravaActivity(activities[0], stravaActivities[0], "description", true)
        }
        confirmVerified(
            getActivities,
            getStravaActivities,
            syncStravaActivity
        )
    }

    @Test
    fun `Restart action`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            viewModel.onAction(SyncStravaAction.Restart)
            skipItems(2)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getActivities(true)
            getStravaActivities(false)
            getStravaActivities(true)
        }
    }
}