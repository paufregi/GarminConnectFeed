package paufregi.connectfeed.data.api.utils.serializers

import com.google.common.truth.Truth
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.time.Instant

class InstantSerializerTest {

    val json = Json { encodeDefaults = true }
    val instant = Instant.parse("2025-01-01T01:00:00Z")

    @Test
    fun `Serialize Instant to JSON`() {
        val result = json.encodeToString(InstantSerializer, instant)

        Truth.assertThat(result.toLong()).isEqualTo(instant.toEpochMilliseconds())
    }

    @Test
    fun `Deserialize JSON to Instant`() {
        val result = json.decodeFromString(InstantSerializer, instant.toEpochMilliseconds().toString())

        Truth.assertThat(result).isEqualTo(instant)
    }
}