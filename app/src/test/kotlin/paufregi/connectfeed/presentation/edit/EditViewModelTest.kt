package paufregi.connectfeed.presentation.edit

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
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.core.usecases.GetActivities
import paufregi.connectfeed.core.usecases.GetCourses
import paufregi.connectfeed.core.usecases.GetEventTypes
import paufregi.connectfeed.core.usecases.GetGears
import paufregi.connectfeed.core.usecases.GetStravaActivities
import paufregi.connectfeed.core.usecases.GetWorkout
import paufregi.connectfeed.core.usecases.UpdateActivity
import paufregi.connectfeed.core.usecases.UpdateStravaActivity
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import kotlin.time.Instant

@ExperimentalCoroutinesApi
class EditViewModelTest {

    private val getActivities = mockk<GetActivities>()
    private val getStravaActivities = mockk<GetStravaActivities>()
    private val getEventTypes = mockk<GetEventTypes>()
    private val getCourses = mockk<GetCourses>()
    private val updateActivity = mockk<UpdateActivity>()
    private val updateStravaActivity = mockk<UpdateStravaActivity>()
    private val getWorkout = mockk<GetWorkout>()
    private val getGears = mockk<GetGears>()

    private lateinit var viewModel: EditViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun createViewModel() = EditViewModel(
        getActivities,
        getStravaActivities,
        getGears,
        getEventTypes,
        getCourses,
        getWorkout,
        updateActivity,
        updateStravaActivity,
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

    val eventTypes = listOf(
        EventType.Training,
        EventType.Recreation,
    )

    val courses = listOf(
        Course(id = 1, name = "course1", distance = 1234.00, type = ActivityType.Running),
        Course(id = 2, name = "course2", distance = 12134.00, type = ActivityType.Cycling),
    )

    val gears = listOf(
        Gear(id = "shoe-1", name = "Shoe 1", type = GearType.Shoe),
        Gear(id = "bike-1", name = "Bike 1", type = GearType.Bike),
        Gear(id = "unknown-1", name = "Unknown 1", type = GearType.Unknown),
    )

    val initialState = EditState(
        process = ProcessState.Idle,
        gears = gears,
        eventTypes = eventTypes
    )

    @Before
    fun setup() {
        every { getGears() } returns flowOf(gears)
        every { getEventTypes() } returns eventTypes
    }

    @After
    fun tearDown() {
        verify { getGears() }
        confirmVerified(getActivities, getStravaActivities, updateActivity, updateStravaActivity, getWorkout, getGears)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(emptyList())
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEmpty()
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.gears).isEqualTo(gears)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.name).isNull()
            assertThat(state.eventType).isNull()
            assertThat(state.course).isNull()
            assertThat(state.gear).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.trainingEffect).isFalse()
            assertThat(state.hasStrava).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Initial state - with Strava`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.gears).isEqualTo(gears)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.name).isNull()
            assertThat(state.eventType).isNull()
            assertThat(state.course).isNull()
            assertThat(state.gear).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.trainingEffect).isFalse()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Fails to load activities`() = runTest {
        coEvery { getActivities(any()) } returns Result.failure("error")
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load Garmin activities"))
            assertThat(state.activities).isEmpty()
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Fails to load Strava activities`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.failure("error")
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load Strava activities"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEmpty()
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Fails to load courses`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.failure("error")

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load courses"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.courses).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Fails to load all data`() = runTest {
        coEvery { getActivities(any()) } returns Result.failure("error")
        coEvery { getStravaActivities(any()) } returns Result.failure("error")
        coEvery { getCourses(any()) } returns Result.failure("error")

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load Garmin & Strava activities & courses"))
            assertThat(state.activities).isEmpty()
            assertThat(state.stravaActivities).isEmpty()
            assertThat(state.courses).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set activity`() = runTest {
        val initState = initialState.copy(
            activity = activities[1],
            name = "name",
            eventType = eventTypes[0],
            course = courses[0],
            gear = gears[0],
            description = "description",
            water = 100,
            effort = 50f,
            feel = 75f
        )

        val expectedState = initialState.copy(
            activity = activities[0],
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetActivity(activities[0]))
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
            viewModel.onAction(EditAction.SetActivity(activities[0]))
            val expectedState = initState.copy(
                activity = activities[0],
                stravaActivity = stravaActivities[0],
            )
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set activity with Strava - no match`() = runTest {
        val initState = initialState.copy(
            stravaActivities = stravaActivities.drop(1),
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetActivity(activities[0]))
            val expectedState = initState.copy(
                activity = activities[0],
            )
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set name`() = runTest {
        val initState = initialState.copy(activity = activities[0])
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetName("Trail run"))
            val expectedState = initState.copy(name = "Trail run")
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set name - blank`() = runTest {
        val initState = initialState.copy(activity = activities[0], name = "Trail run")
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetName(""))
            val expectedState = initState.copy(name = null)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set name - no activity`() = runTest {
        viewModel = createViewModel()
        viewModel.seedStateForTest(initialState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetName("Trail run"))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set event type`() = runTest {
        val initState = initialState.copy(activity = activities[0])
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetEventType(EventType.Recreation))
            val expectedState = initState.copy(eventType = EventType.Recreation)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set event type - no activity`() = runTest {
        viewModel = createViewModel()
        viewModel.seedStateForTest(initialState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetEventType(EventType.Recreation))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set course`() = runTest {
        val initState = initialState.copy(activity = activities[0])
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            val expectedState = initState.copy(course = courses[0])
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set course - incompatible`() = runTest {
        val initState = initialState.copy(activity = activities[0])
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetCourse(courses[1]))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set course - not allowed for activity type`() = runTest {
        val swimmingActivity = activities[0].copy(type = ActivityType.Swimming)
        val swimmingCourse = Course(id = 3, name = "swim course", distance = 1000.00, type = ActivityType.Swimming)
        val initState = initialState.copy(activity = swimmingActivity)
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetCourse(swimmingCourse))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set course - no activity`() = runTest {
        viewModel = createViewModel()
        viewModel.seedStateForTest(initialState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetCourse(courses[1]))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set gear`() = runTest {
        val initState = initialState.copy(activity = activities[0])
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetGear(gears[0]))
            val expectedState = initState.copy(gear = gears[0])
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set gear - incompatible`() = runTest {
        val initState = initialState.copy(activity = activities[0])
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetGear(gears[1]))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set gear - no activity`() = runTest {
        viewModel = createViewModel()
        viewModel.seedStateForTest(initialState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetGear(gears[1]))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set description`() = runTest {
        val initState = initialState.copy(activity = activities[0])
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetDescription("Tempo day"))
            val expectedState = initState.copy(description = "Tempo day")
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set description - no activity`() = runTest {
        viewModel = createViewModel()
        viewModel.seedStateForTest(initialState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetDescription("Tempo day"))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set water`() = runTest {
        val initState = initialState.copy(activity = activities[0])
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetWater(750))
            val expectedState = initState.copy(water = 750)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set water - no activity`() = runTest {
        viewModel = createViewModel()
        viewModel.seedStateForTest(initialState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetWater(750))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set effort`() = runTest {
        val initState = initialState.copy(activity = activities[0])
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetEffort(50f))
            val expectedState = initState.copy(effort = 50f)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set effort - no activity`() = runTest {
        viewModel = createViewModel()
        viewModel.seedStateForTest(initialState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetEffort(50f))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set feel`() = runTest {
        val initState = initialState.copy(activity = activities[0])
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetFeel(80f))
            val expectedState = initState.copy(feel = 80f)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set feel - no activity`() = runTest {
        viewModel = createViewModel()
        viewModel.seedStateForTest(initialState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetFeel(80f))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set training effect`() = runTest {
        val initState = initialState.copy(activity = activities[0])
        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetTrainingEffect(true))
            val expectedState = initState.copy(trainingEffect = true)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Set training effect - no activity`() = runTest {
        viewModel = createViewModel()
        viewModel.seedStateForTest(initialState)

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetTrainingEffect(true))
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Save activity`() = runTest {
        coEvery { getWorkout(any()) } returns Result.success(workout)
        coEvery { updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { updateStravaActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val initState = initialState.copy(
            activity = activities[0],
            stravaActivity = stravaActivities[0],
            name = "Name",
            eventType = EventType.Training,
            course = courses[0],
            gear = gears[1],
            description = "description",
            water = 100,
            effort = 50f,
            feel = 80f,
            trainingEffect = true,
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(EditAction.Save)
            skipItems(2)
            val expectedState = initState.copy(process = ProcessState.Success("Activity updated"))
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getWorkout(workout.id)
            updateActivity(
                activities[0],
                "Name",
                EventType.Training,
                courses[0],
                100,
                80f,
                50f,
                workout,
                gears[1],
            )
            updateStravaActivity(
                stravaActivities[0],
                "Name",
                "description",
                EventType.Training,
                "recovery",
                true,
                workout,
            )
        }
    }

    @Test
    fun `Save activity - no strava`() = runTest {
        coEvery { getWorkout(any()) } returns Result.success(workout)
        coEvery { updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val initState = initialState.copy(
            activity = activities[0],
            name = "Name",
            eventType = EventType.Training,
            course = courses[0],
            gear = gears[1],
            description = "description",
            water = 100,
            effort = 50f,
            feel = 80f,
            trainingEffect = true,
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(EditAction.Save)
            skipItems(2)
            val expectedState = initState.copy(process = ProcessState.Success("Activity updated"))
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getWorkout(workout.id)
            updateActivity(
                activities[0],
                "Name",
                EventType.Training,
                courses[0],
                100,
                80f,
                50f,
                workout,
                gears[1],
            )
        }
    }

    @Test
    fun `Save activity - workout failure`() = runTest {
        coEvery { getWorkout(any()) } returns Result.failure("No workout")
        coEvery { updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { updateStravaActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val initState = initialState.copy(
            activity = activities[0],
            stravaActivity = stravaActivities[0],
            name = "Name",
            eventType = EventType.Training,
            course = courses[0],
            description = "description",
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(EditAction.Save)
            skipItems(2)
            val expectedState = initState.copy(process = ProcessState.Success("Activity updated"))
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getWorkout(workout.id)
            updateActivity(
                activities[0],
                "Name",
                EventType.Training,
                courses[0],
                null,
                null,
                null,
                null,
                null,
            )
            updateStravaActivity(
                stravaActivities[0],
                "Name",
                "description",
                EventType.Training,
                "recovery",
                false,
                null,
            )
        }
    }

    @Test
    fun `Save activity - Garmin failure`() = runTest {
        coEvery { getWorkout(any()) } returns Result.success(workout)
        coEvery { updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("error")
        coEvery { updateStravaActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)

        val initState = initialState.copy(
            activity = activities[0],
            stravaActivity = stravaActivities[0],
            name = "Name",
            eventType = EventType.Training,
            course = courses[0],
            description = "description",
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(EditAction.Save)
            skipItems(2)
            val expectedState = initState.copy(process = ProcessState.Failure("Couldn't update Garmin activity"))
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getWorkout(workout.id)
            updateActivity(
                activities[0],
                "Name",
                EventType.Training,
                courses[0],
                null,
                null,
                null,
                workout,
                null,
            )
            updateStravaActivity(
                stravaActivities[0],
                "Name",
                "description",
                EventType.Training,
                "recovery",
                false,
                workout,
            )
        }
    }

    @Test
    fun `Save activity - Strava failure`() = runTest {
        coEvery { getWorkout(any()) } returns Result.success(workout)
        coEvery { updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { updateStravaActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("error")

        val initState = initialState.copy(
            activity = activities[0],
            stravaActivity = stravaActivities[0],
            name = "Name",
            eventType = EventType.Training,
            course = courses[0],
            description = "description",
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(EditAction.Save)
            skipItems(2)
            val expectedState = initState.copy(process = ProcessState.Failure("Couldn't update Strava activity"))
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getWorkout(workout.id)
            updateActivity(
                activities[0],
                "Name",
                EventType.Training,
                courses[0],
                null,
                null,
                null,
                workout,
                null,
            )
            updateStravaActivity(
                stravaActivities[0],
                "Name",
                "description",
                EventType.Training,
                "recovery",
                false,
                workout,
            )
        }
    }

    @Test
    fun `Save activity - Garmin and Strava failure`() = runTest {
        coEvery { getWorkout(any()) } returns Result.success(workout)
        coEvery { updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("error")
        coEvery { updateStravaActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("error")

        val initState = initialState.copy(
            activity = activities[0],
            stravaActivity = stravaActivities[0],
            name = "Name",
            eventType = EventType.Training,
            course = courses[0],
            description = "description",
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(EditAction.Save)
            skipItems(2)
            val expectedState = initState.copy(process = ProcessState.Failure("Couldn't update Garmin & Strava activity"))
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getWorkout(workout.id)
            updateActivity(
                activities[0],
                "Name",
                EventType.Training,
                courses[0],
                null,
                null,
                null,
                workout,
                null,
            )
            updateStravaActivity(
                stravaActivities[0],
                "Name",
                "description",
                EventType.Training,
                "recovery",
                false,
                workout,
            )
        }
    }

    @Test
    fun `Restart action`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        val initState = initialState.copy(
            activity = activities[0],
            stravaActivity = stravaActivities[0],
            name = "Name",
            eventType = EventType.Training,
            course = courses[0],
            gear = gears[1],
            description = "description",
            water = 100,
            effort = 50f,
            feel = 80f,
            trainingEffect = true,
        )

        val expectedState = initialState.copy(
            activities = activities,
            stravaActivities = stravaActivities,
            courses = courses,
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(EditAction.Restart)
            skipItems(3)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(true)
            getStravaActivities(true)
            getCourses(true)
        }
    }

    @Test
    fun `Restart action - fails to reload activities`() = runTest {
        coEvery { getActivities(any()) } returns Result.failure("error")
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()
        viewModel.seedStateForTest(initialState.copy(activity = activities[0], gear = gears[0]))

        val initState = initialState.copy(
            activity = activities[0],
            stravaActivity = stravaActivities[0],
            name = "Name",
            eventType = EventType.Training,
            course = courses[0],
            gear = gears[1],
            description = "description",
            water = 100,
            effort = 50f,
            feel = 80f,
            trainingEffect = true,
        )

        val expectedState = initialState.copy(
            process = ProcessState.Failure("Couldn't load Garmin activities"),
            stravaActivities = stravaActivities,
            courses = courses,
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(EditAction.Restart)
            skipItems(3)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(true)
            getStravaActivities(true)
            getCourses(true)
        }
    }

    @Test
    fun `Restart action - fails to reload strava activities`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.failure("error")
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()
        viewModel.seedStateForTest(initialState.copy(activity = activities[0], gear = gears[0]))

        val initState = initialState.copy(
            activity = activities[0],
            stravaActivity = stravaActivities[0],
            name = "Name",
            eventType = EventType.Training,
            course = courses[0],
            gear = gears[1],
            description = "description",
            water = 100,
            effort = 50f,
            feel = 80f,
            trainingEffect = true,
        )

        val expectedState = initialState.copy(
            process = ProcessState.Failure("Couldn't load Strava activities"),
            activities = activities,
            courses = courses,
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(EditAction.Restart)
            skipItems(3)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(true)
            getStravaActivities(true)
            getCourses(true)
        }
    }

    @Test
    fun `Restart action - fails to reload all activities`() = runTest {
        coEvery { getActivities(any()) } returns Result.failure("error")
        coEvery { getStravaActivities(any()) } returns Result.failure("error")
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()
        viewModel.seedStateForTest(initialState.copy(activity = activities[0], gear = gears[0]))

        val initState = initialState.copy(
            activity = activities[0],
            stravaActivity = stravaActivities[0],
            name = "Name",
            eventType = EventType.Training,
            course = courses[0],
            gear = gears[1],
            description = "description",
            water = 100,
            effort = 50f,
            feel = 80f,
            trainingEffect = true,
        )

        val expectedState = initialState.copy(
            process = ProcessState.Failure("Couldn't load Garmin & Strava activities"),
            courses = courses,
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(EditAction.Restart)
            skipItems(3)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(true)
            getStravaActivities(true)
            getCourses(true)
        }
    }

    @Test
    fun `Restart action - fails to reload courses`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.failure("error")

        viewModel = createViewModel()
        viewModel.seedStateForTest(initialState.copy(activity = activities[0], gear = gears[0]))

        val initState = initialState.copy(
            activity = activities[0],
            stravaActivity = stravaActivities[0],
            name = "Name",
            eventType = EventType.Training,
            course = courses[0],
            gear = gears[1],
            description = "description",
            water = 100,
            effort = 50f,
            feel = 80f,
            trainingEffect = true,
        )

        val expectedState = initialState.copy(
            process = ProcessState.Failure("Couldn't load courses"),
            activities = activities,
            stravaActivities = stravaActivities,
        )

        viewModel = createViewModel()
        viewModel.seedStateForTest(initState)

        viewModel.state.test {
            viewModel.onAction(EditAction.Restart)
            skipItems(3)
            assertThat(awaitItem()).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(true)
            getStravaActivities(true)
            getCourses(true)
        }
    }

}
