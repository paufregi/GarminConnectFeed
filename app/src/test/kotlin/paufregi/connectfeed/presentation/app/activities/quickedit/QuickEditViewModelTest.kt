package paufregi.connectfeed.presentation.app.activities.quickedit

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
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
import paufregi.connectfeed.presentation.ui.components.notification.NotificationManager
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
    private val gear = Gear("shoe-1", "Trainers", GearType.Shoe)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun stubInitialData() {
        every { getActivity(activity.id, activity.stravaId) } returns activity
        every { getProfiles() } returns flowOf(listOf(profile))
        every { getGears() } returns flowOf(listOf(gear))
    }

    private fun createViewModel() =
        QuickEditViewModel(
            getActivity, getProfiles, getGears, getWorkout, updateActivity,
            notification, activity.id, activity.stravaId
        )

    @Test
    fun `Loads activity profiles and gears`() = runTest {
        stubInitialData()
        val viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.activity).isEqualTo(activity)
            assertThat(state.profiles).containsExactly(profile)
            assertThat(state.gears).containsExactly(gear)
            assertThat(state.availableProfiles).containsExactly(profile)
            assertThat(state.availableGears).containsExactly(gear)
            assertThat(state.canSave).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Missing activity reports error and navigates back`() = runTest {
        every { getActivity(activity.id, activity.stravaId) } returns null
        every { getProfiles() } returns flowOf(emptyList())
        every { getGears() } returns flowOf(emptyList())

        val viewModel = createViewModel()

        verify { notification.show("Activity not found") }
        viewModel.effects.test {
            assertThat(awaitItem()).isEqualTo(QuickEditEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Actions set fields and clear nonpositive effort`() = runTest {
        stubInitialData()
        val viewModel = createViewModel()

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(QuickEditAction.SetProfile(profile))
            viewModel.onAction(QuickEditAction.SetGear(gear))
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            viewModel.onAction(QuickEditAction.SetWater(400))
            viewModel.onAction(QuickEditAction.SetEffort(0f))
            viewModel.onAction(QuickEditAction.SetFeel(4f))
            runCurrent()
            val updated = viewModel.state.value
            assertThat(updated.profile).isEqualTo(profile)
            assertThat(updated.gear).isEqualTo(gear)
            assertThat(updated.description).isEqualTo("description")
            assertThat(updated.water).isEqualTo(400)
            assertThat(updated.effort).isNull()
            assertThat(updated.feel).isEqualTo(4f)
            assertThat(updated.canSave).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Save success uses workout and sets success status`() = runTest {
        stubInitialData()
        val workout = Workout(8, "Intervals")
        coEvery { getWorkout(8) } returns Result.success(workout)
        coEvery {
            updateActivity(activity, profile, "description", 400, 4f, 2f, workout, gear)
        } returns Result.success(Unit)

        val viewModel = createViewModel()

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(QuickEditAction.SetProfile(profile))
            viewModel.onAction(QuickEditAction.SetGear(gear))
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            viewModel.onAction(QuickEditAction.SetWater(400))
            viewModel.onAction(QuickEditAction.SetFeel(4f))
            viewModel.onAction(QuickEditAction.SetEffort(2f))
            viewModel.onAction(QuickEditAction.Save)
            runCurrent()
            assertThat(viewModel.state.value.status).isEqualTo(Status.Success)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getWorkout(8) }
        coVerify {
            updateActivity(activity, profile, "description", 400, 4f, 2f, workout, gear)
        }
    }

    @Test
    fun `Save failure reports failure status`() = runTest {
        stubInitialData()
        coEvery { getWorkout(8) } returns Result.failure(IllegalStateException("workout unavailable"))
        coEvery { updateActivity(activity, null, null, null, null, null, null, null) } returns
            Result.failure(IllegalStateException("save failed"))

        val viewModel = createViewModel()

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(QuickEditAction.Save)
            runCurrent()
            assertThat(viewModel.state.value.status).isEqualTo(Status.Failure("save failed"))
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { updateActivity(activity, null, null, null, null, null, null, null) }
        coVerify { getWorkout(8) }
    }
}
