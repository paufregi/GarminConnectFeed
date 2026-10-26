package paufregi.connectfeed.data.api.garmin.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.EventType as CoreEventType

class EventTypeDtoTest {

    @Test
    fun `To Core event type - race`() {
        val eventTypeDto = EventTypeDto(id = 1, key = "race")
        assertThat(eventTypeDto.toCore()).isEqualTo(CoreEventType.Race)
    }

    @Test
    fun `To Core event type - recreation`() {
        val eventTypeDto = EventTypeDto(id = 2, key = "recreation")
        assertThat(eventTypeDto.toCore()).isEqualTo(CoreEventType.Recreation)
    }

    @Test
    fun `To Core event type - special event`() {
        val eventTypeDto = EventTypeDto(id = 3, key = "specialEvent")
        assertThat(eventTypeDto.toCore()).isEqualTo(CoreEventType.SpecialEvent)
    }

    @Test
    fun `To Core event type - training`() {
        val eventTypeDto = EventTypeDto(id = 4, key = "training")
        assertThat(eventTypeDto.toCore()).isEqualTo(CoreEventType.Training)
    }

    @Test
    fun `To Core event type - transportation`() {
        val eventTypeDto = EventTypeDto(id = 5, key = "transportation")
        assertThat(eventTypeDto.toCore()).isEqualTo(CoreEventType.Transportation)
    }

    @Test
    fun `To Core event type - touring`() {
        val eventTypeDto = EventTypeDto(id = 6, key = "touring")
        assertThat(eventTypeDto.toCore()).isEqualTo(CoreEventType.Touring)
    }

    @Test
    fun `To Core event type - geocaching`() {
        val eventTypeDto = EventTypeDto(id = 7, key = "geocaching")
        assertThat(eventTypeDto.toCore()).isEqualTo(CoreEventType.Geocaching)
    }

    @Test
    fun `To Core event type - fitness`() {
        val eventTypeDto = EventTypeDto(id = 8, key = "fitness")
        assertThat(eventTypeDto.toCore()).isEqualTo(CoreEventType.Fitness)
    }

    @Test
    fun `To Core event type - uncategorized`() {
        val eventTypeDto = EventTypeDto(id = 9, key = "uncategorized")
        assertThat(eventTypeDto.toCore()).isEqualTo(CoreEventType.Uncategorized)
    }

    @Test
    fun `To Core event type - unknown`() {
        val eventTypeDto = EventTypeDto(id = 999, key = "unknown")
        assertThat(eventTypeDto.toCore()).isEqualTo(CoreEventType.Uncategorized)
    }
}