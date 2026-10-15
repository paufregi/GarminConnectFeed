package paufregi.connectfeed.presentation.app.activities.edit

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.core.usecases.GetActivity
import paufregi.connectfeed.core.usecases.GetCourses
import paufregi.connectfeed.core.usecases.GetEventTypes
import paufregi.connectfeed.core.usecases.GetGears
import paufregi.connectfeed.core.usecases.GetWorkout
import paufregi.connectfeed.core.usecases.UpdateActivity
import paufregi.connectfeed.presentation.ui.components.notification.NotificationManager
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class EditViewModelTest {

    private val getActivity = mockk<GetActivity>()
    private val getEventTypes = mockk<GetEventTypes>()
    private val getCourses = mockk<GetCourses>()
    private val getGears = mockk<GetGears>()
    private val getWorkout = mockk<GetWorkout>()
    private val updateActivity = mockk<UpdateActivity>()
    private val notification = mockk<NotificationManager>(relaxed = true)

    private val activity = Activity(
        1, "Morning run", ActivityType.Running, EventType.Training,
        date = LocalDateTime.MIN, workoutId = 8, stravaId = 9
    )
    private val course = Course(2, "Park loop", 5.0, ActivityType.Running)
    private val gear = Gear("shoe-1", "Trainers", GearType.Shoe)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun createViewModel() =
        EditViewModel(
            getActivity, getEventTypes, getCourses, getGears, getWorkout,
            updateActivity, notification, activity.id, activity.stravaId
        )

    private fun stubInitialData() {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        every { getEventTypes() } returns listOf(EventType.Training, EventType.Race)
        every { getGears() } returns flowOf(listOf(gear))
        coEvery { getCourses() } returns Result.success(listOf(course))
    }

    @Test
    fun `Loads activity and compatible edit choices`() = runTest {
        stubInitialData()
        val viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.activity).isEqualTo(activity)
            assertThat(state.eventTypes).containsExactly(EventType.Training, EventType.Race).inOrder()
            assertThat(state.courses).containsExactly(course)
            assertThat(state.gears).containsExactly(gear)
            assertThat(state.availableCourses).containsExactly(course)
            assertThat(state.availableGears).containsExactly(gear)
            assertThat(state.status).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getCourses() }
    }

    @Test
    fun `Missing activity reports error and navigates back`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns null
        every { getEventTypes() } returns emptyList()
        every { getGears() } returns flowOf(emptyList())

        val viewModel = createViewModel()

        verify { notification.show("Activity not found") }
        viewModel.effects.test {
            assertThat(awaitItem()).isEqualTo(EditEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Course load failure notifies and resets status`() = runTest {
        stubInitialData()
        coEvery { getCourses() } returns Result.failure(IllegalStateException("offline"))

        val viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.status).isNull()
            assertThat(state.courses).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }

        verify { notification.show("Failed to load courses: offline") }
    }

    @Test
    fun `Actions update values and effort rejects nonpositive values`() = runTest {
        stubInitialData()
        val viewModel = createViewModel()

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(EditAction.SetName("Updated"))
            viewModel.onAction(EditAction.SetEventType(EventType.Race))
            viewModel.onAction(EditAction.SetCourse(course))
            viewModel.onAction(EditAction.SetGear(gear))
            viewModel.onAction(EditAction.SetDescription("description"))
            viewModel.onAction(EditAction.SetWater(500))
            viewModel.onAction(EditAction.SetEffort(-1f))
            viewModel.onAction(EditAction.SetFeel(3f))
            viewModel.onAction(EditAction.SetTrainingEffect(true))
            runCurrent()
            val state = viewModel.state.value
            assertThat(state.name).isEqualTo("Updated")
            assertThat(state.eventType).isEqualTo(EventType.Race)
            assertThat(state.course).isEqualTo(course)
            assertThat(state.gear).isEqualTo(gear)
            assertThat(state.description).isEqualTo("description")
            assertThat(state.water).isEqualTo(500)
            assertThat(state.effort).isNull()
            assertThat(state.feel).isEqualTo(3f)
            assertThat(state.trainingEffect).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Save success passes edited values and workout`() = runTest {
        stubInitialData()
        val workout = Workout(8, "Intervals")
        coEvery { getWorkout(8) } returns Result.success(workout)
        coEvery {
            updateActivity(
                activity, "Updated", "description", EventType.Race, course, 250,
                4f, 2f, workout, gear, true
            )
        } returns Result.success(Unit)

        val viewModel = createViewModel()

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(EditAction.SetName("Updated"))
            viewModel.onAction(EditAction.SetDescription("description"))
            viewModel.onAction(EditAction.SetEventType(EventType.Race))
            viewModel.onAction(EditAction.SetCourse(course))
            viewModel.onAction(EditAction.SetGear(gear))
            viewModel.onAction(EditAction.SetWater(250))
            viewModel.onAction(EditAction.SetFeel(4f))
            viewModel.onAction(EditAction.SetEffort(2f))
            viewModel.onAction(EditAction.SetTrainingEffect(true))
            viewModel.onAction(EditAction.Save)
            runCurrent()
            assertThat(viewModel.state.value.status).isEqualTo(Status.Success)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getWorkout(8) }
        coVerify {
            updateActivity(
                activity, "Updated", "description", EventType.Race, course, 250,
                4f, 2f, workout, gear, true
            )
        }
    }

    @Test
    fun `Save failure updates status`() = runTest {
        stubInitialData()
        coEvery { getWorkout(8) } returns Result.failure(IllegalStateException("workout unavailable"))
        coEvery {
            updateActivity(activity, activity.name, null, null, null, null, null, null, null, null, false)
        } returns Result.failure(IllegalStateException("save failed"))

        val viewModel = createViewModel()

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(EditAction.Save)
            runCurrent()
            assertThat(viewModel.state.value.status).isEqualTo(Status.Failure("save failed"))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getWorkout(8) }
        coVerify {
            updateActivity(activity, activity.name, null, null, null, null, null, null, null, null, false)
        }
    }
}
