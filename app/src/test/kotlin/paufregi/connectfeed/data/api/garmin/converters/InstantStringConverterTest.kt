package paufregi.connectfeed.data.api.garmin.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant

class InstantStringConverterTest {

    private val converter = InstantStringConverter()

    @Test
    fun `Convert Ticket to string`() {
        val result = converter.convert(Instant.ofEpochMilli(1735693200000))

        assertThat(result).isEqualTo("2025-01-01")
    }
}
