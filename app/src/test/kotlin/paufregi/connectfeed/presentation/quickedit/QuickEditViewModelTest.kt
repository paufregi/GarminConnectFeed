package paufregi.connectfeed.presentation.quickedit

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
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
import paufregi.connectfeed.core.models.Profile
import paufregi.connectfeed.core.usecases.GetActivities
import paufregi.connectfeed.core.usecases.GetProfiles
import paufregi.connectfeed.core.usecases.GetStravaActivities
import paufregi.connectfeed.core.usecases.QuickUpdateActivity
import paufregi.connectfeed.core.usecases.QuickUpdateStravaActivity
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import java.time.Instant

@ExperimentalCoroutinesApi
class QuickEditViewModelTest {

    private val getActivities = mockk<GetActivities>()
    private val getStravaActivities = mockk<GetStravaActivities>()
    private val getProfiles = mockk<GetProfiles>()
    private val quickUpdateActivity = mockk<QuickUpdateActivity>()
    private val quickUpdateStravaActivity = mockk<QuickUpdateStravaActivity>()

    private lateinit var viewModel: QuickEditViewModel

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

    val profiles = listOf(
        Profile(name = "profile1", type = ActivityType.Running),
        Profile(name = "profile2" ,type = ActivityType.Cycling),
        Profile(name = "profile3", type = ActivityType.Running)
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
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(emptyList())
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEmpty()
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Initial state - with Strava`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Fails to load activities`() = runTest {
        coEvery { getActivities(any()) } returns Result.failure("error")
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load activities"))
            assertThat(state.activities).isEmpty()
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Fails to load strava activities`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.failure("error")
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load Strava activities"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEmpty()
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Fails to load all activities`() = runTest {
        coEvery { getActivities(any()) } returns Result.failure("error")
        coEvery { getStravaActivities(any()) } returns Result.failure("error")
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load activities & Strava activities"))
            assertThat(state.activities).isEmpty()
            assertThat(state.stravaActivities).isEmpty()
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Set profile`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.description).isNull()
            assertThat(state.water).isEqualTo(profiles[0].water)
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Set activity`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Set Strava activity`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[0]))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Set activity & Strava activity - matching`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            viewModel.onAction(QuickEditAction.SetActivity(activities[2]))
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[0]))
            skipItems(3)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[2])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.description).isNull()
            assertThat(state.water).isEqualTo(profiles[0].water)
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Set activity & Strava activity - no matching`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.SetProfile(profiles[1]))
            viewModel.onAction(QuickEditAction.SetActivity(activities[1]))
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[0]))
            skipItems(3)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Set Strava activity & activity - matching`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[2]))
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            skipItems(3)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[2])
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.description).isNull()
            assertThat(state.water).isEqualTo(profiles[0].water)
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Set Strava activity & activity - no matching`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.SetProfile(profiles[1]))
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[1]))
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            skipItems(3)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Set description`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.description).isEqualTo("description")
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Set water`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            viewModel.onAction(QuickEditAction.SetWater(100))
            skipItems(2)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.description).isNull()
            assertThat(state.water).isEqualTo(100)
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Set effort`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.SetEffort(50f))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isEqualTo(50f)
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Set feel`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            skipItems(1)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isEqualTo(50f)
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities(false)
            getStravaActivities(false)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Save activity`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)
        coEvery { quickUpdateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { quickUpdateStravaActivity(any(), any(), any(), any()) } returns Result.success(Unit)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            viewModel.onAction(QuickEditAction.SetWater(25))
            viewModel.onAction(QuickEditAction.SetEffort(80f))
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            viewModel.onAction(QuickEditAction.Save)
            skipItems(8)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success("Activity updated"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.description).isEqualTo("description")
            assertThat(state.water).isEqualTo(25)
            assertThat(state.effort).isEqualTo(80f)
            assertThat(state.feel).isEqualTo(50f)
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            quickUpdateActivity(activities[0], profiles[0], 25, 50f, 80f)
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description")
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Save activity - failure`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)
        coEvery { quickUpdateActivity(any(), any(), any(), any(), any()) } returns Result.failure("failure")
        coEvery { quickUpdateStravaActivity(any(), any(), any(), any()) } returns Result.success(Unit)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            viewModel.onAction(QuickEditAction.SetWater(25))
            viewModel.onAction(QuickEditAction.SetEffort(80f))
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            viewModel.onAction(QuickEditAction.Save)
            skipItems(8)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't update Garmin activity"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.description).isEqualTo("description")
            assertThat(state.water).isEqualTo(25)
            assertThat(state.effort).isEqualTo(80f)
            assertThat(state.feel).isEqualTo(50f)
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            quickUpdateActivity(activities[0], profiles[0], 25, 50f, 80f)
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description")
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Save activity - strava failure`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)
        coEvery { quickUpdateActivity(any(), any(), any(), any(), any()) } returns Result.success(Unit)
        coEvery { quickUpdateStravaActivity(any(), any(), any(), any()) } returns Result.failure("failure")

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            viewModel.onAction(QuickEditAction.SetWater(25))
            viewModel.onAction(QuickEditAction.SetEffort(80f))
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            viewModel.onAction(QuickEditAction.Save)
            skipItems(8)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't update Strava activity"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.description).isEqualTo("description")
            assertThat(state.water).isEqualTo(25)
            assertThat(state.effort).isEqualTo(80f)
            assertThat(state.feel).isEqualTo(50f)
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            quickUpdateActivity(activities[0], profiles[0], 25, 50f, 80f)
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description")
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Save activity - both failure`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)
        coEvery { quickUpdateActivity(any(), any(), any(), any(), any()) } returns Result.failure("failure")
        coEvery { quickUpdateStravaActivity(any(), any(), any(), any()) } returns Result.failure("failure")

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            viewModel.onAction(QuickEditAction.SetWater(25))
            viewModel.onAction(QuickEditAction.SetEffort(80f))
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            viewModel.onAction(QuickEditAction.Save)
            skipItems(8)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't update Garmin & Strava activity"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.description).isEqualTo("description")
            assertThat(state.water).isEqualTo(25)
            assertThat(state.effort).isEqualTo(80f)
            assertThat(state.feel).isEqualTo(50f)
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getStravaActivities(false)
            quickUpdateActivity(activities[0], profiles[0], 25, 50f, 80f)
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description")
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }

    @Test
    fun `Restart action`() = runTest {
        coEvery { getActivities(any()) } returns Result.success(activities)
        coEvery { getStravaActivities(any()) } returns Result.success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)

        viewModel.state.test {
            viewModel.onAction(QuickEditAction.Restart)
            skipItems(2)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.water).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities(false)
            getActivities(true)
            getStravaActivities(false)
            getStravaActivities(true)
        }
        coVerify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity, quickUpdateStravaActivity)
    }
}