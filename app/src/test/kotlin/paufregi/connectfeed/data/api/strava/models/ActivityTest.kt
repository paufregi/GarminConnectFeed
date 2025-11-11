package paufregi.connectfeed.data.api.strava.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant
import paufregi.connectfeed.core.models.Activity as CoreActivity
import paufregi.connectfeed.core.models.ActivityType as CoreActivityType

class ActivityTest {

    @Test
    fun `To Core activity`() {
        val activity = Activity(
            id = 1,
            name = "name",
            distance = 15007.59123,
            sportType = "Run",
            startDate = "2024-01-01T01:00:00Z",
        )

        val coreActivity = CoreActivity(
            id = 1,
            name = "name",
            distance = 15008.00,
            type = CoreActivityType.StravaRunning,
            date = Instant.parse("2024-01-01T01:00:00Z")
        )

        assertThat(activity.toCore()).isEqualTo(coreActivity)
    }
}