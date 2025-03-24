package paufregi.connectfeed.data.api.garmin.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.EventType as CoreEventType

class EventTypeTest {

    @Test
    fun `To Core event type - race`() {
        val eventType = EventType(id = 1, key = "race")
        assertThat(eventType.toCore()).isEqualTo(CoreEventType.Race)
    }

    @Test
    fun `To Core event type - recreation`() {
        val eventType = EventType(id = 2, key = "recreation")
        assertThat(eventType.toCore()).isEqualTo(CoreEventType.Recreation)
    }

    @Test
    fun `To Core event type - special event`() {
        val eventType = EventType(id = 3, key = "specialEvent")
        assertThat(eventType.toCore()).isEqualTo(CoreEventType.SpecialEvent)
    }

    @Test
    fun `To Core event type - training`() {
        val eventType = EventType(id = 4, key = "training")
        assertThat(eventType.toCore()).isEqualTo(CoreEventType.Training)
    }

    @Test
    fun `To Core event type - transportation`() {
        val eventType = EventType(id = 5, key = "transportation")
        assertThat(eventType.toCore()).isEqualTo(CoreEventType.Transportation)
    }

    @Test
    fun `To Core event type - touring`() {
        val eventType = EventType(id = 6, key = "touring")
        assertThat(eventType.toCore()).isEqualTo(CoreEventType.Touring)
    }

    @Test
    fun `To Core event type - geocaching`() {
        val eventType = EventType(id = 7, key = "geocaching")
        assertThat(eventType.toCore()).isEqualTo(CoreEventType.Geocaching)
    }

    @Test
    fun `To Core event type - fitness`() {
        val eventType = EventType(id = 8, key = "fitness")
        assertThat(eventType.toCore()).isEqualTo(CoreEventType.Fitness)
    }

    @Test
    fun `To Core event type - uncategorized`() {
        val eventType = EventType(id = 9, key = "uncategorized")
        assertThat(eventType.toCore()).isEqualTo(CoreEventType.Uncategorized)
    }

    @Test
    fun `To Core event type - unknown`() {
        val eventType = EventType(id = 999, key = "unknown")
        assertThat(eventType.toCore()).isEqualTo(CoreEventType.Uncategorized)
    }
}