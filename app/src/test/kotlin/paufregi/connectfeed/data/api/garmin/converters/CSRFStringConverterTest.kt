package paufregi.connectfeed.data.api.garmin.converters

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.data.api.garmin.models.CSRF

class CSRFStringConverterTest {

    private val converter = CSRFStringConverter()

    @Test
    fun `Convert CSRF to string`() {
        val result = converter.convert(CSRF("test"))

        assertThat(result).isEqualTo("test")
    }
}
