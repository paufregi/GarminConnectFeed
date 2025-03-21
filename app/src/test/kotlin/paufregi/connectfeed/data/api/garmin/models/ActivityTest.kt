package paufregi.connectfeed.data.api.garmin.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.Activity as CoreActivity
import paufregi.connectfeed.core.models.ActivityType as CoreActivityType
import paufregi.connectfeed.core.models.EventType as CoreEventType

class ActivityTest {

    @Test
    fun `To Core activity`() {
        val activity = Activity(
            id = 1,
            name = "name",
            distance = 15007.59,
            type = ActivityType(id = 1, key = "running"),
            eventType = EventType(id = 4, key = "training"),
            trainingEffectLabel = "TEMPO"
        )

        val coreActivity = CoreActivity(
            id = 1,
            name = "name",
            type = CoreActivityType.Running,
            eventType = CoreEventType.Training,
            distance = 15008.00,
            trainingEffect = "tempo"
        )

        assertThat(activity.toCore()).isEqualTo(coreActivity)
    }
}
