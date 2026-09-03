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

    val activities = listOf(
        Activity(
            id = 1L,
            name = "Running",
            type = ActivityType.Running,
            eventType = EventType.Training,
            distance = 10234.00,
            trainingEffect = "recovery",
            date = Instant.fromEpochMilliseconds(1735693200000),
            workoutId = 1
        ),
        Activity(
            id = 2L,
            name = "Cycling",
            type = ActivityType.Cycling,
            eventType = EventType.Training,
            distance = 17803.00,
            trainingEffect = "base",
            date = Instant.fromEpochMilliseconds(1729705968000),
            workoutId = 2
        ),
        Activity(
            id = 3L,
            name = "Running2",
            type = ActivityType.Running,
            eventType = EventType.Training,
            distance = 5234.00,
            trainingEffect = "base",
            date = Instant.fromEpochMilliseconds(1729705968000),
            workoutId = 3
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
        EventType.Recreation
    )

    val courses = listOf(
        Course(id = 1, name = "course1", distance = 1234.00, type = ActivityType.Running),
        Course(id = 2, name = "course2", distance = 12134.00, type = ActivityType.Cycling)
    )

    val workout = Workout(1, "workout")

    val gears = listOf(
        Gear(id = "bike-1", name = "Bike 1", type = GearType.Bike),
        Gear(id = "shoe-1", name = "Shoe 1", type = GearType.Shoe),
        Gear(id = "unknown-1", name = "Unknown 1", type = GearType.Unknown),
    )

    private fun createViewModel() = EditViewModel(
        getActivities,
        getStravaActivities,
        getEventTypes,
        getCourses,
        updateActivity,
        updateStravaActivity,
        getWorkout,
        getGears
    )

    @Before
    fun setup() {
        every { getGears() } returns flowOf(gears)
        every { getEventTypes() } returns eventTypes
    }

    @After
    fun tearDown() {
        verify {
            getGears()
            getEventTypes()
        }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity, getWorkout, getGears)
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
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.name).isNull()
            assertThat(state.eventType).isNull()
            assertThat(state.course).isNull()
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
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.name).isNull()
            assertThat(state.eventType).isNull()
            assertThat(state.course).isNull()
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
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.name).isNull()
            assertThat(state.eventType).isNull()
            assertThat(state.course).isNull()
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
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEmpty()
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.name).isNull()
            assertThat(state.eventType).isNull()
            assertThat(state.course).isNull()
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
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEmpty()
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.name).isNull()
            assertThat(state.eventType).isNull()
            assertThat(state.course).isNull()
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
        verify { getEventTypes() }
    }

    @Test
    fun `Set activity`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetActivity(activities[0]))
            val state = awaitItem()
            val expectedState = initialState.copy(
                activity = activities[0],
                stravaActivity = stravaActivities[0],
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set activity after Strava activity & course - matching`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[0]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            viewModel.onAction(EditAction.SetActivity(activities[2]))
            skipItems(2)
            val state = awaitItem()
            val expectedState = initialState.copy(
                activity = activities[2],
                stravaActivity = stravaActivities[0],
                course = courses[0],
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set activity & & course Strava activity - no matching`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[0]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            viewModel.onAction(EditAction.SetActivity(activities[1]))
            skipItems(2)
            val state = awaitItem()
            val expectedState = initialState.copy(
                activity = activities[1],
                stravaActivity = stravaActivities[1],
                course = null,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set Strava activity`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[0]))
            val state = awaitItem()
            val expectedState = initialState.copy(
                activity = activities[0],
                stravaActivity = stravaActivities[0],
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set Strava activity after activity & course - matching`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetActivity(activities[0]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[2]))
            skipItems(2)
            val state = awaitItem()
            val expectedState = initialState.copy(
                activity = activities[0],
                stravaActivity = stravaActivities[2],
                course = courses[0],
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set Strava activity after activity & course - no matching`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetActivity(activities[0]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[1]))
            skipItems(2)
            val state = awaitItem()
            val expectedState = initialState.copy(
                activity = activities[1],
                stravaActivity = stravaActivities[1],
                course = null,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set course`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            val state = awaitItem()
            val expectedState = initialState.copy(course = courses[0])
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set course after activity & Strava activity - matching`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetActivity(activities[0]))
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[2]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            skipItems(2)
            val state = awaitItem()
            val expectedState = initialState.copy(
                activity = activities[0],
                stravaActivity = stravaActivities[2],
                course = courses[0],
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set course after activity & Strava activity - no matching`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetActivity(activities[1]))
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[1]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            skipItems(1)
            val state = awaitItem()
            val expectedState = initialState.copy(
                activity = null,
                stravaActivity = null,
                course = courses[0],
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set name`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetName("name"))
            val state = awaitItem()
            val expectedState = initialState.copy(name = "name")
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set name - blank`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetName("test"))
            viewModel.onAction(EditAction.SetName(""))
            skipItems(1)
            val state = awaitItem()
            val expectedState = initialState.copy(name = null)
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set description`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetDescription("description"))
            val state = awaitItem()
            val expectedState = initialState.copy(description = "description")
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set event type`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetEventType(EventType.Recreation))
            val state = awaitItem()
            val expectedState = initialState.copy(eventType = EventType.Recreation)
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set gear`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetGear(gears[0]))
            val state = awaitItem()
            val expectedState = initialState.copy(gear = gears[0])
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
        verify { getEventTypes() }
    }

    @Test
    fun `Set activity clears incompatible gear`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetGear(gears[1]))
            viewModel.onAction(EditAction.SetActivity(activities[1]))
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
            getCourses(false)
        }
        verify { getEventTypes() }
    }

    @Test
    fun `Set Strava activity clears incompatible gear`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetGear(gears[1]))
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[1]))
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
            getCourses(false)
        }
    }

    @Test
    fun `Set water`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetWater(100))
            val state = awaitItem()
            val expectedState = initialState.copy(water = 100)
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set effort`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetEffort(50f))
            val state = awaitItem()
            val expectedState = initialState.copy(effort = 50f)
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set feel`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetFeel(80f))
            val state = awaitItem()
            val expectedState = initialState.copy(feel = 80f)
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Set training effect`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetTrainingEffect(true))
            val state = awaitItem()
            val expectedState = initialState.copy(trainingEffect = true)
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
        }
    }

    @Test
    fun `Save activity`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)
        coEvery { getWorkout(any()) } returns Result.success(workout)
        coEvery { updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { updateStravaActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetActivity(activities[0]))
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[0]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            viewModel.onAction(EditAction.SetGear(gears[1]))
            viewModel.onAction(EditAction.SetEventType(eventTypes[0]))
            viewModel.onAction(EditAction.SetName("name"))
            viewModel.onAction(EditAction.SetDescription("description"))
            viewModel.onAction(EditAction.SetWater(100))
            viewModel.onAction(EditAction.SetEffort(50f))
            viewModel.onAction(EditAction.SetFeel(80f))
            viewModel.onAction(EditAction.SetTrainingEffect(true))
            viewModel.onAction(EditAction.Save)
            skipItems(11)
            val state = awaitItem()
            val expectedState = initialState.copy(
                process = ProcessState.Success("Activity updated"),
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                name = "name",
                eventType = eventTypes[0],
                course = courses[0],
                gear = gears[1],
                description = "description",
                water = 100,
                effort = 50f,
                feel = 80f,
                trainingEffect = true,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
            getWorkout(workout.id)
            updateActivity(activities[0], "name", eventTypes[0], courses[0], 100, 80f, 50f, workout, gears[1])
            updateStravaActivity(stravaActivities[0], "name", "description", eventTypes[0], "recovery", true, workout)
        }
    }

    @Test
    fun `Save activity - failed to load workout`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)
        coEvery { getWorkout(any()) } returns Result.failure("No workout")
        coEvery { updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { updateStravaActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetActivity(activities[0]))
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[0]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            viewModel.onAction(EditAction.SetEventType(eventTypes[0]))
            viewModel.onAction(EditAction.SetName("name"))
            viewModel.onAction(EditAction.SetDescription("description"))
            viewModel.onAction(EditAction.SetWater(100))
            viewModel.onAction(EditAction.SetEffort(50f))
            viewModel.onAction(EditAction.SetFeel(80f))
            viewModel.onAction(EditAction.SetTrainingEffect(true))
            viewModel.onAction(EditAction.Save)
            skipItems(10)
            val state = awaitItem()
            val expectedState = initialState.copy(
                process = ProcessState.Success("Activity updated"),
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                name = "name",
                eventType = eventTypes[0],
                course = courses[0],
                gear = null,
                description = "description",
                water = 100,
                effort = 50f,
                feel = 80f,
                trainingEffect = true,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
            getWorkout(workout.id)
            updateActivity(activities[0], "name", eventTypes[0], courses[0], 100, 80f, 50f, null, null)
            updateStravaActivity(stravaActivities[0], "name", "description", eventTypes[0], "recovery", true, null)
        }
    }

    @Test
    fun `Save activity - failure`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)
        coEvery { getWorkout(any()) } returns Result.success(workout)
        coEvery { updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("error")
        coEvery { updateStravaActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetActivity(activities[0]))
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[0]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            viewModel.onAction(EditAction.SetEventType(eventTypes[0]))
            viewModel.onAction(EditAction.SetName("name"))
            viewModel.onAction(EditAction.SetDescription("description"))
            viewModel.onAction(EditAction.SetWater(100))
            viewModel.onAction(EditAction.SetEffort(50f))
            viewModel.onAction(EditAction.SetFeel(80f))
            viewModel.onAction(EditAction.SetTrainingEffect(true))
            viewModel.onAction(EditAction.Save)
            skipItems(10)
            val state = awaitItem()
            val expectedState = initialState.copy(
                process = ProcessState.Failure("Couldn't update Garmin activity"),
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                name = "name",
                eventType = eventTypes[0],
                course = courses[0],
                description = "description",
                water = 100,
                effort = 50f,
                feel = 80f,
                trainingEffect = true,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
            getWorkout(workout.id)
            updateActivity(activities[0], "name", eventTypes[0], courses[0], 100, 80f, 50f, workout, null)
            updateStravaActivity(stravaActivities[0], "name", "description", eventTypes[0], "recovery", true, workout)
        }
    }

    @Test
    fun `Save activity - Strava failure`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)
        coEvery { getWorkout(any()) } returns Result.success(workout)
        coEvery { updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery {
            updateStravaActivity(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns Result.failure("error")

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetActivity(activities[0]))
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[0]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            viewModel.onAction(EditAction.SetEventType(eventTypes[0]))
            viewModel.onAction(EditAction.SetName("name"))
            viewModel.onAction(EditAction.SetDescription("description"))
            viewModel.onAction(EditAction.SetWater(100))
            viewModel.onAction(EditAction.SetEffort(50f))
            viewModel.onAction(EditAction.SetFeel(80f))
            viewModel.onAction(EditAction.SetTrainingEffect(true))
            viewModel.onAction(EditAction.Save)
            skipItems(10)
            val state = awaitItem()
            val expectedState = initialState.copy(
                process = ProcessState.Failure("Couldn't update Strava activity"),
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                name = "name",
                eventType = eventTypes[0],
                course = courses[0],
                description = "description",
                water = 100,
                effort = 50f,
                feel = 80f,
                trainingEffect = true,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
            getWorkout(workout.id)
            updateActivity(activities[0], "name", eventTypes[0], courses[0], 100, 80f, 50f, workout, null)
            updateStravaActivity(
                stravaActivities[0],
                "name",
                "description",
                eventTypes[0],
                "recovery",
                true,
                workout
            )
        }
    }

    @Test
    fun `Save activity - both failure`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)
        coEvery { updateActivity(any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("error")
        coEvery { updateStravaActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("error")
        coEvery { getWorkout(any()) } returns Result.success(workout)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.SetActivity(activities[0]))
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[0]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            viewModel.onAction(EditAction.SetEventType(eventTypes[0]))
            viewModel.onAction(EditAction.SetName("name"))
            viewModel.onAction(EditAction.SetDescription("description"))
            viewModel.onAction(EditAction.SetWater(100))
            viewModel.onAction(EditAction.SetEffort(50f))
            viewModel.onAction(EditAction.SetFeel(80f))
            viewModel.onAction(EditAction.SetTrainingEffect(true))
            viewModel.onAction(EditAction.Save)
            skipItems(10)
            val state = awaitItem()
            val expectedState = initialState.copy(
                process = ProcessState.Failure("Couldn't update Garmin & Strava activity"),
                activity = activities[0],
                stravaActivity = stravaActivities[0],
                name = "name",
                eventType = eventTypes[0],
                course = courses[0],
                description = "description",
                water = 100,
                effort = 50f,
                feel = 80f,
                trainingEffect = true,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
            getCourses(false)
            updateActivity(activities[0], "name", eventTypes[0], courses[0], 100, 80f, 50f, workout, null)
            updateStravaActivity(stravaActivities[0], "name", "description", eventTypes[0], "recovery", true, workout)
            getWorkout(workout.id)
        }
    }

    @Test
    fun `Restart action`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        coEvery { getCourses(any()) } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onAction(EditAction.Restart)
            skipItems(2)
            val state = awaitItem()
            val expectedState = initialState.copy(
                process = ProcessState.Idle,
                activity = null,
                stravaActivity = null,
                name = null,
                eventType = null,
                course = null,
                gear = null,
                description = null,
                water = null,
                effort = null,
                feel = null,
                trainingEffect = false,
            )
            assertThat(state).isEqualTo(expectedState)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getActivities(true)
            getStravaActivities(false)
            getStravaActivities(true)
            getCourses(false)
            getCourses(true)
        }
    }
}
