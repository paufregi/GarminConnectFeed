package paufregi.connectfeed.data.api.garmin.converters

import com.google.common.truth.Truth.assertThat

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody

import org.junit.Test
import paufregi.connectfeed.data.api.garmin.models.PreAuthTokenDto

class PreAuthTokenExtractorTest {

    private val converter = PreAuthTokenExtractor()

    private val apiResponse = "oauth_token=TEST_TOKEN&oauth_token_secret=TEST_TOKEN_SECRET"
    private val mediaType = "application/json"

    @Test
    fun `Extract PreAuthToken`() {
        val responseBody = apiResponse.toResponseBody(mediaType.toMediaType())

        val result = converter.convert(responseBody)

        assertThat(result).isEqualTo(PreAuthTokenDto("TEST_TOKEN", "TEST_TOKEN_SECRET"))
    }

    @Test
    fun `No PreAuthToken`() {
        val responseBody = "".toResponseBody(mediaType.toMediaType())

        val result = converter.convert(responseBody)

        assertThat(result).isEqualTo(PreAuthTokenDto("", ""))
    }
}