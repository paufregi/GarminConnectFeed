package paufregi.connectfeed.presentation.profile

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
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
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.presentation.Route
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    private val getProfile = mockk<GetProfile>()
    private val getActivityTypesForProfile = mockk<GetActivityTypesForProfile>()
    private val getEventTypes = mockk<GetEventTypes>()
    private val getCourses = mockk<GetCourses>()
    private val saveProfile = mockk<SaveProfile>()
    private val savedState = mockk<SavedStateHandle>()

    private lateinit var viewModel: ProfileViewModel

    val profile = Profile(id = 1, name = "profile")
    val types = listOf(ActivityType.Any, ActivityType.Running)
    val eventTypes = listOf(EventType.Training, EventType.Recreation)
    val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))

    fun createViewModel() = ProfileViewModel(savedState, getProfile, getActivityTypesForProfile, getEventTypes, getCourses, saveProfile)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup(){
        mockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    @After
    fun tearDown(){
        confirmVerified(savedState, getProfile, getActivityTypesForProfile, getEventTypes, getCourses, saveProfile)
        clearAllMocks()
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    @Test
    fun `Initial state - existing profile`() = runTest {

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile(profile.id)
        coEvery { getProfile(any()) } returns profile
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(profile)
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(profile.id)
            getCourses()
        }
    }

    @Test
    fun `Initial state - new profile`() = runTest {
        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile())
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(0)
            getCourses()
        }
    }

    @Test
    fun `Fails to load courses`() = runTest {
        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.failure("error")

        viewModel = createViewModel()


        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load courses"))
            assertThat(state.profile).isEqualTo(Profile())
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(emptyList<Course>())
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(0)
            getCourses()
        }
    }

    @Test
    fun `Set name`() = runTest {
        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            viewModel.onAction(ProfileAction.SetName("name"))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(name = "name"))
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(0)
            getCourses()
        }
    }

    @Test
    fun `Set activity type`() = runTest {
        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            viewModel.onAction(ProfileAction.SetType(ActivityType.Running))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(type = ActivityType.Running))
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(0)
            getCourses()
        }
    }

    @Test
    fun `Set activity type - with course`() = runTest {
        every { savedState.toRoute<Route.Profile>() } returns Route.Profile(1)
        coEvery { getProfile(any()) } returns profile
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        val expectedProfile = profile.copy(type = ActivityType.Running)
        viewModel.state.test {
            viewModel.onAction(ProfileAction.SetType(ActivityType.Running))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(expectedProfile)
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(profile.id)
            getCourses()
        }
    }

    @Test
    fun `Set activity type - unset course`() = runTest {
        every { savedState.toRoute<Route.Profile>() } returns Route.Profile(1)
        coEvery { getProfile(any()) } returns profile
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        val expectedProfile = profile.copy(type = ActivityType.Cycling, course = null)
        viewModel.state.test {
            viewModel.onAction(ProfileAction.SetType(ActivityType.Cycling))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(expectedProfile)
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(1)
            getCourses()
        }
    }

    @Test
    fun `Set event type`() = runTest {
        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            viewModel.onAction(ProfileAction.SetEventType(EventType.Training))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(eventType = EventType.Training))
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(0)
            getCourses()
        }
    }

    @Test
    fun `Set course`() = runTest {
        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            viewModel.onAction(ProfileAction.SetType(ActivityType.Running))
            viewModel.onAction(ProfileAction.SetCourse(courses[0]))
            skipItems(2)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(type = ActivityType.Running, course = courses[0]))
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(0)
            getCourses()
        }
    }

    @Test
    fun `Set water`() = runTest {
        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            viewModel.onAction(ProfileAction.SetWater(100))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(water = 100))
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(0)
            getCourses()
        }
    }

    @Test
    fun `Set rename`() = runTest {
        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            viewModel.onAction(ProfileAction.SetRename(false))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(rename = false))
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(0)
            getCourses()
        }
    }

    @Test
    fun `Set custom water`() = runTest {
        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            viewModel.onAction(ProfileAction.SetCustomWater(true))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(customWater = true))
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(0)
            getCourses()
        }
    }

    @Test
    fun `Set feel and effort`() = runTest {
        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            viewModel.onAction(ProfileAction.SetFeelAndEffort(true))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(feelAndEffort = true))
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(0)
            getCourses()
        }
    }

    @Test
    fun `Set training effect`() = runTest {
        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            viewModel.onAction(ProfileAction.SetTrainingEffect(true))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(trainingEffect = true))
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(0)
            getCourses()
        }
    }

    @Test
    fun `Save profile`() = runTest {
        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)
        coEvery { saveProfile(any()) } returns Result.success(Unit)

        viewModel = createViewModel()

        viewModel.state.test {
            viewModel.onAction(ProfileAction.Save)
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success("Profile saved"))
            assertThat(state.profile).isEqualTo(Profile())
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(0)
            getCourses()
            saveProfile(Profile())
        }
    }

    @Test
    fun `Save profile - failure`() = runTest {
        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)
        coEvery { saveProfile(any()) } returns Result.failure("error")

        viewModel = createViewModel()

        viewModel.state.test {
            viewModel.onAction(ProfileAction.Save)
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("error"))
            assertThat(state.profile).isEqualTo(Profile())
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(0)
            getCourses()
            saveProfile(Profile())
        }
    }
}