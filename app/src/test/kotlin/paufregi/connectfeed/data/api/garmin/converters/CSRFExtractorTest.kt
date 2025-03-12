package paufregi.connectfeed.data.api.garmin.converters

import com.google.common.truth.Truth.assertThat

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody

import org.junit.Test
import paufregi.connectfeed.data.api.garmin.models.CSRF
import paufregi.connectfeed.htmlForCSRF

class CSRFExtractorTest {

    private val converter = CSRFExtractor()
    private val mediaType = "text/html; charset=UTF-8"

    @Test
    fun `Extract CSRF token`() {
        val responseBody = htmlForCSRF.toResponseBody(mediaType.toMediaType())

        val result = converter.convert(responseBody)

        assertThat(result).isEqualTo(CSRF("TEST_CSRF_VALUE"))
    }

    @Test
    fun `No CSRF token`() {
        val responseBody = "".toResponseBody(mediaType.toMediaType())

        val result = converter.convert(responseBody)

        assertThat(result).isEqualTo(CSRF(""))
    }
}