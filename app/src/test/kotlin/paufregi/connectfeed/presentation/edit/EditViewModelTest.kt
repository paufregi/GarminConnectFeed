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
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.usecases.GetCourses
import paufregi.connectfeed.core.usecases.GetEventTypes
import paufregi.connectfeed.core.usecases.GetLatestActivities
import paufregi.connectfeed.core.usecases.GetLatestStravaActivities
import paufregi.connectfeed.core.usecases.UpdateActivity
import paufregi.connectfeed.core.usecases.UpdateStravaActivity
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
    fun setup(){

    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(emptyList())
        coEvery { getCourses() } returns Result.Success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

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

        coVerify{
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify{ getEventTypes() }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Initial state - with Strava`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        coEvery { getCourses() } returns Result.Success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

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

        coVerify{
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify{ getEventTypes() }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Fails to load activities`() = runTest {
        coEvery { getActivities() } returns Result.Failure("error")
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        coEvery { getCourses() } returns Result.Success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

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

        coVerify{
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify{ getEventTypes() }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Fails to load Strava activities`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Failure("error")
        coEvery { getCourses() } returns Result.Success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

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

        coVerify{
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify{ getEventTypes() }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Fails to load courses`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        coEvery { getCourses() } returns Result.Failure("error")
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

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

        coVerify{
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify{ getEventTypes() }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Fails to load all data`() = runTest {
        coEvery { getActivities() } returns Result.Failure("error")
        coEvery { getStravaActivities() } returns Result.Failure("error")
        coEvery { getCourses() } returns Result.Failure("error")
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

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

        coVerify{
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify{ getEventTypes() }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Set activity`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        coEvery { getCourses() } returns Result.Success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

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

        coVerify{
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify{ getEventTypes() }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Set Strava activity`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        coEvery { getCourses() } returns Result.Success(courses)
        every { getEventTypes() } returns eventTypes

        viewModel = EditViewModel(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)

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

        coVerify{
            getActivities()
            getStravaActivities()
            getCourses()
        }
        verify{ getEventTypes() }
        confirmVerified(getActivities, getStravaActivities, getEventTypes, getCourses, updateActivity, updateStravaActivity)
    }
}