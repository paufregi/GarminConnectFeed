package paufregi.connectfeed.presentation.profile

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
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    private val getProfile = mockk<GetProfile>()
    private val getActivityTypesForProfile = mockk<GetActivityTypesForProfile>()
    private val getEventTypes = mockk<GetEventTypes>()
    private val getCourses = mockk<GetCourses>()
    private val saveProfile = mockk<SaveProfile>()

    private lateinit var viewModel: ProfileViewModel

    val profile = Profile(id = 1, name = "profile")
    val types = listOf(ActivityType.Any, ActivityType.Running)
    val eventTypes = listOf(EventType.Training, EventType.Recreation)
    val courses = listOf(Course(id = 1, name = "course", distance = 10234.00, type = ActivityType.Running))

    fun createViewModel(id: Long = 0L) = ProfileViewModel(id, getProfile, getActivityTypesForProfile, getEventTypes, getCourses, saveProfile)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @After
    fun tearDown(){
        confirmVerified(getProfile, getActivityTypesForProfile, getEventTypes, getCourses, saveProfile)
        clearAllMocks()
    }

    @Test
    fun `Initial state - existing profile`() = runTest {
        coEvery { getProfile(any()) } returns profile
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel(profile.id)

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
        coEvery { getProfile(any()) } returns profile
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel(1)

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
        coEvery { getProfile(any()) } returns profile
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel(1)

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
            getActivityTypesForProfile()
            getEventTypes()
        }
        coVerify {
            getProfile(0)
            getCourses()
        }
    }

    @Test
    fun `Set gear`() = runTest {
        coEvery { getProfile(any()) } returns null
        every { getActivityTypesForProfile() } returns types
        every { getEventTypes() } returns eventTypes
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            viewModel.onAction(ProfileAction.SetGear(true))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.profile).isEqualTo(Profile(gear = true))
            assertThat(state.types).isEqualTo(types)
            assertThat(state.eventTypes).isEqualTo(eventTypes)
            assertThat(state.courses).isEqualTo(courses)
            cancelAndIgnoreRemainingEvents()
        }

        verify{
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