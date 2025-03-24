package paufregi.connectfeed.data.database.coverters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.core.models.EventType

class EventTypeConverterTest {

    private val converter = EventTypeConverter()

    @Test
    fun `To Event type - race`() {
        val id = converter.toId(EventType.Race)
        assertThat(id).isEqualTo(EventType.Race.id)

        val event = converter.fromId(id)
        assertThat(event).isEqualTo(EventType.Race)
    }

    @Test
    fun `To Event type - recreation`() {
        val id = converter.toId(EventType.Recreation)
        assertThat(id).isEqualTo(EventType.Recreation.id)

        val event = converter.fromId(id)
        assertThat(event).isEqualTo(EventType.Recreation)
    }

    @Test
    fun `To Event type - special event`() {
        val id = converter.toId(EventType.SpecialEvent)
        assertThat(id).isEqualTo(EventType.SpecialEvent.id)

        val event = converter.fromId(id)
        assertThat(event).isEqualTo(EventType.SpecialEvent)
    }

    @Test
    fun `To Event type - training`() {
        val id = converter.toId(EventType.Training)
        assertThat(id).isEqualTo(EventType.Training.id)

        val event = converter.fromId(id)
        assertThat(event).isEqualTo(EventType.Training)
    }

    @Test
    fun `To Event type - transportation`() {
        val id = converter.toId(EventType.Transportation)
        assertThat(id).isEqualTo(EventType.Transportation.id)

        val event = converter.fromId(id)
        assertThat(event).isEqualTo(EventType.Transportation)
    }

    @Test
    fun `To Event type - touring`() {
        val id = converter.toId(EventType.Touring)
        assertThat(id).isEqualTo(EventType.Touring.id)

        val event = converter.fromId(id)
        assertThat(event).isEqualTo(EventType.Touring)
    }

    @Test
    fun `To Event type - geocaching`() {
        val id = converter.toId(EventType.Geocaching)
        assertThat(id).isEqualTo(EventType.Geocaching.id)

        val event = converter.fromId(id)
        assertThat(event).isEqualTo(EventType.Geocaching)
    }

    @Test
    fun `To Event type - fitness`() {
        val id = converter.toId(EventType.Fitness)
        assertThat(id).isEqualTo(EventType.Fitness.id)

        val event = converter.fromId(id)
        assertThat(event).isEqualTo(EventType.Fitness)
    }

    @Test
    fun `To Event type - uncategorized`() {
        val id = converter.toId(EventType.Uncategorized)
        assertThat(id).isEqualTo(EventType.Uncategorized.id)

        val event = converter.fromId(id)
        assertThat(event).isEqualTo(EventType.Uncategorized)
    }

    @Test
    fun `To Event type - null`() {
        val id = converter.toId(null)
        assertThat(id).isNull()

        val event = converter.fromId(null)
        assertThat(event).isNull()
    }

    @Test
    fun `To Event type - unknown`() {
        val event = converter.fromId(1000)
        assertThat(event).isNull()
    }
}