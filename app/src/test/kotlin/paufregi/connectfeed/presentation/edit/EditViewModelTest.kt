package paufregi.connectfeed.presentation.edit

import app.cash.turbine.test
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
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.core.usecases.GetCourses
import paufregi.connectfeed.core.usecases.GetEventTypes
import paufregi.connectfeed.core.usecases.GetLatestActivities
import paufregi.connectfeed.core.usecases.GetLatestStravaActivities
import paufregi.connectfeed.core.usecases.UpdateActivity
import paufregi.connectfeed.core.usecases.UpdateStravaActivity
import paufregi.connectfeed.presentation.quickedit.QuickEditAction
import paufregi.connectfeed.presentation.quickedit.QuickEditViewModel
import paufregi.connectfeed.presentation.ui.models.ProcessState
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import java.time.Instant
import kotlin.collections.get

@ExperimentalCoroutinesApi
class EditViewModelTest {

    private val getActivities = mockk<GetLatestActivities>()
    private val getStravaActivities = mockk<GetLatestStravaActivities>()
    private val getEventTypes = mockk<GetEventTypes>()
    private val getCourses = mockk<GetCourses>()
    private val updateActivity = mockk<UpdateActivity>()
    private val updateStravaActivity = mockk<UpdateStravaActivity>()

    private lateinit var viewModel: EditViewModel

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

    val eventTypes = listOf(
        EventType.Training,
        EventType.Recreation
    )

    @Before
    fun setup(){

    }

    @After
    fun tearDown(){
        clearAllMocks()
    }
}