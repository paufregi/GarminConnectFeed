package paufregi.connectfeed.presentation.app.profiles.edit

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
import paufregi.connectfeed.presentation.ui.components.notification.NotificationManager
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    private val getActivityTypes = mockk<GetActivityTypesForProfile>()
    private val getEventTypes = mockk<GetEventTypes>()
    private val getProfile = mockk<GetProfile>()
    private val getCourses = mockk<GetCourses>()
    private val saveProfile = mockk<SaveProfile>()
    private val notification = mockk<NotificationManager>(relaxed = true)

    private val activityTypes = listOf(ActivityType.Any, ActivityType.Running, ActivityType.Cycling)
    private val eventTypes = listOf(EventType.Training, EventType.Race)
    private val runningCourse = Course(1, "Park loop", 5.0, ActivityType.Running)
    private val cyclingCourse = Course(2, "City ride", 20.0, ActivityType.Cycling)
    private val courses = listOf(runningCourse, cyclingCourse)
    private val profile = Profile(
        id = 7,
        name = "Morning run",
        type = ActivityType.Running,
        eventType = EventType.Training,
        water = 250
    )

    private lateinit var viewModel: ProfileViewModel

    fun createViewModel(id: Long? = profile.id) =
        ProfileViewModel(getActivityTypes, getEventTypes, getProfile, getCourses, saveProfile, notification, id)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        every { getActivityTypes() } returns activityTypes
        every { getEventTypes() } returns eventTypes
    }

    @After
    fun tearDown() {
        confirmVerified(getActivityTypes, getEventTypes, getProfile, getCourses, saveProfile, notification)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.profile).isEqualTo(profile)
            assertThat(state.activityTypes).containsExactlyElementsIn(activityTypes).inOrder()
            assertThat(state.eventTypes).containsExactlyElementsIn(eventTypes).inOrder()
            assertThat(state.courses).containsExactlyElementsIn(courses).inOrder()
            assertThat(state.availableCourses).containsExactly(runningCourse)
            assertThat(state.isNew).isFalse()
            assertThat(state.canSave).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Initial state - new profile`() = runTest {
        coEvery { getProfile(null) } returns null
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel(null)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.profile).isEqualTo(Profile())
            assertThat(state.courses).containsExactlyElementsIn(courses).inOrder()
            assertThat(state.availableCourses).containsExactlyElementsIn(courses).inOrder()
            assertThat(state.isNew).isTrue()
            assertThat(state.canSave).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(null) }
        coVerify { getCourses() }
    }

    @Test
    fun `Initial state - missing profile`() = runTest {
        coEvery { getProfile(profile.id) } returns null
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.profile).isEqualTo(Profile())
            assertThat(state.isNew).isTrue()
            assertThat(state.canSave).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Initial state - fail to load courses`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.failure("error")

        viewModel = createViewModel()

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.profile).isEqualTo(profile)
            assertThat(state.courses).isEmpty()
            assertThat(state.availableCourses).isEmpty()
            assertThat(state.canSave).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Initial state - loading courses`() = runTest {
        val coursesResult = CompletableDeferred<Result<List<Course>>>()
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } coAnswers { coursesResult.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            val loading = awaitItem()
            assertThat(loading.loading).isTrue()
            assertThat(loading.profile).isEqualTo(profile)
            assertThat(loading.courses).isEmpty()

            coursesResult.complete(Result.success(courses))

            val state = expectMostRecentItem()
            assertThat(state.loading).isFalse()
            assertThat(state.courses).containsExactlyElementsIn(courses).inOrder()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set name`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetName("Evening run"))

            val state = awaitItem()
            assertThat(state.profile.name).isEqualTo("Evening run")
            assertThat(state.canSave).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set name - blank`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetName(" "))

            val state = awaitItem()
            assertThat(state.profile.name).isEqualTo(" ")
            assertThat(state.canSave).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set type - preserve compatible course`() = runTest {
        coEvery { getProfile(profile.id) } returns profile.copy(type = ActivityType.Any, course = runningCourse)
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetType(ActivityType.Running))

            val state = awaitItem()
            assertThat(state.profile.type).isEqualTo(ActivityType.Running)
            assertThat(state.profile.course).isEqualTo(runningCourse)
            assertThat(state.availableCourses).containsExactly(runningCourse)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set type - drop incompatible course`() = runTest {
        coEvery { getProfile(profile.id) } returns profile.copy(course = runningCourse)
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetType(ActivityType.Cycling))

            val state = awaitItem()
            assertThat(state.profile.type).isEqualTo(ActivityType.Cycling)
            assertThat(state.profile.course).isNull()
            assertThat(state.availableCourses).containsExactly(cyclingCourse)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set type - no course`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetType(ActivityType.Cycling))

            val state = awaitItem()
            assertThat(state.profile.type).isEqualTo(ActivityType.Cycling)
            assertThat(state.profile.course).isNull()
            assertThat(state.availableCourses).containsExactly(cyclingCourse)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set event type`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetEventType(EventType.Race))

            val state = awaitItem()
            assertThat(state.profile.eventType).isEqualTo(EventType.Race)
            assertThat(state.canSave).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set event type - null`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetEventType(null))

            val state = awaitItem()
            assertThat(state.profile.eventType).isNull()
            assertThat(state.canSave).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set course`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetCourse(runningCourse))

            val state = awaitItem()
            assertThat(state.profile.course).isEqualTo(runningCourse)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set course - null`() = runTest {
        coEvery { getProfile(profile.id) } returns profile.copy(course = runningCourse)
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetCourse(null))

            val state = awaitItem()
            assertThat(state.profile.course).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set water`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetWater("500"))

            val state = awaitItem()
            assertThat(state.profile.water).isEqualTo(500)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set water - strip non digits`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetWater("2a5"))

            val state = awaitItem()
            assertThat(state.profile.water).isEqualTo(25)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set water - invalid`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetWater("abc"))

            val state = awaitItem()
            assertThat(state.profile.water).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set water - overflow`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetWater("99999999999"))

            val state = awaitItem()
            assertThat(state.profile.water).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set water - empty`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetWater(""))

            val state = awaitItem()
            assertThat(state.profile.water).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set rename`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetRename(false))

            val state = awaitItem()
            assertThat(state.profile.rename).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set custom water`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetCustomWater(true))

            val state = awaitItem()
            assertThat(state.profile.customWater).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set gear`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetGear(true))

            val state = awaitItem()
            assertThat(state.profile.gear).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set feel and effort`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetFeelAndEffort(true))

            val state = awaitItem()
            assertThat(state.profile.feelAndEffort).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Set training effect`() = runTest {
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.onAction(ProfileAction.SetTrainingEffect(true))

            val state = awaitItem()
            assertThat(state.profile.trainingEffect).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
    }

    @Test
    fun `Save - success`() = runTest {
        val seeded = ProfileState(
            loading = true,
            profile = profile.copy(name = "Seeded"),
            activityTypes = activityTypes,
            eventTypes = eventTypes,
            courses = courses,
        )
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)
        coEvery { saveProfile(seeded.profile) } returns Result.success(Unit)

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(seeded)
            assertThat(awaitItem()).isEqualTo(seeded)
            viewModel.onAction(ProfileAction.Save)

            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.profile).isEqualTo(seeded.profile)
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.effects.test {
            assertThat(awaitItem()).isEqualTo(ProfileEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        verify { notification.show("Profile saved") }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
        coVerify { saveProfile(seeded.profile) }
    }

    @Test
    fun `Save - failure`() = runTest {
        val seeded = ProfileState(
            loading = true,
            profile = profile.copy(name = "Seeded"),
            activityTypes = activityTypes,
            eventTypes = eventTypes,
            courses = courses,
        )
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)
        coEvery { saveProfile(seeded.profile) } returns Result.failure("invalid")

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(seeded)
            assertThat(awaitItem()).isEqualTo(seeded)
            viewModel.onAction(ProfileAction.Save)

            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.profile).isEqualTo(seeded.profile)
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.effects.test {
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        verify { notification.show("invalid") }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
        coVerify { saveProfile(seeded.profile) }
    }

    @Test
    fun `Save - failure without message`() = runTest {
        val seeded = ProfileState(
            loading = true,
            profile = profile.copy(name = "Seeded"),
            activityTypes = activityTypes,
            eventTypes = eventTypes,
            courses = courses,
        )
        coEvery { getProfile(profile.id) } returns profile
        coEvery { getCourses() } returns Result.success(courses)
        coEvery { saveProfile(seeded.profile) } returns Result.failure(Exception())

        viewModel = createViewModel()

        viewModel.state.test {
            skipItems(1)
            viewModel.injectState(seeded)
            assertThat(awaitItem()).isEqualTo(seeded)
            viewModel.onAction(ProfileAction.Save)

            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.profile).isEqualTo(seeded.profile)
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.effects.test {
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }

        verify { getActivityTypes() }
        verify { getEventTypes() }
        verify { notification.show("Error") }
        coVerify { getProfile(profile.id) }
        coVerify { getCourses() }
        coVerify { saveProfile(seeded.profile) }
    }
}
