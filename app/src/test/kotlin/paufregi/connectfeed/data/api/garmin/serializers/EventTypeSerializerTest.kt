package paufregi.connectfeed.data.api.garmin.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import org.junit.Test
import paufregi.connectfeed.core.models.EventType

class EventTypeSerializerTest {

    private val json = Json { encodeDefaults = true }

    @Test
    fun `Serialize EventType Race to JSON`() {
        val res = json.encodeToString(EventTypeSerializer, EventType.Race)

        assertThat(res).isEqualTo("{\"typeId\":1,\"typeKey\":\"race\"}")
    }

    @Test
    fun `Serialize EventType Recreation to JSON`() {
        val res = json.encodeToString(EventTypeSerializer, EventType.Recreation)

        assertThat(res).isEqualTo("{\"typeId\":2,\"typeKey\":\"recreation\"}")
    }

    @Test
    fun `Serialize EventType SpecialEvent to JSON`() {
        val res = json.encodeToString(EventTypeSerializer, EventType.SpecialEvent)

        assertThat(res).isEqualTo("{\"typeId\":3,\"typeKey\":\"specialEvent\"}")
    }

    @Test
    fun `Serialize EventType Training to JSON`() {
        val res = json.encodeToString(EventTypeSerializer, EventType.Training)

        assertThat(res).isEqualTo("{\"typeId\":4,\"typeKey\":\"training\"}")
    }

    @Test
    fun `Serialize EventType Transportation to JSON`() {
        val res = json.encodeToString(EventTypeSerializer, EventType.Transportation)

        assertThat(res).isEqualTo("{\"typeId\":5,\"typeKey\":\"transportation\"}")
    }

    @Test
    fun `Serialize EventType Touring to JSON`() {
        val res = json.encodeToString(EventTypeSerializer, EventType.Touring)

        assertThat(res).isEqualTo("{\"typeId\":6,\"typeKey\":\"touring\"}")
    }

    @Test
    fun `Serialize EventType Geocaching to JSON`() {
        val res = json.encodeToString(EventTypeSerializer, EventType.Geocaching)

        assertThat(res).isEqualTo("{\"typeId\":7,\"typeKey\":\"geocaching\"}")
    }

    @Test
    fun `Serialize EventType Fitness to JSON`() {
        val res = json.encodeToString(EventTypeSerializer, EventType.Fitness)

        assertThat(res).isEqualTo("{\"typeId\":8,\"typeKey\":\"fitness\"}")
    }

    @Test
    fun `Serialize EventType Uncategorized to JSON`() {
        val res = json.encodeToString(EventTypeSerializer, EventType.Uncategorized)

        assertThat(res).isEqualTo("{\"typeId\":9,\"typeKey\":\"uncategorized\"}")
    }

    @Test
    fun `Deserialize JSON to EventType Race`() {
        val res = json.decodeFromString(EventTypeSerializer, "{\"typeId\":1,\"typeKey\":\"race\"}")

        assertThat(res).isEqualTo(EventType.Race)
    }

    @Test
    fun `Deserialize JSON to EventType Recreation`() {
        val res = json.decodeFromString(EventTypeSerializer, "{\"typeId\":2,\"typeKey\":\"recreation\"}")

        assertThat(res).isEqualTo(EventType.Recreation)
    }

    @Test
    fun `Deserialize JSON to EventType SpecialEvent`() {
        val res = json.decodeFromString(EventTypeSerializer, "{\"typeId\":3,\"typeKey\":\"specialEvent\"}")

        assertThat(res).isEqualTo(EventType.SpecialEvent)
    }

    @Test
    fun `Deserialize JSON to EventType Training`() {
        val res = json.decodeFromString(EventTypeSerializer, "{\"typeId\":4,\"typeKey\":\"training\"}")

        assertThat(res).isEqualTo(EventType.Training)
    }

    @Test
    fun `Deserialize JSON to EventType Transportation`() {
        val res = json.decodeFromString(EventTypeSerializer, "{\"typeId\":5,\"typeKey\":\"transportation\"}")

        assertThat(res).isEqualTo(EventType.Transportation)
    }

    @Test
    fun `Deserialize JSON to EventType Touring`() {
        val res = json.decodeFromString(EventTypeSerializer, "{\"typeId\":6,\"typeKey\":\"touring\"}")

        assertThat(res).isEqualTo(EventType.Touring)
    }

    @Test
    fun `Deserialize JSON to EventType Geocaching`() {
        val res = json.decodeFromString(EventTypeSerializer, "{\"typeId\":7,\"typeKey\":\"geocaching\"}")

        assertThat(res).isEqualTo(EventType.Geocaching)
    }

    @Test
    fun `Deserialize JSON to EventType Fitness`() {
        val res = json.decodeFromString(EventTypeSerializer, "{\"typeId\":8,\"typeKey\":\"fitness\"}")

        assertThat(res).isEqualTo(EventType.Fitness)
    }

    @Test
    fun `Deserialize JSON to EventType Uncategorized`() {
        val res = json.decodeFromString(EventTypeSerializer, "{\"typeId\":9,\"typeKey\":\"uncategorized\"}")

        assertThat(res).isEqualTo(EventType.Uncategorized)
    }

    @Test
    fun `Deserialize unknown JSON to EventType Uncategorized`() {
        val res = json.decodeFromString(EventTypeSerializer, "{\"typeId\":-1,\"typeKey\":\"unknown\"}")

        assertThat(res).isEqualTo(EventType.Uncategorized)
    }

    @Test
    fun `Deserialize unsupported JSON to EventType Uncategorized`() {
        val res = json.decodeFromString(EventTypeSerializer, "{\"typeId\":999,\"typeKey\":\"nope\"}")

        assertThat(res).isEqualTo(EventType.Uncategorized)
    }
}

