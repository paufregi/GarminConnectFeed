package paufregi.connectfeed.data.api.garmin.serializers

import com.google.common.truth.Truth
import kotlinx.serialization.json.Json
import org.junit.Test
import java.time.LocalDateTime

class LocalDateTimeSerializerTest {

    val json = Json { encodeDefaults = true }
    val dataString = "\"2024-10-24 06:52:48\""
    val date: LocalDateTime = LocalDateTime.of(2024, 10, 24, 6, 52, 48)

    @Test
    fun `Serialize LocalDateTime to JSON`() {
        val result = json.encodeToString(LocalDateTimeSerializer, date)

        Truth.assertThat(result).isEqualTo(dataString)
    }

    @Test
    fun `Deserialize JSON string to LocalDateTime`() {
        val result = json.decodeFromString(LocalDateTimeSerializer, dataString)

        Truth.assertThat(result).isEqualTo(date)
    }
}