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
            type = ActivityType.RoadBiking,
            eventType = EventType.Transportation,
            date = Instant.parse("2025-01-01T01:01:00Z")
        )

        val activity2 = Activity(
            id = 2,
            name = "Activity 2",
            distance = 7804.00,
            trainingEffect = "recovery",
            type = ActivityType.StravaRide,
            date = Instant.parse("2025-01-01T01:01:30Z")
        )

        assertThat(activity1.match(activity2)).isTrue()
    }

    @Test
    fun `No match - no timestamp`() {
        val activity1 = Activity(
            id = 1,
            name = "Activity 1",
            distance = 17804.00,
            trainingEffect = "recovery",
            type = ActivityType.RoadBiking,
            eventType = EventType.Transportation,
            date = null
        )

        val activity2 = Activity(
            id = 2,
            name = "Activity 2",
            distance = 7804.00,
            trainingEffect = "recovery",
            type = ActivityType.StravaRide,
            date = Instant.parse("2025-01-01T01:02:00Z")
        )

        assertThat(activity1.match(activity2)).isFalse()
    }

    @Test
    fun `No match - no timestamp in parameter`() {
        val activity1 = Activity(
            id = 1,
            name = "Activity 1",
            distance = 17804.00,
            trainingEffect = "recovery",
            type = ActivityType.RoadBiking,
            eventType = EventType.Transportation,
            date = Instant.parse("2025-01-01T01:01:00Z")
        )

        val activity2 = Activity(
            id = 2,
            name = "Activity 2",
            distance = 7804.00,
            trainingEffect = "recovery",
            type = ActivityType.StravaRide,
            date = null
        )

        assertThat(activity1.match(activity2)).isFalse()
    }

    @Test
    fun `No match - different type`() {
        val activity1 = Activity(
            id = 1,
            name = "Activity 1",
            distance = 7804.00,
            trainingEffect = "recovery",
            type = ActivityType.Running,
            eventType = EventType.Transportation,
            date = Instant.parse("2025-01-01T01:01:00Z")
        )

        val activity2 = Activity(
            id = 2,
            name = "Activity 2",
            distance = 17804.00,
            trainingEffect = "recovery",
            type = ActivityType.StravaRide,
            date = Instant.parse("2025-01-01T01:01:00Z")
        )

        assertThat(activity1.match(activity2)).isFalse()
    }

    @Test
    fun `No match - different timestamp`() {
        val activity1 = Activity(
            id = 1,
            name = "Activity 1",
            distance = 17804.00,
            trainingEffect = "recovery",
            type = ActivityType.RoadBiking,
            eventType = EventType.Transportation,
            date = Instant.parse("2025-01-01T01:01:00Z")
        )

        val activity2 = Activity(
            id = 2,
            name = "Activity 2",
            distance = 7804.00,
            trainingEffect = "recovery",
            type = ActivityType.StravaRide,
            date = Instant.parse("2025-01-01T01:02:30Z")
        )

        assertThat(activity1.match(activity2)).isFalse()
    }
}