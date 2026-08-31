package paufregi.connectfeed.presentation.quickedit

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
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.core.usecases.GetActivities
import paufregi.connectfeed.core.usecases.GetGears
import paufregi.connectfeed.core.usecases.GetProfiles
import paufregi.connectfeed.core.usecases.GetStravaActivities
import paufregi.connectfeed.core.usecases.GetWorkout
import paufregi.connectfeed.core.usecases.QuickUpdateActivity
import paufregi.connectfeed.core.usecases.QuickUpdateStravaActivity
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import kotlin.time.Instant

@ExperimentalCoroutinesApi
class QuickEditViewModelTest {

    private val getActivities = mockk<GetActivities>()
    private val getStravaActivities = mockk<GetStravaActivities>()
    private val getProfiles = mockk<GetProfiles>()
    private val getGears = mockk<GetGears>()
    private val quickUpdateActivity = mockk<QuickUpdateActivity>()
    private val quickUpdateStravaActivity = mockk<QuickUpdateStravaActivity>()
    private val getWorkout = mockk<GetWorkout>()

    private lateinit var viewModel: QuickEditViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel() = QuickEditViewModel(
        getActivities,
        getStravaActivities,
        getProfiles,
        getGears,
        quickUpdateActivity,
        quickUpdateStravaActivity,
        getWorkout,
    )

    val workout = Workout(1, "workout")

    val activities = listOf(
        Activity(
            id = 1L,
            name = "Running",
            type = ActivityType.Running,
            eventType = EventType.Training,
            distance = 10234.00,
            trainingEffect = "recovery",
            date = Instant.fromEpochMilliseconds(1735693200000),
            workoutId = workout.id
        ),
        Activity(
            id = 2L,
            name = "Cycling",
            type = ActivityType.Cycling,
            eventType = EventType.Training,
            distance = 17803.00,
            trainingEffect = "base",
            date = Instant.fromEpochMilliseconds(1729705968000)
        ),
        Activity(
            id = 3L,
            name = "Running2",
            type = ActivityType.Running,
            eventType = EventType.Training,
            distance = 5234.00,
            trainingEffect = "base",
            date = Instant.fromEpochMilliseconds(1729705968000)
        ),
    )

    val stravaActivities = listOf(
        Activity(
            id = 1L,
            name = "StravaRunning",
            type = ActivityType.Running,
            distance = 10234.00,
            date = Instant.fromEpochMilliseconds(1735693200000)
        ),
        Activity(
            id = 2L,
            name = "StravaCycling",
            type = ActivityType.Cycling,
            distance = 17803.00,
            date = Instant.fromEpochMilliseconds(1729705968000)
        ),
        Activity(
            id = 3L,
            name = "StravaRunning2",
            type = ActivityType.Running,
            distance = 5234.00,
            date = Instant.fromEpochMilliseconds(1729705968000)
        ),
    )

    val profiles = listOf(
        Profile(name = "profile1", type = ActivityType.Running),
        Profile(name = "profile2" ,type = ActivityType.Cycling),
        Profile(name = "profile3", type = ActivityType.Running)
    )

    val gears = listOf(
        Gear(id = "bike-1", name = "Bike 1", type = GearType.Bike),
        Gear(id = "shoe-1", name = "Shoe 1", type = GearType.Shoe),
        Gear(id = "unknown-1", name = "Unknown 1", type = GearType.Unknown),
    )

    @Before
    fun setup(){
        every { getProfiles() } returns flowOf(profiles)
        every { getGears() } returns flowOf(gears)
    }

    @After
    fun tearDown(){
        verify {
            getProfiles()
            getGears()
        }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity, getWorkout, getGears)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(emptyList())

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEmpty()
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.gears).isEqualTo(gears)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.gear).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Initial state - with Strava`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.gears).isEqualTo(gears)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.gear).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
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

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load activities"))
            assertThat(state.activities).isEmpty()
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.gears).isEqualTo(gears)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.gear).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
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

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load Strava activities"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEmpty()
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.gears).isEqualTo(gears)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.gear).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isFalse()
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

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load activities & Strava activities"))
            assertThat(state.activities).isEmpty()
            assertThat(state.stravaActivities).isEmpty()
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.gears).isEqualTo(gears)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.gear).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set profile`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            val state = awaitItem()
            val expectedState = initialState.copy(
                profile = profiles[0],
                water = profiles[0].water
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set activity`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            val state = awaitItem()
            val expectedState = initialState.copy(
                activity = activities[0],
                stravaActivity = stravaActivities[0]
            )
            assertThat(state).isEqualTo(expectedState)
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

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[0]))
            val state = awaitItem()
            val expectedState = initialState.copy(
                activity = activities[0],
                stravaActivity = stravaActivities[0]
            )
            assertThat(state).isEqualTo(expectedState)
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

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            viewModel.onAction(QuickEditAction.SetActivity(activities[2]))
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[0]))
            skipItems(2)
            val state = awaitItem()
            val expectedState = initialState.copy(
                activity = activities[2],
                stravaActivity = stravaActivities[0],
                profile = profiles[0],
            )
            assertThat(state).isEqualTo(expectedState)
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

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetProfile(profiles[1]))
            viewModel.onAction(QuickEditAction.SetActivity(activities[1]))
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[0]))
            skipItems(2)
            val state = awaitItem()
            val expectedState = initialState.copy(
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                profile = null,
            )
            assertThat(state).isEqualTo(expectedState)
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

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[2]))
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            skipItems(2)
            val state = awaitItem()
            val expectedState = initialState.copy(
                activity = activities[0],
                stravaActivity = stravaActivities[2],
                profile = profiles[0],
                water = profiles[0].water
            )
            assertThat(state).isEqualTo(expectedState)
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

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetProfile(profiles[1]))
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[1]))
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            skipItems(2)
            val state = awaitItem()
            val expectedState = initialState.copy(
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                profile = null,
            )
            assertThat(state).isEqualTo(expectedState)
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

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            val state = awaitItem()
            val expectedState = initialState.copy(description = "description")
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set gear`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetGear(gears[0]))
            val state = awaitItem()
            val expectedState = initialState.copy(gear = gears[0])
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set activity clears incompatible gear`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetGear(gears[1]))
            viewModel.onAction(QuickEditAction.SetActivity(activities[1]))
            skipItems(1)
            val state = awaitItem()
            val expectedState = initialState.copy(
                activity = activities[1],
                stravaActivity = stravaActivities[1],
                gear = null,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set Strava activity clears incompatible gear`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetGear(gears[1]))
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[1]))
            skipItems(1)
            val state = awaitItem()
            val expectedState = initialState.copy(
                activity = activities[1],
                stravaActivity = stravaActivities[1],
                gear = null,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set profile clears incompatible gear`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            viewModel.onAction(QuickEditAction.SetGear(gears[1]))
            viewModel.onAction(QuickEditAction.SetProfile(profiles[1]))
            skipItems(2)
            val state = awaitItem()
            val expectedState = initialState.copy(
                profile = profiles[1],
                activity = null,
                stravaActivity = null,
                gear = null,
                water = profiles[1].water,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set water`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            viewModel.onAction(QuickEditAction.SetWater(100))
            skipItems(1)
            val state = awaitItem()
            val expectedState = initialState.copy(
                profile = profiles[0],
                water = 100,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set effort`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetEffort(50f))
            val state = awaitItem()
            val expectedState = initialState.copy(effort = 50f)
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
    }

    @Test
    fun `Set feel`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            val state = awaitItem()
            val expectedState = initialState.copy(feel = 50f)
            assertThat(state).isEqualTo(expectedState)
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
        coEvery { quickUpdateActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { quickUpdateStravaActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { getWorkout(any()) } returns Result.success(workout)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            viewModel.onAction(QuickEditAction.SetGear(gears[1]))
            viewModel.onAction(QuickEditAction.SetWater(25))
            viewModel.onAction(QuickEditAction.SetEffort(80f))
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            viewModel.onAction(QuickEditAction.Save)
            skipItems(8)
            val state = awaitItem()
            val expectedState = initialState.copy(
                process = ProcessState.Success("Activity updated"),
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                profile = profiles[0],
                gear = gears[1],
                description = "description",
                water = 25,
                effort = 80f,
                feel = 50f
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            quickUpdateActivity(activities[0], profiles[0], 25, 50f, 80f, workout, gears[1])
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description", workout)
            getWorkout(workout.id)
        }
    }

    @Test
    fun `Save activity - failed to load workout`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { quickUpdateActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { quickUpdateStravaActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { getWorkout(any()) } returns Result.failure(Exception("No workout"))

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            viewModel.onAction(QuickEditAction.SetWater(25))
            viewModel.onAction(QuickEditAction.SetEffort(80f))
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            viewModel.onAction(QuickEditAction.Save)
            skipItems(7)
            val state = awaitItem()
            val expectedState = initialState.copy(
                process = ProcessState.Success("Activity updated"),
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                profile = profiles[0],
                gear = null,
                description = "description",
                water = 25,
                effort = 80f,
                feel = 50f,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            quickUpdateActivity(activities[0], profiles[0], 25, 50f, 80f, null, null)
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description", null)
            getWorkout(workout.id)
        }
    }

    @Test
    fun `Save activity - failure`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { quickUpdateActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("failure")
        coEvery { quickUpdateStravaActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { getWorkout(any()) } returns Result.success(workout)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            viewModel.onAction(QuickEditAction.SetWater(25))
            viewModel.onAction(QuickEditAction.SetEffort(80f))
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            viewModel.onAction(QuickEditAction.Save)
            skipItems(7)
            val state = awaitItem()
            val expectedState = initialState.copy(
                process = ProcessState.Failure("Couldn't update Garmin activity"),
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                profile = profiles[0],
                description = "description",
                water = 25,
                effort = 80f,
                feel = 50f,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            quickUpdateActivity(activities[0], profiles[0], 25, 50f, 80f, workout, null)
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description", workout)
            getWorkout(workout.id)
        }
    }

    @Test
    fun `Save activity - strava failure`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { quickUpdateActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { quickUpdateStravaActivity(any(), any(), any(), any(), any()) } returns Result.failure("failure")
        coEvery { getWorkout(any()) } returns Result.success(workout)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            viewModel.onAction(QuickEditAction.SetWater(25))
            viewModel.onAction(QuickEditAction.SetEffort(80f))
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            viewModel.onAction(QuickEditAction.Save)
            skipItems(7)
            val state = awaitItem()
            val expectedState = initialState.copy(
                process = ProcessState.Failure("Couldn't update Strava activity"),
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                profile = profiles[0],
                description = "description",
                water = 25,
                effort = 80f,
                feel = 50f,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            quickUpdateActivity(activities[0], profiles[0], 25, 50f, 80f, workout, null)
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description", workout)
            getWorkout(workout.id)
        }
    }

    @Test
    fun `Save activity - both failure`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { quickUpdateActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("failure")
        coEvery { quickUpdateStravaActivity(any(), any(), any(), any(), any()) } returns Result.failure("failure")
        coEvery { getWorkout(workout.id) } returns Result.success(workout)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            viewModel.onAction(QuickEditAction.SetWater(25))
            viewModel.onAction(QuickEditAction.SetEffort(80f))
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            viewModel.onAction(QuickEditAction.Save)
            skipItems(7)
            val state = awaitItem()
            val expectedState = initialState.copy(
                process = ProcessState.Failure("Couldn't update Garmin & Strava activity"),
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                profile = profiles[0],
                description = "description",
                water = 25,
                effort = 80f,
                feel = 50f,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            quickUpdateActivity(activities[0], profiles[0], 25, 50f, 80f, workout, null)
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description", workout)
            getWorkout(workout.id)
        }
    }

    @Test
    fun `Restart action`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            viewModel.onAction(QuickEditAction.SetGear(gears[1]))
            viewModel.onAction(QuickEditAction.Restart)
            skipItems(3)
            val state = awaitItem()
            val expectedState = initialState.copy(
                process = ProcessState.Idle,
                activity = null,
                stravaActivity = null,
                profile = null,
                gear = null,
                description = null,
                water = null,
                effort = null,
                feel = null,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getActivities(true)
            getStravaActivities(false)
            getStravaActivities(true)
        }
    }
}