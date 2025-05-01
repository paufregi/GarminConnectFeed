package paufregi.connectfeed.data.api.garmin.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PreAuthTokenTest {

    @Test
    fun `Valid PreAuthToken`() {
        val token = PreAuthToken(token = "TOKEN", secret = "SECRET")

        assertThat(token.isValid()).isTrue()
    }

    @Test
    fun `Invalid - missing token`() {
        val oauthToken = PreAuthToken(token = "", secret = "SECRET")

        assertThat(oauthToken.isValid()).isFalse()
    }

    @Test
    fun `Invalid - missing secret`() {
        val token = PreAuthToken(token = "TOKEN", secret = "")

        assertThat(token.isValid()).isFalse()
    }
}