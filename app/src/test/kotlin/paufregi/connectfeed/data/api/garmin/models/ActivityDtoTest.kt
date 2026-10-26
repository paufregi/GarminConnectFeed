package paufregi.connectfeed.data.api.garmin.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.time.Instant
import paufregi.connectfeed.core.models.Activity as CoreActivity
import paufregi.connectfeed.core.models.ActivityType as CoreActivityType
import paufregi.connectfeed.core.models.EventType as CoreEventType

class ActivityDtoTest {

    @Test
    fun `To Core activity`() {
        val activityDto = ActivityDto(
            id = 1,
            name = "name",
            distance = 15007.59,
            type = ActivityTypeDto(id = 1, key = "running"),
            eventType = EventTypeDto(id = 4, key = "training"),
            trainingEffectLabel = "TEMPO",
            beginTimestamp = 1735693200000,
            workoutId = 2
        )

        val coreActivity = CoreActivity(
            id = 1,
            name = "name",
            type = CoreActivityType.Running,
            eventType = CoreEventType.Training,
            distance = 15008.00,
            trainingEffect = "tempo",
            date = Instant.fromEpochMilliseconds(1735693200000),
            workoutId = 2
        )

        assertThat(activityDto.toCore()).isEqualTo(coreActivity)
    }
}
