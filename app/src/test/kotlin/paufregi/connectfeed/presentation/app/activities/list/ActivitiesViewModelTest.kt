package paufregi.connectfeed.presentation.app.activities.list

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.usecases.GetActivities
import paufregi.connectfeed.presentation.ui.components.notification.NotificationManager
import paufregi.connectfeed.presentation.utils.MainDispatcherRule
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class ActivitiesViewModelTest {

    private val getActivities = mockk<GetActivities>()
    private val notification = mockk<NotificationManager>(relaxed = true)

    private val activities = listOf(
        Activity(1, "Run", ActivityType.Running, EventType.Training, date = LocalDateTime.MIN),
        Activity(2, "Ride", ActivityType.Cycling, EventType.Race, date = LocalDateTime.MIN),
    )

    private lateinit var viewModel: ActivitiesViewModel

    private fun createViewModel() = ActivitiesViewModel(getActivities, notification)

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @After
    fun tearDown() {
        confirmVerified(getActivities, notification)
        clearAllMocks()
    }

    @Test
    fun `Initial state`() = runTest {
        val result = CompletableDeferred<Result<List<Activity>>>()
        coEvery { getActivities() } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            val loading = awaitItem()
            assertThat(loading.loading).isTrue()
            assertThat(loading.activities).isEmpty()
            result.complete(Result.success(activities))
            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.activities).isEqualTo(activities)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { getActivities() }
    }

    @Test
    fun `Initial state - no activities`() = runTest {
        val result = CompletableDeferred<Result<List<Activity>>>()
        coEvery { getActivities() } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem().loading).isTrue()
            result.complete(Result.success(emptyList()))
            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.activities).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { getActivities() }
    }

    @Test
    fun `Initial state - fail to load activities`() = runTest {
        val result = CompletableDeferred<Result<List<Activity>>>()
        coEvery { getActivities() } coAnswers { result.await() }

        viewModel = createViewModel()

        viewModel.state.test {
            assertThat(awaitItem().loading).isTrue()
            result.complete(Result.failure(IllegalStateException("network error")))
            val state = awaitItem()
            assertThat(state.loading).isFalse()
            assertThat(state.activities).isEmpty()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { getActivities() }
        verify { notification.show("Failed to load activities: network error") }
    }
}
