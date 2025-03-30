package paufregi.connectfeed.presentation.quickedit

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
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.usecases.GetLatestActivities
import paufregi.connectfeed.core.usecases.GetLatestStravaActivities
import paufregi.connectfeed.core.usecases.GetProfiles
import paufregi.connectfeed.core.usecases.QuickUpdateActivity
import paufregi.connectfeed.core.usecases.QuickUpdateStravaActivity
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import java.time.Instant

@ExperimentalCoroutinesApi
class QuickEditViewModelTest {

    private val getActivities = mockk<GetLatestActivities>()
    private val getStravaActivities = mockk<GetLatestStravaActivities>()
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
        Profile(name = "profile1", activityType = ActivityType.Running),
        Profile(name = "profile2" ,activityType = ActivityType.Cycling),
        Profile(name = "profile3", activityType = ActivityType.Running)
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
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(emptyList())
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

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
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Initial state - with Strava`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

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
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Fails to load activities`() = runTest {
        coEvery { getActivities() } returns Result.Failure("error")
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

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
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Fails to load strava activities`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Failure("error")
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

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
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Fails to load all activities`() = runTest {
        coEvery { getActivities() } returns Result.Failure("error")
        coEvery { getStravaActivities() } returns Result.Failure("error")
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

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
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Set profile`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.description).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
        }
        verify { getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Set activity`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
        }
        verify { getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Set activity - matching`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[2]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[2])
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.description).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Set activity - no matching`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetProfile(profiles[1]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[1]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Set Strava activity`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[0]))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
        }
        verify { getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Set Strava activity - matching`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetActivity(activities[2]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[0]))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[2])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.description).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Set Strava activity - no matching`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetProfile(profiles[1]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetActivity(activities[1]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[0]))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Set description`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.description).isEqualTo("description")
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Set water`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetWater(100))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isEqualTo(profiles[0].copy(water = 100))
            assertThat(state.description).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Set effort`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetEffort(50f))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.effort).isEqualTo(50f)
            assertThat(state.feel).isNull()
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Set feel`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.stravaActivity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.description).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isEqualTo(50f)
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Save activity`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)
        coEvery { quickUpdateActivity(any(), any(), any(), any()) } returns Result.Success(Unit)
        coEvery { quickUpdateStravaActivity(any(), any(), any(), any()) } returns Result.Success(Unit)

        viewModel = QuickEditViewModel(
            getActivities,
            getStravaActivities,
            getProfiles,
            quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetEffort(80f))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.Save)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Success("Activity updated"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.description).isEqualTo("description")
            assertThat(state.effort).isEqualTo(80f)
            assertThat(state.feel).isEqualTo(50f)
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
            quickUpdateActivity(activities[0], profiles[0], 50f, 80f)
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description")
        }
        verify { getProfiles() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getProfiles,
            quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Save activity - failure`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)
        coEvery { quickUpdateActivity(any(), any(), any(), any()) } returns Result.Failure("failure")
        coEvery {
            quickUpdateStravaActivity(
                any(),
                any(),
                any(),
                any()
            )
        } returns Result.Success(Unit)

        viewModel = QuickEditViewModel(
            getActivities,
            getStravaActivities,
            getProfiles,
            quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetEffort(80f))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.Save)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't update activity"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.description).isEqualTo("description")
            assertThat(state.effort).isEqualTo(80f)
            assertThat(state.feel).isEqualTo(50f)
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
            quickUpdateActivity(activities[0], profiles[0], 50f, 80f)
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description")
        }
        verify { getProfiles() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getProfiles,
            quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Save activity - strava failure`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)
        coEvery { quickUpdateActivity(any(), any(), any(), any()) } returns Result.Success(Unit)
        coEvery { quickUpdateStravaActivity(any(), any(), any(), any()) } returns Result.Failure("failure")

        viewModel = QuickEditViewModel(
            getActivities,
            getStravaActivities,
            getProfiles,
            quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetEffort(80f))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.Save)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't update Strava activity"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.description).isEqualTo("description")
            assertThat(state.effort).isEqualTo(80f)
            assertThat(state.feel).isEqualTo(50f)
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
            quickUpdateActivity(activities[0], profiles[0], 50f, 80f)
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description")
        }
        verify { getProfiles() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getProfiles,
            quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }

    @Test
    fun `Save activity - both failure`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)
        coEvery { quickUpdateActivity(any(), any(), any(), any()) } returns Result.Failure("failure")
        coEvery { quickUpdateStravaActivity(any(), any(), any(), any()) } returns Result.Failure("failure")

        viewModel = QuickEditViewModel(
            getActivities,
            getStravaActivities,
            getProfiles,
            quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetEffort(80f))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetDescription("description"))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.Save)
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't update activity & Strava activity"))
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.stravaActivities).isEqualTo(stravaActivities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.description).isEqualTo("description")
            assertThat(state.effort).isEqualTo(80f)
            assertThat(state.feel).isEqualTo(50f)
            assertThat(state.hasStrava).isTrue()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
            quickUpdateActivity(activities[0], profiles[0], 50f, 80f)
            quickUpdateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description")
        }
        verify { getProfiles() }
        confirmVerified(
            getActivities,
            getStravaActivities,
            getProfiles,
            quickUpdateActivity,
            this@QuickEditViewModelTest.quickUpdateStravaActivity
        )
    }
}