package paufregi.connectfeed.data.api.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.Activity as CoreActivity
import paufregi.connectfeed.core.models.ActivityType as CoreActivityType

class ActivityTest {

    @Test
    fun `To Core activity`() {
        val activity = Activity(
            id = 1,
            name = "name",
            distance = 15007.59,
            type = ActivityType(id = 1, key = "running")
        )

        val coreActivity = CoreActivity(
            id = 1,
            name = "name",
            distance = 15008.00,
            type = CoreActivityType.Running
        )

        assertThat(activity.toCore()).isEqualTo(coreActivity)
    }
}
