package paufregi.connectfeed.data.api.garmin.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import org.junit.Test
import paufregi.connectfeed.core.models.GearType

class GearTypeSerializerTest {

    private val json = Json { encodeDefaults = true }

    @Test
    fun `Serialize GearType Bike to JSON`() {
        val res = json.encodeToString(GearTypeSerializer, GearType.Bike)

        assertThat(res).isEqualTo("\"BIKE\"")
    }

    @Test
    fun `Serialize GearType Shoe to JSON`() {
        val res = json.encodeToString(GearTypeSerializer, GearType.Shoe)

        assertThat(res).isEqualTo("\"SHOE\"")
    }

    @Test
    fun `Serialize GearType Unknown to JSON`() {
        val res = json.encodeToString(GearTypeSerializer, GearType.Unknown)

        assertThat(res).isEqualTo("\"UNKNOWN\"")
    }

    @Test
    fun `Deserialize JSON to GearType Bike`() {
        val res = json.decodeFromString(GearTypeSerializer, "\"BIKE\"")

        assertThat(res).isEqualTo(GearType.Bike)
    }

    @Test
    fun `Deserialize JSON to GearType Shoe`() {
        val res = json.decodeFromString(GearTypeSerializer, "\"SHOE\"")

        assertThat(res).isEqualTo(GearType.Shoe)
    }

    @Test
    fun `Deserialize unknown JSON to GearType Unknown`() {
        val res = json.decodeFromString(GearTypeSerializer, "\"UNKNOWN\"")

        assertThat(res).isEqualTo(GearType.Unknown)
    }

    @Test
    fun `Deserialize unsupported JSON to GearType Unknown`() {
        val res = json.decodeFromString(GearTypeSerializer, "\"NOPE\"")

        assertThat(res).isEqualTo(GearType.Unknown)
    }
}

