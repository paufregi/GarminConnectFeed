package paufregi.connectfeed.data.api.strava.converters

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.time.Instant

class InstantSerializerTest {

    val json = Json { encodeDefaults = true }

    private val instant = Instant.parse("2025-01-01T01:00:00Z")

    @Test
    fun `Serialize Instant to JSON`() {
        val res = json.encodeToString(InstantSerializer, instant)

        assertThat(res.toLong()).isEqualTo(instant.toEpochMilliseconds())
    }

    @Test
    fun `Deserialize JSON to AuthToken`() {
        val res = json.decodeFromString(InstantSerializer, instant.toEpochMilliseconds().toString())

        assertThat(res).isEqualTo(instant)
    }
}