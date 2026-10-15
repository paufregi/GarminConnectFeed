package paufregi.connectfeed.presentation.app.profiles.edit

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
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Course
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.usecases.GetActivityTypesForProfile
import paufregi.connectfeed.core.usecases.GetCourses
import paufregi.connectfeed.core.usecases.GetEventTypes
import paufregi.connectfeed.core.usecases.GetProfile
import paufregi.connectfeed.core.usecases.SaveProfile
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    private val getActivityTypes = mockk<GetActivityTypesForProfile>()
    private val getEventTypes = mockk<GetEventTypes>()
    private val getProfile = mockk<GetProfile>()
    private val getCourses = mockk<GetCourses>()
    private val saveProfile = mockk<SaveProfile>()
    private val notification = mockk<paufregi.connectfeed.presentation.ui.components.notification.NotificationManager>(relaxed = true)

    private val existingProfile = Profile(
        id = 7,
        name = "Morning run",
        type = ActivityType.Running,
        eventType = EventType.Training,
        water = 250
    )
    private val runningCourse = Course(1, "Park loop", 5.0, ActivityType.Running)
    private val cyclingCourse = Course(2, "City ride", 20.0, ActivityType.Cycling)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun stubReferenceData() {
        every { getActivityTypes() } returns listOf(ActivityType.Any, ActivityType.Running, ActivityType.Cycling)
        every { getEventTypes() } returns listOf(EventType.Training, EventType.Race)
        coEvery { getCourses() } returns Result.success(listOf(runningCourse, cyclingCourse))
    }

    private fun createViewModel(id: Long? = existingProfile.id) =
        ProfileViewModel(
            getActivityTypes, getEventTypes, getProfile, getCourses,
            saveProfile, notification, id
        )

    @Test
    fun `Loads existing profile and compatible courses`() = runTest {
        stubReferenceData()
        coEvery { getProfile(existingProfile.id) } returns existingProfile

        val viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.profile).isEqualTo(existingProfile)
            assertThat(state.activityTypes).containsExactly(
                ActivityType.Any, ActivityType.Running, ActivityType.Cycling
            ).inOrder()
            assertThat(state.eventTypes).containsExactly(EventType.Training, EventType.Race).inOrder()
            assertThat(state.courses).containsExactly(runningCourse, cyclingCourse)
            assertThat(state.availableCourses).containsExactly(runningCourse)
            assertThat(state.loading).isFalse()
            assertThat(state.isNew).isFalse()
            assertThat(state.canSave).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `New profile defaults when no profile exists`() = runTest {
        stubReferenceData()
        coEvery { getProfile(null) } returns null

        val viewModel = createViewModel(null)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.profile).isEqualTo(Profile())
            assertThat(state.isNew).isTrue()
            assertThat(state.canSave).isFalse()
            assertThat(state.loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { getProfile(null) }
    }

    @Test
    fun `Actions update profile values and clear incompatible course`() = runTest {
        stubReferenceData()
        coEvery { getProfile(existingProfile.id) } returns existingProfile.copy(course = runningCourse)

        val viewModel = createViewModel()

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(ProfileAction.SetType(ActivityType.Cycling))
            viewModel.onAction(ProfileAction.SetName("Updated"))
            viewModel.onAction(ProfileAction.SetEventType(EventType.Race))
            viewModel.onAction(ProfileAction.SetCourse(cyclingCourse))
            viewModel.onAction(ProfileAction.SetWater("2a5"))
            viewModel.onAction(ProfileAction.SetRename(false))
            viewModel.onAction(ProfileAction.SetCustomWater(true))
            viewModel.onAction(ProfileAction.SetGear(true))
            viewModel.onAction(ProfileAction.SetFeelAndEffort(true))
            viewModel.onAction(ProfileAction.SetTrainingEffect(true))
            runCurrent()
            val state = viewModel.state.value
            assertThat(state.profile.name).isEqualTo("Updated")
            assertThat(state.profile.type).isEqualTo(ActivityType.Cycling)
            assertThat(state.profile.eventType).isEqualTo(EventType.Race)
            assertThat(state.profile.course).isEqualTo(cyclingCourse)
            assertThat(state.profile.water).isEqualTo(25)
            assertThat(state.profile.rename).isFalse()
            assertThat(state.profile.customWater).isTrue()
            assertThat(state.profile.gear).isTrue()
            assertThat(state.profile.feelAndEffort).isTrue()
            assertThat(state.profile.trainingEffect).isTrue()
            assertThat(state.availableCourses).containsExactly(cyclingCourse)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Invalid water input clears water`() = runTest {
        stubReferenceData()
        coEvery { getProfile(existingProfile.id) } returns existingProfile

        val viewModel = createViewModel()

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(ProfileAction.SetWater("abc"))
            runCurrent()
            assertThat(viewModel.state.value.profile.water).isNull()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Save success notifies and navigates back`() = runTest {
        stubReferenceData()
        coEvery { getProfile(existingProfile.id) } returns existingProfile
        coEvery { saveProfile(existingProfile) } returns Result.success(Unit)

        val viewModel = createViewModel()

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(ProfileAction.Save)
            verify { notification.show("Profile saved") }
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.effects.test {
            assertThat(awaitItem()).isEqualTo(ProfileEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { saveProfile(existingProfile) }
    }

    @Test
    fun `Save failure displays message and does not navigate`() = runTest {
        stubReferenceData()
        coEvery { getProfile(existingProfile.id) } returns existingProfile
        coEvery { saveProfile(existingProfile) } returns Result.failure(IllegalArgumentException("invalid"))

        val viewModel = createViewModel()

        viewModel.state.test {
            awaitItem()
            viewModel.onAction(ProfileAction.Save)
            verify { notification.show("invalid") }
            cancelAndIgnoreRemainingEvents()
        }
        viewModel.effects.test {
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { saveProfile(existingProfile) }
    }
}
