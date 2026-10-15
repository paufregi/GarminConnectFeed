package paufregi.connectfeed.presentation.app.activities.list

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
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

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `Loads activities successfully`() = runTest {
        val activities = listOf(
            Activity(1, "Run", ActivityType.Running, EventType.Training, date = LocalDateTime.MIN)
        )
        val result = CompletableDeferred<Result<List<Activity>>>()
        coEvery { getActivities() } coAnswers { result.await() }

        val viewModel = ActivitiesViewModel(getActivities, notification)

        viewModel.state.test {
            assertThat(awaitItem().loading).isTrue()
            result.complete(Result.success(activities))
            val loaded = awaitItem()
            assertThat(loaded.activities).isEqualTo(activities)
            assertThat(loaded.loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { getActivities() }
        verify(exactly = 0) { notification.show(any()) }
    }

    @Test
    fun `Reports load failure and clears loading state`() = runTest {
        val result = CompletableDeferred<Result<List<Activity>>>()
        coEvery { getActivities() } coAnswers { result.await() }

        val viewModel = ActivitiesViewModel(getActivities, notification)

        viewModel.state.test {
            assertThat(awaitItem().loading).isTrue()
            result.complete(Result.failure(IllegalStateException("network error")))
            val failed = awaitItem()
            assertThat(failed.activities).isEmpty()
            assertThat(failed.loading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { getActivities() }
        verify { notification.show("Failed to load activities: network error") }
    }
}
