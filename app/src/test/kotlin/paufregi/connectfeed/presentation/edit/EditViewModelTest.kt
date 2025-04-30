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
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.usecases.GetCourses
import paufregi.connectfeed.core.usecases.GetEventTypes
import paufregi.connectfeed.core.usecases.GetLatestActivities
import paufregi.connectfeed.core.usecases.GetLatestStravaActivities
import paufregi.connectfeed.core.usecases.UpdateActivity
import paufregi.connectfeed.core.usecases.UpdateStravaActivity
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import java.time.Instant

@ExperimentalCoroutinesApi
class EditViewModelTest {

    private val getActivities = mockk<GetLatestActivities>()
    private val getStravaActivities = mockk<GetLatestStravaActivities>()
    private val getEventTypes = mockk<GetEventTypes>()
    private val getCourses = mockk<GetCourses>()
    private val updateActivity = mockk<UpdateActivity>()
    private val updateStravaActivity = mockk<UpdateStravaActivity>()

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
            date = Instant.ofEpochMilli(1735693200000)
        ),
        Activity(
            id = 2L,
            name = "Cycling",
            type = ActivityType.Cycling,
            eventType = EventType.Training,
            distance = 17803.00,
            trainingEffect = "base",
            date = Instant.ofEpochMilli(1729705968000)
        ),
        Activity(
            id = 3L,
            name = "Running2",
            type = ActivityType.Running,
            eventType = EventType.Training,
            distance = 5234.00,
            trainingEffect = "base",
            date = Instant.ofEpochMilli(1729705968000)
        ),
    )

    val stravaActivities = listOf(
        Activity(
            id = 1L,
            name = "StravaRunning",
            type = ActivityType.Running,
            distance = 10234.00,
            date = Instant.ofEpochMilli(1735693200000)
        ),
        Activity(
            id = 2L,
            name = "StravaCycling",
            type = ActivityType.Cycling,
            distance = 17803.00,
            date = Instant.ofEpochMilli(1729705968000)
        ),
        Activity(
            id = 3L,
            name = "StravaRunning2",
            type = ActivityType.Running,
            distance = 5234.00,
            date = Instant.ofEpochMilli(1729705968000)
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

    @Before
    fun setup() {

    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(emptyList())
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
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
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )
    }

    @Test
    fun `Initial state - with Strava`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

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
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )
    }

    @Test
    fun `Fails to load activities`() = runTest {
        coEvery { getActivities() } returns Result.failure("error")
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load activities"))
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
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )
    }

    @Test
    fun `Fails to load Strava activities`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.failure("error")
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

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
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )
    }

    @Test
    fun `Fails to load courses`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.failure("error")
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

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
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )
    }

    @Test
    fun `Fails to load all data`() = runTest {
        coEvery { getActivities() } returns Result.failure("error")
        coEvery { getStravaActivities() } returns Result.failure("error")
        coEvery { getCourses() } returns Result.failure("error")
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load activities & Strava activities & courses"))
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
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )
    }

    @Test
    fun `Set activity`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

        viewModel.state.test {
            viewModel.onAction(EditAction.SetActivity(activities[0]))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
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
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )
    }

    @Test
    fun `Set activity after Strava activity & course - matching`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

        viewModel.state.test {
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[0]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            viewModel.onAction(EditAction.SetActivity(activities[2]))
            skipItems(3)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.activity).isEqualTo(activities[2])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.name).isNull()
            assertThat(state.eventType).isNull()
            assertThat(state.course).isEqualTo(courses[0])
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.trainingEffect).isFalse()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )
    }

    @Test
    fun `Set activity & & course Strava activity - no matching`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

        viewModel.state.test {
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[0]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            viewModel.onAction(EditAction.SetActivity(activities[1]))
            skipItems(3)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.activity).isEqualTo(activities[1])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[1])
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
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )
    }

    @Test
    fun `Set Strava activity`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

        viewModel.state.test {
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[0]))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
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
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )
    }

    @Test
    fun `Set Strava activity after activity & course - matching`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

        viewModel.state.test {
            viewModel.onAction(EditAction.SetActivity(activities[0]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[2]))
            skipItems(3)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[2])
            assertThat(state.name).isNull()
            assertThat(state.eventType).isNull()
            assertThat(state.course).isEqualTo(courses[0])
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.trainingEffect).isFalse()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )
    }

    @Test
    fun `Set Strava activity after activity & course - no matching`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

        viewModel.state.test {
            viewModel.onAction(EditAction.SetActivity(activities[0]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[1]))
            skipItems(3)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.activity).isEqualTo(activities[1])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[1])
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
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )
    }

    @Test
    fun `Set course`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

        viewModel.state.test {
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            skipItems(1)
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
            assertThat(state.course).isEqualTo(courses[0])
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.trainingEffect).isFalse()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )
    }

    @Test
    fun `Set course after activity & Strava activity - matching`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

        viewModel.state.test {
            viewModel.onAction(EditAction.SetActivity(activities[0]))
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[2]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            skipItems(3)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[2])
            assertThat(state.name).isNull()
            assertThat(state.eventType).isNull()
            assertThat(state.course).isEqualTo(courses[0])
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.trainingEffect).isFalse()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )
    }

    @Test
    fun `Set course after activity & Strava activity - no matching`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

        viewModel.state.test {
            viewModel.onAction(EditAction.SetActivity(activities[1]))
            viewModel.onAction(EditAction.SetStravaActivity(stravaActivities[1]))
            viewModel.onAction(EditAction.SetCourse(courses[0]))
            skipItems(2)
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
            assertThat(state.course).isEqualTo(courses[0])
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.trainingEffect).isFalse()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )
    }

    @Test
    fun `Set name`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

        viewModel.state.test {
            viewModel.onAction(EditAction.SetName("name"))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.name).isEqualTo("name")
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
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

    }

    @Test
    fun `Set description`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

        viewModel.state.test {
            viewModel.onAction(EditAction.SetDescription("description"))
            skipItems(1)
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
            assertThat(state.description).isEqualTo("description")
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.trainingEffect).isFalse()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

    }

    @Test
    fun `Set event type`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )

        viewModel.state.test {
            viewModel.onAction(EditAction.SetEventType(EventType.Recreation))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.name).isNull()
            assertThat(state.eventType).isEqualTo(EventType.Recreation)
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
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify { getEventTypes() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getEventTypes,
            getCourses,
            updateActivity,
            updateStravaActivity
        )
    }

    @Test
    fun `Set water`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(EditAction.SetWater(100))
            skipItems(1)
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
            assertThat(state.water).isEqualTo(100)
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.trainingEffect).isFalse()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify{ getEventTypes() }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

    }

    @Test
    fun `Set effort`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(EditAction.SetEffort(50f))
            skipItems(1)
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
            assertThat(state.effort).isEqualTo(50f)
            assertThat(state.feel).isNull()
            assertThat(state.trainingEffect).isFalse()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify{ getEventTypes() }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

    }

    @Test
    fun `Set feel`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(EditAction.SetFeel(80f))
            skipItems(1)
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
            assertThat(state.feel).isEqualTo(80f)
            assertThat(state.trainingEffect).isFalse()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify{ getEventTypes() }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

    }

    @Test
    fun `Set training effect`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(EditAction.SetTrainingEffect(true))
            skipItems(1)
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
            assertThat(state.trainingEffect).isTrue()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify{ getEventTypes() }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

    }

    @Test
    fun `Save activity`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes
        coEvery { updateActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { updateStravaActivity(any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)

        viewModel = EditViewModel(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

        viewModel.state.test {
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
            assertThat(state.process).isEqualTo(ProcessState.Success("Activity updated"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.name).isEqualTo("name")
            assertThat(state.eventType).isEqualTo(eventTypes[0])
            assertThat(state.course).isEqualTo(courses[0])
            assertThat(state.description).isEqualTo("description")
            assertThat(state.water).isEqualTo(100)
            assertThat(state.effort).isEqualTo(50f)
            assertThat(state.feel).isEqualTo(80f)
            assertThat(state.trainingEffect).isTrue()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
            getCourses()
            updateActivity(activities[0], "name", eventTypes[0], courses[0], 100, 80f, 50f)
            updateStravaActivity(stravaActivities[0], "name", "description", eventTypes[0], "recovery", true)
        }
        verify{ getEventTypes() }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

    }

    @Test
    fun `Save activity - failure`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes
        coEvery { updateActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("error")
        coEvery { updateStravaActivity(any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)

        viewModel = EditViewModel(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

        viewModel.state.test {
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
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't update activity"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.name).isEqualTo("name")
            assertThat(state.eventType).isEqualTo(eventTypes[0])
            assertThat(state.course).isEqualTo(courses[0])
            assertThat(state.description).isEqualTo("description")
            assertThat(state.water).isEqualTo(100)
            assertThat(state.effort).isEqualTo(50f)
            assertThat(state.feel).isEqualTo(80f)
            assertThat(state.trainingEffect).isTrue()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
            getCourses()
            updateActivity(activities[0], "name", eventTypes[0], courses[0], 100, 80f, 50f)
            updateStravaActivity(stravaActivities[0], "name", "description", eventTypes[0], "recovery", true)
        }
        verify{ getEventTypes() }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

    }

    @Test
    fun `Save activity - Strava failure`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes
        coEvery { updateActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { updateStravaActivity(any(), any(), any(), any(), any(), any()) } returns Result.failure("error")

        viewModel = EditViewModel(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

        viewModel.state.test {
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
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't update Strava activity"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.name).isEqualTo("name")
            assertThat(state.eventType).isEqualTo(eventTypes[0])
            assertThat(state.course).isEqualTo(courses[0])
            assertThat(state.description).isEqualTo("description")
            assertThat(state.water).isEqualTo(100)
            assertThat(state.effort).isEqualTo(50f)
            assertThat(state.feel).isEqualTo(80f)
            assertThat(state.trainingEffect).isTrue()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
            getCourses()
            updateActivity(activities[0], "name", eventTypes[0], courses[0], 100, 80f, 50f)
            updateStravaActivity(stravaActivities[0], "name", "description", eventTypes[0], "recovery", true)
        }
        verify{ getEventTypes() }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Save activity - both failure`() = runTest {
        coEvery { getActivities() } returns Result.success(activities)
        coEvery { getStravaActivities() } returns Result.success(stravaActivities)
        coEvery { getCourses() } returns Result.success(courses)
        every { getEventTypes() } returns eventTypes
        coEvery { updateActivity(any(), any(), any(), any(), any(), any(), any()) } returns Result.failure("error")
        coEvery { updateStravaActivity(any(), any(), any(), any(), any(), any()) } returns Result.failure("error")

        viewModel = EditViewModel(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

        viewModel.state.test {
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
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't update activity & Strava activity"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.name).isEqualTo("name")
            assertThat(state.eventType).isEqualTo(eventTypes[0])
            assertThat(state.course).isEqualTo(courses[0])
            assertThat(state.description).isEqualTo("description")
            assertThat(state.water).isEqualTo(100)
            assertThat(state.effort).isEqualTo(50f)
            assertThat(state.feel).isEqualTo(80f)
            assertThat(state.trainingEffect).isTrue()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
            getCourses()
            updateActivity(activities[0], "name", eventTypes[0], courses[0], 100, 80f, 50f)
            updateStravaActivity(stravaActivities[0], "name", "description", eventTypes[0], "recovery", true)
        }
        verify{ getEventTypes() }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)
    }
}
