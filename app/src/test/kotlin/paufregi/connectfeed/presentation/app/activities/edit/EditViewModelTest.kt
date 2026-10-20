package paufregi.connectfeed.presentation.app.activities.edit

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
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
    private val eventTypes = listOf(EventType.Training, EventType.Race)
    private val course = Course(2, "Park loop", 5.0, ActivityType.Running)
    private val bikeCourse = Course(3, "Hill climb", 20.0, ActivityType.Cycling)
    private val gear = Gear("shoe-1", "Trainers", GearType.Shoe)
    private val bike = Gear("bike-1", "Road bike", GearType.Bike)
    private val workout = Workout(8, "Intervals")

    private lateinit var viewModel: EditViewModel

    private fun createViewModel() = EditViewModel(
        getActivity, getEventTypes, getCourses, getGears, getWorkout,
        updateActivity, notification, activity.id, activity.stravaId
    )

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        every { getEventTypes() } returns eventTypes
        every { getGears() } returns flowOf(listOf(gear, bike))
    }

    @After
    fun tearDown() {
        confirmVerified(getActivity, getEventTypes, getCourses, getGears, getWorkout, updateActivity, notification)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course, bikeCourse))

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.status).isNull()
            assertThat(state.activity).isEqualTo(activity)
            assertThat(state.eventTypes).containsExactly(EventType.Training, EventType.Race).inOrder()
            assertThat(state.courses).containsExactly(course, bikeCourse)
            assertThat(state.gears).containsExactly(gear, bike)
            assertThat(state.availableCourses).containsExactly(course)
            assertThat(state.availableGears).containsExactly(gear)
            assertThat(state.name).isNull()
            assertThat(state.eventType).isNull()
            assertThat(state.course).isNull()
            assertThat(state.gear).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.trainingEffect).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Initial state - fail to load courses`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.failure(IllegalStateException("offline"))

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.status).isNull()
            assertThat(state.activity).isEqualTo(activity)
            assertThat(state.courses).isEmpty()
            assertThat(state.availableCourses).isEmpty()
            assertThat(state.gears).containsExactly(gear, bike)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        verify { notification.show("Failed to load courses: offline") }
        coVerify { getCourses() }
    }

    @Test
    fun `Initial state - missing activity`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns null

        viewModel = createViewModel()

        viewModel.effects.test {
            assertThat(awaitItem()).isEqualTo(EditEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        verify { notification.show("Activity not found") }
    }

    @Test
    fun `Set name`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetName("Updated"))

            assertThat(awaitItem().name).isEqualTo("Updated")
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Set name - null`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(EditState(activity = activity, eventTypes = eventTypes, name = "Updated"))
            assertThat(awaitItem().name).isEqualTo("Updated")
            viewModel.onAction(EditAction.SetName(null))

            assertThat(awaitItem().name).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Set event type`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetEventType(EventType.Race))

            assertThat(awaitItem().eventType).isEqualTo(EventType.Race)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Set course`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetCourse(course))

            assertThat(awaitItem().course).isEqualTo(course)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Set course - incompatible`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course, bikeCourse))

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(EditState(activity = activity, eventTypes = eventTypes, course = course))
            val seeded = awaitItem()
            assertThat(seeded.course).isEqualTo(course)

            viewModel.onAction(EditAction.SetCourse(bikeCourse))

            expectNoEvents()
            assertThat(viewModel.state.value).isEqualTo(seeded)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Set gear`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetGear(gear))

            assertThat(awaitItem().gear).isEqualTo(gear)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Set gear - incompatible`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(EditState(activity = activity, eventTypes = eventTypes, gear = gear))
            val seeded = awaitItem()
            assertThat(seeded.gear).isEqualTo(gear)

            viewModel.onAction(EditAction.SetGear(bike))

            expectNoEvents()
            assertThat(viewModel.state.value).isEqualTo(seeded)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Set description`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetDescription("description"))

            assertThat(awaitItem().description).isEqualTo("description")
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Set water`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetWater(500))

            assertThat(awaitItem().water).isEqualTo(500)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Set effort`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetEffort(2f))

            assertThat(awaitItem().effort).isEqualTo(2f)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Set effort - zero`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(EditState(activity = activity, eventTypes = eventTypes, effort = 3f))
            assertThat(awaitItem().effort).isEqualTo(3f)
            viewModel.onAction(EditAction.SetEffort(0f))

            assertThat(awaitItem().effort).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Set effort - negative`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(EditState(activity = activity, eventTypes = eventTypes, effort = 3f))
            assertThat(awaitItem().effort).isEqualTo(3f)
            viewModel.onAction(EditAction.SetEffort(-1f))

            assertThat(awaitItem().effort).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Set effort - null`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(EditState(activity = activity, eventTypes = eventTypes, effort = 3f))
            assertThat(awaitItem().effort).isEqualTo(3f)
            viewModel.onAction(EditAction.SetEffort(null))

            assertThat(awaitItem().effort).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Set feel`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetFeel(3f))

            assertThat(awaitItem().feel).isEqualTo(3f)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Set training effect`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.SetTrainingEffect(true))

            assertThat(awaitItem().trainingEffect).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Reset status`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(EditState(status = Status.Failure("error"), activity = activity, eventTypes = eventTypes))
            assertThat(awaitItem().status).isEqualTo(Status.Failure("error"))
            viewModel.onAction(EditAction.ResetStatus)

            assertThat(awaitItem().status).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
    }

    @Test
    fun `Save - success`() = runTest {
        val result = CompletableDeferred<Result<Unit>>()
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))
        coEvery { getWorkout(8) } returns Result.success(workout)
        coEvery {
            updateActivity(activity, "Updated", "description", EventType.Race, course, 250, 4f, 2f, workout, gear, true)
        } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(
                EditState(
                    status = null,
                    activity = activity,
                    eventTypes = eventTypes,
                    name = "Updated",
                    eventType = EventType.Race,
                    course = course,
                    gear = gear,
                    description = "description",
                    water = 250,
                    effort = 2f,
                    feel = 4f,
                    trainingEffect = true,
                )
            )
            assertThat(awaitItem().name).isEqualTo("Updated")
            viewModel.onAction(EditAction.Save)

            assertThat(awaitItem().status).isEqualTo(Status.Loading)
            result.complete(Result.success(Unit))
            assertThat(awaitItem().status).isEqualTo(Status.Success)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
        coVerify { getWorkout(8) }
        coVerify { updateActivity(activity, "Updated", "description", EventType.Race, course, 250, 4f, 2f, workout, gear, true) }
    }

    @Test
    fun `Save - failure`() = runTest {
        val result = CompletableDeferred<Result<Unit>>()
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))
        coEvery { getWorkout(8) } returns Result.success(workout)
        coEvery {
            updateActivity(activity, "Updated", null, null, null, null, null, null, workout, null, false)
        } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(EditState(status = null, activity = activity, eventTypes = eventTypes, name = "Updated"))
            assertThat(awaitItem().name).isEqualTo("Updated")
            viewModel.onAction(EditAction.Save)

            assertThat(awaitItem().status).isEqualTo(Status.Loading)
            result.complete(Result.failure(IllegalStateException("save failed")))
            assertThat(awaitItem().status).isEqualTo(Status.Failure("save failed"))
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
        coVerify { getWorkout(8) }
        coVerify { updateActivity(activity, "Updated", null, null, null, null, null, null, workout, null, false) }
    }

    @Test
    fun `Save - failure without message`() = runTest {
        val result = CompletableDeferred<Result<Unit>>()
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))
        coEvery { getWorkout(8) } returns Result.success(workout)
        coEvery {
            updateActivity(activity, "Updated", null, null, null, null, null, null, workout, null, false)
        } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(EditState(status = null, activity = activity, eventTypes = eventTypes, name = "Updated"))
            assertThat(awaitItem().name).isEqualTo("Updated")
            viewModel.onAction(EditAction.Save)

            assertThat(awaitItem().status).isEqualTo(Status.Loading)
            result.complete(Result.failure(Exception()))
            assertThat(awaitItem().status).isEqualTo(Status.Failure("Something went wrong: unknown error"))
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
        coVerify { getWorkout(8) }
        coVerify { updateActivity(activity, "Updated", null, null, null, null, null, null, workout, null, false) }
    }

    @Test
    fun `Save - default name`() = runTest {
        val result = CompletableDeferred<Result<Unit>>()
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))
        coEvery { getWorkout(8) } returns Result.success(workout)
        coEvery {
            updateActivity(activity, activity.name, null, null, null, null, null, null, workout, null, false)
        } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(EditAction.Save)

            assertThat(awaitItem().status).isEqualTo(Status.Loading)
            result.complete(Result.success(Unit))
            assertThat(awaitItem().status).isEqualTo(Status.Success)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
        coVerify { getWorkout(8) }
        coVerify { updateActivity(activity, activity.name, null, null, null, null, null, null, workout, null, false) }
    }

    @Test
    fun `Save - no workout`() = runTest {
        val noWorkoutActivity = activity.copy(workoutId = null)
        val result = CompletableDeferred<Result<Unit>>()
        every { getActivity(activity.id, activity.stravaId) } returns noWorkoutActivity
        coEvery { getCourses() } returns Result.success(listOf(course))
        coEvery {
            updateActivity(noWorkoutActivity, "Updated", null, null, null, null, null, null, null, null, false)
        } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(EditState(status = null, activity = noWorkoutActivity, eventTypes = eventTypes, name = "Updated"))
            assertThat(awaitItem().name).isEqualTo("Updated")
            viewModel.onAction(EditAction.Save)

            assertThat(awaitItem().status).isEqualTo(Status.Loading)
            result.complete(Result.success(Unit))
            assertThat(awaitItem().status).isEqualTo(Status.Success)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
        coVerify { updateActivity(noWorkoutActivity, "Updated", null, null, null, null, null, null, null, null, false) }
    }

    @Test
    fun `Save - workout unavailable`() = runTest {
        val result = CompletableDeferred<Result<Unit>>()
        every { getActivity(activity.id, activity.stravaId) } returns activity
        coEvery { getCourses() } returns Result.success(listOf(course))
        coEvery { getWorkout(8) } returns Result.failure(IllegalStateException("workout unavailable"))
        coEvery {
            updateActivity(activity, "Updated", null, null, null, null, null, null, null, null, false)
        } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(EditState(status = null, activity = activity, eventTypes = eventTypes, name = "Updated"))
            assertThat(awaitItem().name).isEqualTo("Updated")
            viewModel.onAction(EditAction.Save)

            assertThat(awaitItem().status).isEqualTo(Status.Loading)
            result.complete(Result.success(Unit))
            assertThat(awaitItem().status).isEqualTo(Status.Success)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        coVerify { getCourses() }
        coVerify { getWorkout(8) }
        coVerify { updateActivity(activity, "Updated", null, null, null, null, null, null, null, null, false) }
    }

    @Test
    fun `Save - missing activity`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns null
        coEvery { getCourses() } returns Result.success(listOf(course))

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem().activity).isEqualTo(Activity.EMPTY)
            viewModel.onAction(EditAction.Save)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivity(activity.id, activity.stravaId) }
        verify { getEventTypes() }
        verify { getGears() }
        verify { notification.show("Activity not found") }
        coVerify { getCourses() }
    }
}
