package paufregi.connectfeed.data.api.garmin.converters

import com.google.common.truth.Truth.assertThat
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import paufregi.connectfeed.data.api.garmin.models.CSRF
import paufregi.connectfeed.htmlCSRF

class CSRFConverterTest {

    private val mediaType = "text/html; charset=UTF-8"

    @Test
    fun `ResponseBody to CSRF`() {
        val responseBody = htmlCSRF.toResponseBody(mediaType.toMediaType())
        val result = CSRFConverter.ResponseBodyConverter.convert(responseBody)

        assertThat(result).isEqualTo(CSRF("TEST_CSRF_VALUE"))
    }

    @Test
    fun `ResponseBody to CSRF - no CSRF`() {
        val responseBody = "".toResponseBody(mediaType.toMediaType())
        val result = CSRFConverter.ResponseBodyConverter.convert(responseBody)

        assertThat(result).isEqualTo(CSRF(""))
    }

    @Test
    fun `CSRF to string`() {
        val csrf = CSRF("TEST_CSRF_VALUE")
        val result = CSRFConverter.StringConverter.convert(csrf)

        assertThat(result).isEqualTo("TEST_CSRF_VALUE")
    }
}