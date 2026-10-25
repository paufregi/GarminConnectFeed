package paufregi.connectfeed.presentation.app.activities.quickedit

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
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.models.Workout
import paufregi.connectfeed.core.usecases.GetActivity
import paufregi.connectfeed.core.usecases.GetGears
import paufregi.connectfeed.core.usecases.GetProfiles
import paufregi.connectfeed.core.usecases.GetWorkout
import paufregi.connectfeed.core.usecases.QuickUpdateActivity
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.presentation.ui.components.notification.NotificationManager
import paufregi.connectfeed.presentation.ui.models.Status
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class QuickEditViewModelTest {

    private val getActivity = mockk<GetActivity>()
    private val getProfiles = mockk<GetProfiles>()
    private val getGears = mockk<GetGears>()
    private val getWorkout = mockk<GetWorkout>()
    private val updateActivity = mockk<QuickUpdateActivity>()
    private val notification = mockk<NotificationManager>(relaxed = true)

    private val activity = Activity(
        1, "Morning run", ActivityType.Running, EventType.Training,
        date = LocalDateTime.MIN, workoutId = 8, stravaId = 9
    )
    private val profile = Profile(2, "Quick run", ActivityType.Running, EventType.Training)
    private val rideProfile = Profile(3, "Quick ride", ActivityType.Cycling, EventType.Training)
    private val gear = Gear("shoe-1", "Trainers", GearType.Shoe)
    private val bike = Gear("bike-1", "Road bike", GearType.Bike)
    private val workout = Workout(8, "Intervals")

    private lateinit var viewModel: QuickEditViewModel

    private fun createViewModel() =
        QuickEditViewModel(
            getActivity, getProfiles, getGears, getWorkout, updateActivity,
            notification, activity.id, activity.stravaId
        )

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        every { getProfiles() } returns flowOf(listOf(profile, rideProfile))
        every { getGears() } returns flowOf(listOf(gear, bike))
    }

    @After
    fun tearDown() {
        confirmVerified(getActivity, getProfiles, getGears, getWorkout, updateActivity, notification)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.status).isNull()
            assertThat(state.activity).isEqualTo(activity)
            assertThat(state.profiles).containsExactly(profile, rideProfile)
            assertThat(state.gears).containsExactly(gear, bike)
            assertThat(state.availableProfiles).containsExactly(profile)
            assertThat(state.availableGears).containsExactly(gear)
            assertThat(state.profile).isNull()
            assertThat(state.gear).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.canSave).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
    }

    @Test
    fun `Initial state - missing activity`() = runTest {
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.failure("not found")

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.status).isNull()
            assertThat(state.notFound).isTrue()
            assertThat(state.activity).isEqualTo(Activity.EMPTY)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
        verify { notification.show("Failed to load activity: not found") }
    }

    @Test
    fun `Initial state - loading activity`() = runTest {
        val result = CompletableDeferred<Result<Activity>>()
        coEvery { getActivity(activity.id, activity.stravaId) } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            val loading = awaitItem()
            assertThat(loading.status).isEqualTo(Status.Loading)
            assertThat(loading.notFound).isFalse()
            assertThat(loading.activity).isEqualTo(Activity.EMPTY)

            result.complete(Result.success(activity))

            val loaded = awaitItem()
            assertThat(loaded.status).isNull()
            assertThat(loaded.activity).isEqualTo(activity)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
    }

    @Test
    fun `Set profile`() = runTest {
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetProfile(profile))

            val state = awaitItem()
            assertThat(state.profile).isEqualTo(profile)
            assertThat(state.canSave).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
    }

    @Test
    fun `Set profile - incompatible`() = runTest {
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(QuickEditState(activity = activity, profile = profile))
            val seeded = awaitItem()
            assertThat(seeded.profile).isEqualTo(profile)
            assertThat(seeded.canSave).isTrue()

            viewModel.onAction(QuickEditAction.SetProfile(rideProfile))

            expectNoEvents()
            assertThat(viewModel.state.value).isEqualTo(seeded)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
    }

    @Test
    fun `Set profile - null`() = runTest {
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(QuickEditState(activity = activity, profile = profile))
            assertThat(awaitItem().canSave).isTrue()
            viewModel.onAction(QuickEditAction.SetProfile(null))

            val state = awaitItem()
            assertThat(state.profile).isNull()
            assertThat(state.canSave).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
    }

    @Test
    fun `Set gear`() = runTest {
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetGear(gear))

            assertThat(awaitItem().gear).isEqualTo(gear)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
    }

    @Test
    fun `Set gear - incompatible`() = runTest {
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(QuickEditState(activity = activity, gear = gear))
            val seeded = awaitItem()
            assertThat(seeded.gear).isEqualTo(gear)

            viewModel.onAction(QuickEditAction.SetGear(bike))

            expectNoEvents()
            assertThat(viewModel.state.value).isEqualTo(seeded)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
    }

    @Test
    fun `Set description`() = runTest {
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetDescription("description"))

            assertThat(awaitItem().description).isEqualTo("description")
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
    }

    @Test
    fun `Set water`() = runTest {
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetWater(400))

            assertThat(awaitItem().water).isEqualTo(400)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
    }

    @Test
    fun `Set effort`() = runTest {
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetEffort(2f))

            assertThat(awaitItem().effort).isEqualTo(2f)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
    }

    @Test
    fun `Set effort - zero`() = runTest {
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(QuickEditState(activity = activity, effort = 3f))
            assertThat(awaitItem().effort).isEqualTo(3f)
            viewModel.onAction(QuickEditAction.SetEffort(0f))

            assertThat(awaitItem().effort).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
    }

    @Test
    fun `Set effort - negative`() = runTest {
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(QuickEditState(activity = activity, effort = 3f))
            assertThat(awaitItem().effort).isEqualTo(3f)
            viewModel.onAction(QuickEditAction.SetEffort(-1f))

            assertThat(awaitItem().effort).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
    }

    @Test
    fun `Set effort - null`() = runTest {
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(QuickEditState(activity = activity, effort = 3f))
            assertThat(awaitItem().effort).isEqualTo(3f)
            viewModel.onAction(QuickEditAction.SetEffort(null))

            assertThat(awaitItem().effort).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
    }

    @Test
    fun `Set feel`() = runTest {
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.SetFeel(4f))

            assertThat(awaitItem().feel).isEqualTo(4f)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
    }

    @Test
    fun `Reset status`() = runTest {
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(QuickEditState(status = Status.Failure("error"), activity = activity))
            assertThat(awaitItem().status).isEqualTo(Status.Failure("error"))
            viewModel.onAction(QuickEditAction.ResetStatus)

            assertThat(awaitItem().status).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
    }

    @Test
    fun `Save - success`() = runTest {
        val result = CompletableDeferred<Result<Unit>>()
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)
        coEvery { getWorkout(8) } returns Result.success(workout)
        coEvery {
            updateActivity(activity, profile, "description", 400, 4f, 2f, workout, gear)
        } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(
                QuickEditState(
                    status = null,
                    activity = activity,
                    profile = profile,
                    gear = gear,
                    description = "description",
                    water = 400,
                    effort = 2f,
                    feel = 4f,
                )
            )
            assertThat(awaitItem().profile).isEqualTo(profile)
            viewModel.onAction(QuickEditAction.Save)

            assertThat(awaitItem().status).isEqualTo(Status.Loading)
            result.complete(Result.success(Unit))
            assertThat(awaitItem().status).isEqualTo(Status.Success())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
        coVerify { getWorkout(8) }
        coVerify { updateActivity(activity, profile, "description", 400, 4f, 2f, workout, gear) }
    }

    @Test
    fun `Save - failure`() = runTest {
        val result = CompletableDeferred<Result<Unit>>()
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)
        coEvery { getWorkout(8) } returns Result.success(workout)
        coEvery {
            updateActivity(activity, profile, null, null, null, null, workout, null)
        } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(QuickEditState(status = null, activity = activity, profile = profile))
            assertThat(awaitItem().profile).isEqualTo(profile)
            viewModel.onAction(QuickEditAction.Save)

            assertThat(awaitItem().status).isEqualTo(Status.Loading)
            result.complete(Result.failure(IllegalStateException("save failed")))
            assertThat(awaitItem().status).isEqualTo(Status.Failure("save failed"))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
        coVerify { getWorkout(8) }
        coVerify { updateActivity(activity, profile, null, null, null, null, workout, null) }
    }

    @Test
    fun `Save - failure without message`() = runTest {
        val result = CompletableDeferred<Result<Unit>>()
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)
        coEvery { getWorkout(8) } returns Result.success(workout)
        coEvery {
            updateActivity(activity, profile, null, null, null, null, workout, null)
        } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(QuickEditState(status = null, activity = activity, profile = profile))
            assertThat(awaitItem().profile).isEqualTo(profile)
            viewModel.onAction(QuickEditAction.Save)

            assertThat(awaitItem().status).isEqualTo(Status.Loading)
            result.complete(Result.failure(Exception()))
            assertThat(awaitItem().status).isEqualTo(Status.Failure("Something went wrong: unknown error"))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
        coVerify { getWorkout(8) }
        coVerify { updateActivity(activity, profile, null, null, null, null, workout, null) }
    }

    @Test
    fun `Save - no profile`() = runTest {
        val result = CompletableDeferred<Result<Unit>>()
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)
        coEvery { getWorkout(8) } returns Result.success(workout)
        coEvery {
            updateActivity(activity, null, null, null, null, null, workout, null)
        } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(QuickEditAction.Save)

            assertThat(awaitItem().status).isEqualTo(Status.Loading)
            result.complete(Result.failure(IllegalStateException("Validation error")))
            assertThat(awaitItem().status).isEqualTo(Status.Failure("Validation error"))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
        coVerify { getWorkout(8) }
        coVerify { updateActivity(activity, null, null, null, null, null, workout, null) }
    }

    @Test
    fun `Save - no workout`() = runTest {
        val noWorkoutActivity = activity.copy(workoutId = null)
        val result = CompletableDeferred<Result<Unit>>()
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(noWorkoutActivity)
        coEvery {
            updateActivity(noWorkoutActivity, profile, null, null, null, null, null, null)
        } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(QuickEditState(status = null, activity = noWorkoutActivity, profile = profile))
            assertThat(awaitItem().profile).isEqualTo(profile)
            viewModel.onAction(QuickEditAction.Save)

            assertThat(awaitItem().status).isEqualTo(Status.Loading)
            result.complete(Result.success(Unit))
            assertThat(awaitItem().status).isEqualTo(Status.Success())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
        coVerify { updateActivity(noWorkoutActivity, profile, null, null, null, null, null, null) }
    }

    @Test
    fun `Save - workout unavailable`() = runTest {
        val result = CompletableDeferred<Result<Unit>>()
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.success(activity)
        coEvery { getWorkout(8) } returns Result.failure(IllegalStateException("workout unavailable"))
        coEvery {
            updateActivity(activity, profile, null, null, null, null, null, null)
        } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(QuickEditState(status = null, activity = activity, profile = profile))
            assertThat(awaitItem().profile).isEqualTo(profile)
            viewModel.onAction(QuickEditAction.Save)

            assertThat(awaitItem().status).isEqualTo(Status.Loading)
            result.complete(Result.success(Unit))
            assertThat(awaitItem().status).isEqualTo(Status.Success())
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
        coVerify { getWorkout(8) }
        coVerify { updateActivity(activity, profile, null, null, null, null, null, null) }
    }

    @Test
    fun `Save - missing activity`() = runTest {
        coEvery { getActivity(activity.id, activity.stravaId) } returns Result.failure("not found")

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem().notFound).isTrue()
            viewModel.onAction(QuickEditAction.Save)

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getActivity(activity.id, activity.stravaId) }
        verify { getProfiles() }
        verify { getGears() }
        verify { notification.show("Failed to load activity: not found") }
        coVerify(exactly = 0) { updateActivity(any(), any(), any(), any(), any(), any(), any(), any()) }
    }
}
