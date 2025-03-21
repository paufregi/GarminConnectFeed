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
import paufregi.connectfeed.core.usecases.UpdateActivity
import paufregi.connectfeed.core.usecases.UpdateStravaActivity
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule

@ExperimentalCoroutinesApi
class QuickEditViewModelTest {

    private val getActivities = mockk<GetLatestActivities>()
    private val getStravaActivities = mockk<GetLatestStravaActivities>()
    private val getProfiles = mockk<GetProfiles>()
    private val updateActivity = mockk<UpdateActivity>()
    private val updateStravaActivity = mockk<UpdateStravaActivity>()

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
            trainingEffect = "recovery"
        ),
        Activity(
            id = 2L,
            name = "Cycling",
            type = ActivityType.Cycling,
            eventType = EventType.Training,
            distance = 17803.00,
            trainingEffect = "base"
        )
    )

    val stravaActivities = listOf(
        Activity(
            id = 1L,
            name = "StravaRunning",
            type = ActivityType.Running,
            distance = 10234.00
        ),
        Activity(
            id = 2L,
            name = "StravaCycling",
            type = ActivityType.Cycling,
            distance = 17803.00
        )
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
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Fails to load activities`() = runTest {
        coEvery { getActivities() } returns Result.Failure("error")
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)

        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't load activities"))
            assertThat(state.activities).isEqualTo(emptyList<Activity>())
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Set profile`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
        }
        verify { getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Set activity`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.profile).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
        }
        verify { getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Set activity with matching profile`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.profile).isEqualTo(profiles[0])
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Set activity with no matching profile`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetProfile(profiles[1]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isEqualTo(activities[0])
            assertThat(state.profile).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Set water`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetProfile(profiles[0]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetWater(100))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.profile).isEqualTo(profiles[0].copy(water = 100))
            assertThat(state.effort).isNull()
            assertThat(state.feel).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Set effort`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetEffort(50f))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.effort).isEqualTo(50f)
            assertThat(state.feel).isNull()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Set feel`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetFeel(50f))
            val state = awaitItem()
            assertThat(state.process).isEqualTo(ProcessState.Idle)
            assertThat(state.activities).isEqualTo(activities)
            assertThat(state.profiles).isEqualTo(profiles)
            assertThat(state.activity).isNull()
            assertThat(state.profile).isNull()
            assertThat(state.effort).isNull()
            assertThat(state.feel).isEqualTo(50f)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify{
            getActivities()
            getStravaActivities()
        }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)
    }

    @Test
    fun `Save activity`() = runTest {
        coEvery { getActivities() } returns Result.Success(activities)
        coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
        every { getProfiles() } returns flowOf(profiles)
        coEvery { updateActivity(any(), any(), any(), any()) } returns Result.Success(Unit)
        coEvery { updateStravaActivity(any(), any(), any(), any()) } returns Result.Success(Unit)

        viewModel = QuickEditViewModel(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)

        viewModel.state.test {
            awaitItem() // skip initial state
            viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
            awaitItem() // skip
            viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[0]))
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
            assertThat(state.effort).isEqualTo(80f)
            assertThat(state.feel).isEqualTo(50f)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify {
            getActivities()
            getStravaActivities()
            updateActivity(activities[0], profiles[0], 50f, 80f)
            updateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description") }
        verify{ getProfiles() }
        confirmVerified(getActivities, getStravaActivities, getProfiles, updateActivity, updateStravaActivity)

        @Test
        fun `Save activity - garmin failure`() = runTest {
            coEvery { getActivities() } returns Result.Success(activities)
            coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
            every { getProfiles() } returns flowOf(profiles)
            coEvery { updateActivity(any(), any(), any(), any()) } returns Result.Failure("failure")
            coEvery {
                updateStravaActivity(
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
                updateActivity,
                updateStravaActivity
            )

            viewModel.state.test {
                awaitItem() // skip initial state
                viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
                awaitItem() // skip
                viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[0]))
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
                assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't update Garmin activity"))
                assertThat(state.activities).isEqualTo(activities)
                assertThat(state.stravaActivities).isEqualTo(stravaActivities)
                assertThat(state.profiles).isEqualTo(profiles)
                assertThat(state.activity).isEqualTo(activities[0])
                assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
                assertThat(state.profile).isEqualTo(profiles[0])
                assertThat(state.effort).isEqualTo(80f)
                assertThat(state.feel).isEqualTo(50f)
                cancelAndIgnoreRemainingEvents()
            }

            coVerify {
                getActivities()
                getStravaActivities()
                updateActivity(activities[0], profiles[0], 50f, 80f)
                updateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description")
            }
            verify { getProfiles() }
            confirmVerified(
                getActivities,
                getStravaActivities,
                getProfiles,
                updateActivity,
                updateStravaActivity
            )
        }

        @Test
        fun `Save activity - strava failure`() = runTest {
            coEvery { getActivities() } returns Result.Success(activities)
            coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
            every { getProfiles() } returns flowOf(profiles)
            coEvery { updateActivity(any(), any(), any(), any()) } returns Result.Success(Unit)
            coEvery { updateStravaActivity(any(), any(), any(), any()) } returns Result.Failure("failure")

            viewModel = QuickEditViewModel(
                getActivities,
                getStravaActivities,
                getProfiles,
                updateActivity,
                updateStravaActivity
            )

            viewModel.state.test {
                awaitItem() // skip initial state
                viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
                awaitItem() // skip
                viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[0]))
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
                assertThat(state.effort).isEqualTo(80f)
                assertThat(state.feel).isEqualTo(50f)
                cancelAndIgnoreRemainingEvents()
            }

            coVerify {
                getActivities()
                getStravaActivities()
                updateActivity(activities[0], profiles[0], 50f, 80f)
                updateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description")
            }
            verify { getProfiles() }
            confirmVerified(
                getActivities,
                getStravaActivities,
                getProfiles,
                updateActivity,
                updateStravaActivity
            )
        }

        @Test
        fun `Save activity - garmin & strava failure`() = runTest {
            coEvery { getActivities() } returns Result.Success(activities)
            coEvery { getStravaActivities() } returns Result.Success(stravaActivities)
            every { getProfiles() } returns flowOf(profiles)
            coEvery { updateActivity(any(), any(), any(), any()) } returns Result.Failure("failure")
            coEvery { updateStravaActivity(any(), any(), any(), any()) } returns Result.Failure("failure")

            viewModel = QuickEditViewModel(
                getActivities,
                getStravaActivities,
                getProfiles,
                updateActivity,
                updateStravaActivity
            )

            viewModel.state.test {
                awaitItem() // skip initial state
                viewModel.onAction(QuickEditAction.SetActivity(activities[0]))
                awaitItem() // skip
                viewModel.onAction(QuickEditAction.SetStravaActivity(stravaActivities[0]))
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
                assertThat(state.process).isEqualTo(ProcessState.Failure("Couldn't update Garmin & Strava activity"))
                assertThat(state.activities).isEqualTo(activities)
                assertThat(state.stravaActivities).isEqualTo(stravaActivities)
                assertThat(state.profiles).isEqualTo(profiles)
                assertThat(state.activity).isEqualTo(activities[0])
                assertThat(state.stravaActivity).isEqualTo(stravaActivities[0])
                assertThat(state.profile).isEqualTo(profiles[0])
                assertThat(state.effort).isEqualTo(80f)
                assertThat(state.feel).isEqualTo(50f)
                cancelAndIgnoreRemainingEvents()
            }

            coVerify {
                getActivities()
                getStravaActivities()
                updateActivity(activities[0], profiles[0], 50f, 80f)
                updateStravaActivity(activities[0], stravaActivities[0], profiles[0], "description")
            }
            verify { getProfiles() }
            confirmVerified(
                getActivities,
                getStravaActivities,
                getProfiles,
                updateActivity,
                updateStravaActivity
            )
        }
    }
}