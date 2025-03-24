package paufregi.connectfeed.presentation.quickedit

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType

@ExperimentalCoroutinesApi
class QuickEditStateTest {

    @Test
    fun `Has Strava`() = runTest {
        val state = QuickEditState(
            stravaActivities = listOf(
                Activity(
                    id = 1L,
                    name = "StravaRunning",
                    type = ActivityType.Running,
                    distance = 10234.00
                )
            )
        )

        assertThat(state.hasStrava).isTrue()
    }

    @Test
    fun `Has not Strava`() = runTest {
        val state = QuickEditState(stravaActivities = emptyList())

        assertThat(state.hasStrava).isFalse()
    }
}
