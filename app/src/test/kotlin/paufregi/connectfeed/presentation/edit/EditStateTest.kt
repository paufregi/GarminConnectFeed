package paufregi.connectfeed.presentation.edit

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType

@ExperimentalCoroutinesApi
class EditStateTest {

    @Test
    fun `Has Strava`() = runTest {
        val state = EditState(
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
        val state = EditState(stravaActivities = emptyList())

        assertThat(state.hasStrava).isFalse()
    }
}
