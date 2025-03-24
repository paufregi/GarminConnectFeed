package paufregi.connectfeed.core.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant

class ActivityTest {

    @Test
    fun `Match activity`() {
        val activity1 = Activity(
            id = 1,
            name = "Activity 1",
            distance = 17804.00,
            trainingEffect = "recovery",
            type = ActivityType.Cycling,
            eventType = EventType.Transportation,
            date = Instant.ofEpochMilli(1729754100000)
        )

        val activity2 = Activity(
            id = 2,
            name = "Activity 2",
            distance = 7804.00,
            trainingEffect = "recovery",
            type = ActivityType.Cycling,
            date = Instant.ofEpochMilli(1729754100000)
        )

        assertThat(activity1.match(activity2)).isTrue()
    }

    @Test
    fun `No match activity`() {
        val activity1 = Activity(
            id = 1,
            name = "Activity 1",
            distance = 17804.00,
            trainingEffect = "recovery",
            type = ActivityType.Cycling,
            eventType = EventType.Transportation,
            date = Instant.ofEpochMilli(1729754100000)
        )

        val activity2 = Activity(
            id = 2,
            name = "Activity 2",
            distance = 7804.00,
            trainingEffect = "recovery",
            type = ActivityType.Cycling,
            date = Instant.ofEpochMilli(1729754500000)
        )

        assertThat(activity1.match(activity2)).isFalse()
    }
}