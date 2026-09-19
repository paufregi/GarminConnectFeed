package paufregi.connectfeed.presentation.modify

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
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.core.usecases.GetActivities
import paufregi.connectfeed.core.usecases.GetCourses
import paufregi.connectfeed.core.usecases.GetEventTypes
import paufregi.connectfeed.core.usecases.GetGears
import paufregi.connectfeed.core.usecases.GetProfiles
import paufregi.connectfeed.core.usecases.GetStravaActivities
import paufregi.connectfeed.core.usecases.GetWorkout
import paufregi.connectfeed.core.usecases.QuickUpdateActivity
import paufregi.connectfeed.core.usecases.QuickUpdateStravaActivity
import paufregi.connectfeed.core.usecases.UpdateActivity
import paufregi.connectfeed.core.usecases.UpdateStravaActivity
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import kotlin.time.Instant

@ExperimentalCoroutinesApi
class ModifyViewModelTest {

    private val getActivities = mockk<GetActivities>()
    private val getStravaActivities = mockk<GetStravaActivities>()
    private val getProfiles = mockk<GetProfiles>()
    private val getGears = mockk<GetGears>()
    private val getEventTypes = mockk<GetEventTypes>()
    private val getCourses = mockk<GetCourses>()
    private val getWorkout = mockk<GetWorkout>()
    private val updateActivity = mockk<UpdateActivity>()
    private val updateStravaActivity = mockk<UpdateStravaActivity>()
    private val quickUpdateActivity = mockk<QuickUpdateActivity>()
    private val quickUpdateStravaActivity = mockk<QuickUpdateStravaActivity>()

    private lateinit var viewModel: ModifyViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel() = ModifyViewModel(
        getActivities,
        getStravaActivities,
        getProfiles,
        getGears,
        getEventTypes,
        getCourses,
        getWorkout,
        updateActivity,
        updateStravaActivity,
        quickUpdateActivity,
        quickUpdateStravaActivity,
    )

    private val workout = Workout(1, "Workout")

    private val activities = listOf(
        Activity(
            id = 1L,
            name = "Running",
            type = ActivityType.Running,
            eventType = EventType.Training,
            distance = 10234.00,
            trainingEffect = "recovery",
            date = Instant.fromEpochMilliseconds(1735693200000),
            workoutId = workout.id,
        ),
    )

    private val stravaActivities = listOf(
        Activity(
            id = 10L,
            name = "StravaRunning",
            type = ActivityType.Running,
            distance = 10234.00,
            date = Instant.fromEpochMilliseconds(1735693200000),
        ),
    )

    private val profiles = listOf(
        Profile(name = "profile", type = ActivityType.Running, customWater = true, feelAndEffort = true),
    )

    private val courses = listOf(
        Course(id = 1L, name = "course", distance = 12000.0, type = ActivityType.Running),
    )

    @Before
    fun setup() {
        every { getProfiles() } returns flowOf(profiles)
        every { getGears() } returns flowOf(emptyList())
        every { getEventTypes() } returns listOf(EventType.Training)
    }

    @After
    fun tearDown() {
        verify {
            getProfiles()
            getGears()
            getEventTypes()
        }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getProfiles,
            getGears,
            getEventTypes,
            getCourses,
            getWorkout,
            updateActivity,
            updateStravaActivity,
            quickUpdateActivity,
            quickUpdateStravaActivity,
        )
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.mode).isEqualTo(ModifyMode.Manual)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set activity defaults to manual and matches strava`() = runTest {
        viewModel = createViewModel()
        viewModel.seedStateForTest(
            ModifyState(
                process = ProcessState.Idle,
                stravaActivities = stravaActivities,
                mode = ModifyMode.Profile,
                profile = profiles[0],
            ),
        )

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ModifyAction.SetActivity(activities[0]))
            val state = awaitItem()
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.mode).isEqualTo(ModifyMode.Manual)
            assertThat(state.profile).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Switching modes clears edit state`() = runTest {
        viewModel = createViewModel()
        viewModel.seedStateForTest(
            ModifyState(
                process = ProcessState.Idle,
                activity = activities[0],
                mode = ModifyMode.Manual,
                name = "Tempo",
                water = 10,
                feel = 50f,
            ),
        )

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ModifyAction.SetMode(ModifyMode.Profile))
            val state = awaitItem()
            assertThat(state.mode).isEqualTo(ModifyMode.Profile)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.name).isNull()
            assertThat(state.water).isNull()
            assertThat(state.feel).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Save manual updates Garmin and Strava`() = runTest {
        coEvery { getWorkout(workout.id) } returns Result.success(workout)
        coEvery {
            updateActivity(
                activity = any(),
                name = any(),
                eventType = any(),
                course = any(),
                water = any(),
                feel = any(),
                effort = any(),
                workout = any(),
                gear = any(),
            )
        } returns Result.success(Unit)
        coEvery {
            updateStravaActivity(
                stravaActivity = any(),
                name = any(),
                description = any(),
                eventType = any(),
                trainingEffect = any(),
                trainingEffectFlag = any(),
                workout = any(),
            )
        } returns Result.success(Unit)

        viewModel = createViewModel()
        viewModel.seedStateForTest(
            ModifyState(
                process = ProcessState.Idle,
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                description = "Desc",
                trainingEffect = true,
            ),
        )

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ModifyAction.Save)
            assertThat(awaitItem().process).isEqualTo(ProcessState.Success("Activity updated"))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getWorkout(workout.id)
            updateActivity(
                activity = activities[0],
                name = activities[0].name,
                eventType = null,
                course = null,
                water = null,
                feel = null,
                effort = null,
                workout = workout,
                gear = null,
            )
            updateStravaActivity(
                stravaActivity = stravaActivities[0],
                name = activities[0].name,
                description = "Desc",
                eventType = null,
                trainingEffect = activities[0].trainingEffect,
                trainingEffectFlag = true,
                workout = workout,
            )
        }
    }

    @Test
    fun `Save profile updates Garmin and Strava`() = runTest {
        coEvery { getWorkout(workout.id) } returns Result.success(workout)
        coEvery {
            quickUpdateActivity(
                activity = any(),
                profile = any(),
                water = any(),
                feel = any(),
                effort = any(),
                workout = any(),
                gear = any(),
            )
        } returns Result.success(Unit)
        coEvery {
            quickUpdateStravaActivity(
                activity = any(),
                stravaActivity = any(),
                profile = any(),
                description = any(),
                workout = any(),
            )
        } returns Result.success(Unit)

        viewModel = createViewModel()
        viewModel.seedStateForTest(
            ModifyState(
                process = ProcessState.Idle,
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                mode = ModifyMode.Profile,
                profile = profiles[0],
                description = "Desc",
                water = 20,
                feel = 50f,
                effort = 60f,
            ),
        )

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ModifyAction.Save)
            assertThat(awaitItem().process).isEqualTo(ProcessState.Success("Activity updated"))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getWorkout(workout.id)
            quickUpdateActivity(
                activity = activities[0],
                profile = profiles[0],
                water = 20,
                feel = 50f,
                effort = 60f,
                workout = workout,
                gear = null,
            )
            quickUpdateStravaActivity(
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                profile = profiles[0],
                description = "Desc",
                workout = workout,
            )
        }
    }
}
