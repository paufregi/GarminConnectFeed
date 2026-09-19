package paufregi.connectfeed.presentation.modify

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType

@ExperimentalCoroutinesApi
class ModifyStateTest {

    @Test
    fun `Has Strava`() = runTest {
        val state = ModifyState(
            stravaActivities = listOf(
                Activity(
                    id = 1L,
                    name = "StravaRunning",
                    type = ActivityType.Running,
                    distance = 10234.00,
                ),
            ),
        )

        assertThat(state.hasStrava).isTrue()
    }

    @Test
    fun `Can save in manual mode with selected activity`() = runTest {
        val state = ModifyState(
            activity = Activity(
                id = 1L,
                name = "Running",
                type = ActivityType.Running,
            ),
        )

        assertThat(state.canSave).isTrue()
    }

    @Test
    fun `Cannot save in profile mode without profile`() = runTest {
        val state = ModifyState(
            activity = Activity(
                id = 1L,
                name = "Running",
                type = ActivityType.Running,
            ),
            mode = ModifyMode.Profile,
        )

        assertThat(state.canSave).isFalse()
    }
}
