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
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.usecases.GetActivityTypes
import paufregi.connectfeed.core.usecases.GetCourses
import paufregi.connectfeed.core.usecases.GetEventTypes
import paufregi.connectfeed.core.usecases.GetProfile
import paufregi.connectfeed.core.usecases.SaveProfile
import paufregi.connectfeed.presentation.Route
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    private val getProfile = mockk<GetProfile>()
    private val getActivityTypes = mockk<GetActivityTypes>()
    private val getEventTypes = mockk<GetEventTypes>()
    private val getCourses = mockk<GetCourses>()
    private val saveProfile = mockk<SaveProfile>()
    private val savedState = mockk<SavedStateHandle>()

    private lateinit var viewModel: ProfileViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup(){
        mockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Initial state - existing profile`() = runTest {
        val profile = Profile(id = 1, name = "profile")
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val eventTypes = listOf(EventType(id = 1, name = "event"))
        val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile(1)
        coEvery { getProfile(any()) } returns profile
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Success(eventTypes)
        coEvery { getCourses() } returns Result.Success(courses)

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(profile)
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(1)
            getEventTypes()
            getCourses()
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Initial state - new profile`() = runTest {
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val eventTypes = listOf(EventType(id = 1, name = "event"))
        val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Success(eventTypes)
        coEvery { getCourses() } returns Result.Success(courses)

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile())
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(0)
            getEventTypes()
            getCourses()
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Fails to load event types`() = runTest {
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Failure<List<EventType>>("error")
        coEvery { getCourses() } returns Result.Success(courses)

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load event types"))
            assertThat(state.profile).isEqualTo(Profile())
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(emptyList<EventType>())
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(0)
            getEventTypes()
            getCourses()
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Fails to load courses`() = runTest {
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val eventTypes = listOf(EventType(id = 1, name = "event"))

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Success(eventTypes)
        coEvery { getCourses() } returns Result.Failure<List<Course>>("error")

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load courses"))
            assertThat(state.profile).isEqualTo(Profile())
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(emptyList<Course>())
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(0)
            getEventTypes()
            getCourses()
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Fails to load event types and courses`() = runTest {
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Failure<List<EventType>>("error")
        coEvery { getCourses() } returns Result.Failure<List<Course>>("error")

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load event types & courses"))
            assertThat(state.profile).isEqualTo(Profile())
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(emptyList<EventType>())
            assertThat(state.courses).isEqualTo(emptyList<Course>())
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(0)
            getEventTypes()
            getCourses()
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Set name`() = runTest {
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val eventTypes = listOf(EventType(id = 1, name = "event"))
        val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Success(eventTypes)
        coEvery { getCourses() } returns Result.Success(courses)

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(ProfileAction.SetName("name"))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(name = "name"))
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(0)
            getEventTypes()
            getCourses()
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Set activity type`() = runTest {
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val eventTypes = listOf(EventType(id = 1, name = "event"))
        val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Success(eventTypes)
        coEvery { getCourses() } returns Result.Success(courses)

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(ProfileAction.SetActivityType(ActivityType.Running))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(activityType = ActivityType.Running))
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(0)
            getEventTypes()
            getCourses()
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Set activity type - with course`() = runTest {
        val course = Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running)
        val profile = Profile(id = 1, name = "profile", course = course)
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val eventTypes = listOf(EventType(id = 1, name = "event"))
        val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile(1)
        coEvery { getProfile(any()) } returns profile
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Success(eventTypes)
        coEvery { getCourses() } returns Result.Success(courses)

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)

        val expectedProfile = profile.copy(activityType = ActivityType.Running)
        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(ProfileAction.SetActivityType(ActivityType.Running))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(expectedProfile)
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(1)
            getEventTypes()
            getCourses()
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Set activity type - unset course`() = runTest {
        val course = Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running)
        val profile = Profile(id = 1, name = "profile", course = course)
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val eventTypes = listOf(EventType(id = 1, name = "event"))
        val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile(1)
        coEvery { getProfile(any()) } returns profile
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Success(eventTypes)
        coEvery { getCourses() } returns Result.Success(courses)

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)

        val expectedProfile = profile.copy(activityType = ActivityType.Cycling, course = null)
        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(ProfileAction.SetActivityType(ActivityType.Cycling))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(expectedProfile)
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(1)
            getEventTypes()
            getCourses()
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Set event type`() = runTest {
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val eventTypes = listOf(EventType(id = 1, name = "event"))
        val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))
        val eventType = EventType(id = 1, name = "event")

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Success(eventTypes)
        coEvery { getCourses() } returns Result.Success(courses)

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(ProfileAction.SetEventType(eventType))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(eventType = eventType))
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(0)
            getEventTypes()
            getCourses()
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Set course`() = runTest {
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val eventTypes = listOf(EventType(id = 1, name = "event"))
        val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))
        val course = Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running)

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Success(eventTypes)
        coEvery { getCourses() } returns Result.Success(courses)

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(ProfileAction.SetCourse(course))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(course = course))
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(0)
            getEventTypes()
            getCourses()
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Set water`() = runTest {
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val eventTypes = listOf(EventType(id = 1, name = "event"))
        val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Success(eventTypes)
        coEvery { getCourses() } returns Result.Success(courses)

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(ProfileAction.SetWater(100))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(water = 100))
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(0)
            getEventTypes()
            getCourses()
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Set rename`() = runTest {
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val eventTypes = listOf(EventType(id = 1, name = "event"))
        val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Success(eventTypes)
        coEvery { getCourses() } returns Result.Success(courses)

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(ProfileAction.SetRename(false))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(rename = false))
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(0)
            getEventTypes()
            getCourses()
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Set custom water`() = runTest {
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val eventTypes = listOf(EventType(id = 1, name = "event"))
        val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Success(eventTypes)
        coEvery { getCourses() } returns Result.Success(courses)

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(ProfileAction.SetCustomWater(true))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(customWater = true))
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(0)
            getEventTypes()
            getCourses()
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Set feel and effort`() = runTest {
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val eventTypes = listOf(EventType(id = 1, name = "event"))
        val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Success(eventTypes)
        coEvery { getCourses() } returns Result.Success(courses)

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)


        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(ProfileAction.SetFeelAndEffort(true))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(feelAndEffort = true))
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(0)
            getEventTypes()
            getCourses()
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Set training effect`() = runTest {
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val eventTypes = listOf(EventType(id = 1, name = "event"))
        val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Success(eventTypes)
        coEvery { getCourses() } returns Result.Success(courses)

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(ProfileAction.SetTrainingEffect(true))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(trainingEffect = true))
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(0)
            getEventTypes()
            getCourses()
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Save profile`() = runTest {
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val eventTypes = listOf(EventType(id = 1, name = "event"))
        val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Success(eventTypes)
        coEvery { getCourses() } returns Result.Success(courses)
        coEvery { saveProfile(any()) } returns Result.Success(Unit)

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
        viewModel.onAction(ProfileAction.Save)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(ProfileAction.Save)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success("Profile saved"))
            assertThat(state.profile).isEqualTo(Profile())
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(0)
            getEventTypes()
            getCourses()
            saveProfile(Profile())
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }

    @Test
    fun `Save profile - failure`() = runTest {
        val activityTypes = listOf(ActivityType.Any, ActivityType.Running)
        val eventTypes = listOf(EventType(id = 1, name = "event"))
        val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))

        every { savedState.toRoute<Route.Profile>() } returns Route.Profile()
        coEvery { getProfile(any()) } returns null
        every { getActivityTypes() } returns activityTypes
        coEvery { getEventTypes() } returns Result.Success(eventTypes)
        coEvery { getCourses() } returns Result.Success(courses)
        coEvery { saveProfile(any()) } returns Result.Failure("error")

        viewModel = ProfileViewModel(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(ProfileAction.Save)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("error"))
            assertThat(state.profile).isEqualTo(Profile())
            assertThat(state.activityTypes).isEqualTo(activityTypes)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
            savedState.toRoute<Route.Profile>()
            getActivityTypes()
        }
        coVerify {
            getProfile(0)
            getEventTypes()
            getCourses()
            saveProfile(Profile())
        }
        confirmVerified(savedState, getProfile, getActivityTypes, getEventTypes, getCourses, saveProfile)
    }
}