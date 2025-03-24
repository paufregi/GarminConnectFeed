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
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.usecases.GetLatestActivities
import paufregi.connectfeed.core.usecases.GetLatestStravaActivities
import paufregi.connectfeed.core.usecases.SyncStravaActivity
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class SyncStravaViewModelTest {

    private val getActivities = mockk<GetLatestActivities>()
    private val getStravaActivities = mockk<GetLatestStravaActivities>()
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
            trainingEffect = "recovery"
        ),
        Activity(
            id = 2L,
            name = "Cycling",
            type = ActivityType.Cycling,
            eventType = EventType.Training,
            distance = 17803.00,
            trainingEffect = "base"
        )
    )

    val stravaActivities = listOf(
        Activity(
            id = 1L,
            name = "StravaRunning",
            type = ActivityType.Running,
            distance = 10234.00
        ),
        Activity(
            id = 2L,
            name = "StravaCycling",
            type = ActivityType.Cycling,
            distance = 17803.00
        )
    )

    @Before
    fun setup(){

    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)

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
            getActivities()
            getStravaActivities()
        }
        confirmVerified(getActivities, getStravaActivities, syncStravaActivity)
    }

    @Test
    fun `Fails to load activities`() = runTest {
        coEvery { getActivities() } returns Result.Failure("error")
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load activities"))
            assertThat(state.activities).isEmpty()
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        confirmVerified(getActivities, getStravaActivities, syncStravaActivity)
    }

    @Test
    fun `Fails to load strava activities`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Failure("error")

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
            getActivities()
            getStravaActivities()
        }
        confirmVerified(getActivities, getStravaActivities, syncStravaActivity)
    }

    @Test
    fun `Fails to load all activities`() = runTest {
        coEvery { getActivities() } returns Result.Failure("error")
        coEvery { getStravaActivities() } returns Result.Failure("error")

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load activities & Strava activities"))
            assertThat(state.activities).isEmpty()
            assertThat(state.stravaActivities).isEmpty()
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        confirmVerified(getActivities, getStravaActivities, syncStravaActivity)
    }

    @Test
    fun `Set activity`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(SyncStravaAction.SetActivity(activities[0]))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isNull()
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
        }
        confirmVerified(getActivities, getStravaActivities, syncStravaActivity)
    }

    @Test
    fun `Set activity - matching`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(SyncStravaAction.SetStravaActivity(stravaActivities[0]))
            awaitItem() // skip
            viewModel.onAction(SyncStravaAction.SetActivity(activities[0]))
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
            getActivities()
            getStravaActivities()
        }
        confirmVerified(getActivities, getStravaActivities, syncStravaActivity)
    }

    @Test
    fun `Set activity - no matching`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(SyncStravaAction.SetStravaActivity(stravaActivities[1]))
            awaitItem() // skip
            viewModel.onAction(SyncStravaAction.SetActivity(activities[0]))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isNull()
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        confirmVerified(getActivities, getStravaActivities, syncStravaActivity)
    }

    @Test
    fun `Set Strava activity`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(SyncStravaAction.SetStravaActivity(stravaActivities[0]))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
        }
        confirmVerified(getActivities, getStravaActivities, syncStravaActivity)
    }

    @Test
    fun `Set Strava activity - matching`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(SyncStravaAction.SetActivity(activities[0]))
            awaitItem() // skip
            viewModel.onAction(SyncStravaAction.SetStravaActivity(stravaActivities[0]))
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
            getActivities()
            getStravaActivities()
        }
        confirmVerified(getActivities, getStravaActivities, syncStravaActivity)
    }

    @Test
    fun `Set Strava activity - no matching`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(SyncStravaAction.SetActivity(activities[1]))
            awaitItem() // skip
            viewModel.onAction(SyncStravaAction.SetStravaActivity(stravaActivities[0]))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.description).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        confirmVerified(getActivities, getStravaActivities, syncStravaActivity)
    }

    @Test
    fun `Set description`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(SyncStravaAction.SetDescription("description"))
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
            getActivities()
            getStravaActivities()
        }
        confirmVerified(getActivities, getStravaActivities, syncStravaActivity)
    }

    @Test
    fun `Set training effect`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(SyncStravaAction.SetTrainingEffect(true))
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
            getActivities()
            getStravaActivities()
        }
        confirmVerified(getActivities, getStravaActivities, syncStravaActivity)
    }

    @Test
    fun `Save activity`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        coEvery { syncStravaActivity(any(), any(), any(), any()) } returns Result.Success(Unit)

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(SyncStravaAction.SetActivity(activities[0]))
            awaitItem() // skip
            viewModel.onAction(SyncStravaAction.SetStravaActivity(stravaActivities[0]))
            awaitItem() // skip
            viewModel.onAction(SyncStravaAction.SetDescription("description"))
            awaitItem() // skip
            viewModel.onAction(SyncStravaAction.SetTrainingEffect(true))
            awaitItem() // skip
            viewModel.onAction(SyncStravaAction.Save)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success("Activity updated"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.description).isEqualTo("description")
            assertThat(state.trainingEffect).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
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
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        coEvery { syncStravaActivity(any(), any(), any(), any()) } returns Result.Failure("failure")

        viewModel = SyncStravaViewModel(getActivities, getStravaActivities, syncStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(SyncStravaAction.SetActivity(activities[0]))
            awaitItem() // skip
            viewModel.onAction(SyncStravaAction.SetStravaActivity(stravaActivities[0]))
            awaitItem() // skip
            viewModel.onAction(SyncStravaAction.SetDescription("description"))
            awaitItem() // skip
            viewModel.onAction(SyncStravaAction.SetTrainingEffect(true))
            awaitItem() // skip
            viewModel.onAction(SyncStravaAction.Save)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't update activity"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.description).isEqualTo("description")
            assertThat(state.trainingEffect).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
            syncStravaActivity(activities[0], stravaActivities[0], "description", true)
        }
        confirmVerified(
            getActivities,
            getStravaActivities,
            syncStravaActivity
        )
    }
}