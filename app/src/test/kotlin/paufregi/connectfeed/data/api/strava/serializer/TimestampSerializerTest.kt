package paufregi.connectfeed.data.api.strava.serializer

import com.google.common.truth.Truth
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.time.Instant

class TimestampSerializerTest {

    val json = Json { encodeDefaults = true }
    val dataString = "\"2025-01-01T01:00:00Z\""
    val instant = Instant.parse(dataString)

    @Test
    fun `Serialize Instant to JSON`() {
        val result = json.encodeToString(TimestampSerializer, instant)

        Truth.assertThat(result).isEqualTo(dataString)
    }

    @Test
    fun `Deserialize JSON string to Instant`() {
        val result = json.decodeFromString(TimestampSerializer, dataString)

        Truth.assertThat(result).isEqualTo(instant)
    }
}