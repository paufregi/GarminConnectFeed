package paufregi.connectfeed.data.repository.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.data.api.garmin.models.ActivityTypeDto
import paufregi.connectfeed.data.api.garmin.models.EventTypeDto
import kotlin.time.Instant
import paufregi.connectfeed.data.api.garmin.models.ActivityDto as GarminActivity
import paufregi.connectfeed.data.api.strava.models.ActivityDto as StravaActivity

class ActivityConverterTest {

    @Test
    fun `GarminDTO to core`() {
        val dto = GarminActivity(
            id = 1,
            name = "name",
            distance = 15007.59,
            type = ActivityTypeDto(id = 1, key = "running"),
            eventType = EventTypeDto(id = 4, key = "training"),
            trainingEffectLabel = "TEMPO",
            beginTimestamp = 1735693200000,
            workoutId = 2
        )

        val activity = Activity(
            id = 1,
            name = "name",
            type = ActivityType.Running,
            eventType = EventType.Training,
            distance = 15008.00,
            trainingEffect = "tempo",
            date = Instant.fromEpochMilliseconds(1735693200000),
            workoutId = 2
        )

        assertThat(dto.toCore()).isEqualTo(activity)
    }

    @Test
    fun `StravaDTO to core`() {
        val activityDto = StravaActivity(
            id = 1,
            name = "name",
            distance = 15007.59123,
            sportType = "Run",
            startDate = "2024-01-01T01:00:00Z",
        )

        val coreActivity = Activity(
            id = 1,
            name = "name",
            distance = 15008.00,
            type = ActivityType.StravaRunning,
            date = Instant.parse("2024-01-01T01:00:00Z")
        )

        assertThat(activityDto.toCore()).isEqualTo(coreActivity)
    }
}