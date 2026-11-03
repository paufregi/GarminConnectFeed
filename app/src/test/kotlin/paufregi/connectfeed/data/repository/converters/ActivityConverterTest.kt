package paufregi.connectfeed.data.repository.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.data.api.garmin.models.ActivityDto
import paufregi.connectfeed.data.api.garmin.models.ActivityTypeDto
import paufregi.connectfeed.data.api.garmin.models.EventTypeDto
import kotlin.time.Instant

class ActivityConverterTest {

    @Test
    fun `DTO to core`() {
        val dto = ActivityDto(
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
            trainingEffect = "TEMPO",
            date = Instant.fromEpochMilliseconds(1735693200000),
            workoutId = 2
        )

        assertThat(dto.toCore()).isEqualTo(activity)
    }
}