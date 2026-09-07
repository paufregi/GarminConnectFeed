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
        Gear(id = "shoe-1", name = "Shoe 1", type = GearType.Shoe),
        Gear(id = "bike-1", name = "Bike 1", type = GearType.Bike),
        Gear(id = "unknown-1", name = "Unknown 1", type = GearType.Unknown),
    )

    val initialState = QuickEditState(
        process = ProcessState.Idle,
        profiles = profiles,
        gears = gears
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
    fun `Set activity`() = runTest {
        val initState = initialState.copy(
            activity = activities[1],
            profile = profiles[0],
            gear = gears[0],
            water = 100,
            effort = 50f,
            feel = 75f
        )

        val expectedState = initialState.copy(
            activity = activities[0]
        )


        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set activity with Strava`() = runTest {
        val initState = initialState.copy(
            stravaActivities = stravaActivities,
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            val expectedState = initState.copy(
                activity = activities[0],
                stravaActivity = stravaActivities[0]
            )
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set activity with Strava - no match`() = runTest {
        val initState = initialState.copy(
            stravaActivities = stravaActivities.drop(1)
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            val expectedState = initState.copy(
                activity = activities[0],
            )
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set profile`() = runTest {
        val initState = initialState.copy(
            activity = activities[0],
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            val expectedState = initState.copy(
                profile = profiles[0]
            )
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set profile - no activity`() = runTest {
        viewModel = createViewModel()
        viewModel.seedStateForTest(initialState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set profile - no match`() = runTest {
        val initState = initialState.copy(
            activity = activities[0]
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetProfile(profiles[1]))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set gear`() = runTest {
        val initState = initialState.copy(
            activity = activities[0],
            profile = profiles[0]
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetGear(gears[0]))
            val expectedState = initState.copy(
                gear = gears[0]
            )
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set gear - no activity`() = runTest {
        val initState = initialState.copy(
            profile = profiles[0]
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetGear(gears[0]))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set gear - no profile`() = runTest {
        val initState = initialState.copy(
            activity = activities[0]
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetGear(gears[0]))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set gear - no match`() = runTest {
        val initState = initialState.copy(
            activity = activities[0],
            profile = profiles[0]
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetGear(gears[1]))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set description`() = runTest {
        val initState = initialState.copy(
            activity = activities[0],
            profile = profiles[0]
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetDescription("Tempo day"))
            val expectedState = initState.copy(description = "Tempo day")
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set description - no activity`() = runTest {
        val initState = initialState.copy(
            profile = profiles[0]
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetDescription("Tempo day"))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set description - no profile`() = runTest {
        val initState = initialState.copy(
            activity = activities[0]
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetDescription("Tempo day"))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set water`() = runTest {
        val initState = initialState.copy(
            activity = activities[0],
            profile = profiles[0].copy(customWater = true)
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetWater(750))
            val expectedState = initState.copy(water = 750)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set water - no activity`() = runTest {
        val initState = initialState.copy(
            profile = profiles[0].copy(customWater = true)
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetWater(750))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set water - no profile`() = runTest {
        val initState = initialState.copy(
            activity = activities[0]
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetWater(750))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set water - no custom water`() = runTest {
        val initState = initialState.copy(
            activity = activities[0],
            profile = profiles[0].copy(customWater = false)
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetWater(750))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set effort`() = runTest {
        val initState = initialState.copy(
            activity = activities[0],
            profile = profiles[0].copy(feelAndEffort = true)
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetEffort(50f))
            val expectedState = initState.copy(effort = 50f)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set effort - no activity`() = runTest {
        val initState = initialState.copy(
            profile = profiles[0].copy(feelAndEffort = true)
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetEffort(50f))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set effort - no profile`() = runTest {
        val initState = initialState.copy(
            activity = activities[0]
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetEffort(50f))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set effort - no feel and effort`() = runTest {
        val initState = initialState.copy(
            activity = activities[0],
            profile = profiles[0].copy(feelAndEffort = false)
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetEffort(50f))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set feel`() = runTest {
        val initState = initialState.copy(
            activity = activities[0],
            profile = profiles[0].copy(feelAndEffort = true)
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetFeel(80f))
            val expectedState = initState.copy(feel = 80f)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set feel - no activity`() = runTest {
        val initState = initialState.copy(
            profile = profiles[0].copy(feelAndEffort = true)
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetFeel(80f))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set feel - no profile`() = runTest {
        val initState = initialState.copy(
            activity = activities[0]
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetFeel(80f))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set feel - no feel and effort`() = runTest {
        val initState = initialState.copy(
            activity = activities[0],
            profile = profiles[0].copy(feelAndEffort = false)
        )
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetFeel(80f))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Save action`() = runTest {
        coEvery { getWorkout(any()) } returns Result.success(workout)
        coEvery { quickUpdateActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { quickUpdateStravaActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val initState = initialState.copy(
            activity = activities[0],
            stravaActivity = stravaActivities[0],
            profile = profiles[0],
            gear = gears[0],
            description = "Tempo day",
            water = 750,
            effort = 50f,
            feel = 80f
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.Save)
            skipItems(2)
            val expectedState = initState.copy(process = ProcessState.Success("Activity updated"))
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getWorkout(workout.id)
            quickUpdateActivity(activities[0], profiles[0], 750, 80f, 50f, workout, gears[0])
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "Tempo day", workout)
        }
    }

    @Test
    fun `Save action - no Strava activity`() = runTest {
        coEvery { getWorkout(any()) } returns Result.success(workout)
        coEvery { quickUpdateActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val initState = initialState.copy(
            activity = activities[0],
            profile = profiles[0],
            gear = gears[0],
            description = "Tempo day",
            water = 750,
            effort = 50f,
            feel = 80f
        )
        val expectedState = initState.copy(process = ProcessState.Success("Activity updated"))

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.Save)
            skipItems(2)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getWorkout(workout.id)
            quickUpdateActivity(activities[0], profiles[0], 750, 80f, 50f, workout, gears[0])
        }
    }

    @Test
    fun `Save action - Garmin failure`() = runTest {
        coEvery { getWorkout(any()) } returns Result.success(workout)
        coEvery { quickUpdateActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("error")
        coEvery { quickUpdateStravaActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val initState = initialState.copy(
            activity = activities[0],
            stravaActivity = stravaActivities[0],
            profile = profiles[0],
            description = "Tempo day"
        )
        val expectedState = initState.copy(process = ProcessState.Failure("Couldn't update Garmin activity"))

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.Save)
            skipItems(2)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getWorkout(workout.id)
            quickUpdateActivity(activities[0], profiles[0], null, null, null, workout, null)
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "Tempo day", workout)
        }
    }

    @Test
    fun `Save action - Strava failure`() = runTest {
        coEvery { getWorkout(any()) } returns Result.success(workout)
        coEvery { quickUpdateActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { quickUpdateStravaActivity(any(), any(), any(), any(), any()) } returns Result.failure("error")

        val initState = initialState.copy(
            activity = activities[0],
            stravaActivity = stravaActivities[0],
            profile = profiles[0],
            description = "Tempo day"
        )
        val expectedState = initState.copy(process = ProcessState.Failure("Couldn't update Strava activity"))

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.Save)
            skipItems(2)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getWorkout(workout.id)
            quickUpdateActivity(activities[0], profiles[0], null, null, null, workout, null)
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "Tempo day", workout)
        }
    }

    @Test
    fun `Save action - Garmin and Strava failure`() = runTest {
        coEvery { getWorkout(any()) } returns Result.success(workout)
        coEvery { quickUpdateActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("error")
        coEvery { quickUpdateStravaActivity(any(), any(), any(), any(), any()) } returns Result.failure("error")

        val initState = initialState.copy(
            activity = activities[0],
            stravaActivity = stravaActivities[0],
            profile = profiles[0],
            description = "Tempo day"
        )
        val expectedState = initState.copy(process = ProcessState.Failure("Couldn't update Garmin & Strava activity"))

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.Save)
            skipItems(2)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getWorkout(workout.id)
            quickUpdateActivity(activities[0], profiles[0], null, null, null, workout, null)
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "Tempo day", workout)
        }
    }

    @Test
    fun `Restart action`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        val initState = initialState.copy(
            activity = activities[0],
            profile = profiles[0],
            gear = gears[0],
            description = "Before restart",
            water = 750,
            effort = 50f,
            feel = 80f
        )

        val expectedState = initialState.copy(
            activities = activities,
            stravaActivities = stravaActivities
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.Restart)
            skipItems(2)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(true)
            getStravaActivities(true)
        }
    }

    @Test
    fun `Restart - fails to reload activities`() = runTest {
        coEvery { getActivities(any()) } returns Result.failure("error")
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)

        val initState = initialState.copy(
            activity = activities[0],
            profile = profiles[0],
            water = 750
        )

        val expectedState = initialState.copy(
            process = ProcessState.Failure("Couldn't load activities"),
            stravaActivities = stravaActivities

        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.Restart)
            skipItems(2)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(true)
            getStravaActivities(true)
        }
    }

    @Test
    fun `Restart - fails to reload strava activities`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.failure("error")

        val initState = initialState.copy(
            activity = activities[0],
            profile = profiles[0],
            water = 750
        )

        val expectedState = initialState.copy(
            process = ProcessState.Failure("Couldn't load Strava activities"),
            activities = activities
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.Restart)
            skipItems(2)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(true)
            getStravaActivities(true)
        }
    }

    @Test
    fun `Restart - to load all activities`() = runTest {
        coEvery { getActivities(any()) } returns Result.failure("error")
        coEvery { getStravaActivities(any()) } returns Result.failure("error")

        val initState = initialState.copy(
            activity = activities[0],
            profile = profiles[0],
            water = 750
        )

        val expectedState = initialState.copy(
            process = ProcessState.Failure("Couldn't load activities & Strava activities"),
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.Restart)
            skipItems(2)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(true)
            getStravaActivities(true)
        }
    }

}