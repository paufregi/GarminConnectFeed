package paufregi.connectfeed.data.repository.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.data.api.garmin.models.EventTypeDto
import paufregi.connectfeed.core.models.EventType

class EventTypeConverterTest {

    @Test
    fun `DTO to core - race`() {
        val dto = EventTypeDto(id = 1, key = "race")
        assertThat(dto.toCore()).isEqualTo(EventType.Race)
    }

    @Test
    fun `DTO to core - recreation`() {
        val dto = EventTypeDto(id = 2, key = "recreation")
        assertThat(dto.toCore()).isEqualTo(EventType.Recreation)
    }

    @Test
    fun `DTO to core - special event`() {
        val dto = EventTypeDto(id = 3, key = "specialEvent")
        assertThat(dto.toCore()).isEqualTo(EventType.SpecialEvent)
    }

    @Test
    fun `DTO to core - training`() {
        val dto = EventTypeDto(id = 4, key = "training")
        assertThat(dto.toCore()).isEqualTo(EventType.Training)
    }

    @Test
    fun `DTO to core - transportation`() {
        val dto = EventTypeDto(id = 5, key = "transportation")
        assertThat(dto.toCore()).isEqualTo(EventType.Transportation)
    }

    @Test
    fun `DTO to core - touring`() {
        val dto = EventTypeDto(id = 6, key = "touring")
        assertThat(dto.toCore()).isEqualTo(EventType.Touring)
    }

    @Test
    fun `DTO to core - geocaching`() {
        val dto = EventTypeDto(id = 7, key = "geocaching")
        assertThat(dto.toCore()).isEqualTo(EventType.Geocaching)
    }

    @Test
    fun `DTO to core - fitness`() {
        val dto = EventTypeDto(id = 8, key = "fitness")
        assertThat(dto.toCore()).isEqualTo(EventType.Fitness)
    }

    @Test
    fun `DTO to core - uncategorized`() {
        val dto = EventTypeDto(id = 9, key = "uncategorized")
        assertThat(dto.toCore()).isEqualTo(EventType.Uncategorized)
    }

    @Test
    fun `DTO to core - unknown`() {
        val dto = EventTypeDto(id = 999, key = "unknown")
        assertThat(dto.toCore()).isEqualTo(EventType.Uncategorized)
    }
}