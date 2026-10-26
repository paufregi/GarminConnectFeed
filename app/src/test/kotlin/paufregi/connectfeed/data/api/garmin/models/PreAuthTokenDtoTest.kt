package paufregi.connectfeed.data.api.garmin.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PreAuthTokenDtoTest {

    @Test
    fun `Valid PreAuthToken`() {
        val token = PreAuthTokenDto(token = "TOKEN", secret = "SECRET")

        assertThat(token.isValid()).isTrue()
    }

    @Test
    fun `Invalid - missing token`() {
        val oauthToken = PreAuthTokenDto(token = "", secret = "SECRET")

        assertThat(oauthToken.isValid()).isFalse()
    }

    @Test
    fun `Invalid - missing secret`() {
        val token = PreAuthTokenDto(token = "TOKEN", secret = "")

        assertThat(token.isValid()).isFalse()
    }
}