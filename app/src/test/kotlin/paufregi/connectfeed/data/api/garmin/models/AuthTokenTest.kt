package paufregi.connectfeed.data.api.garmin.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.createAuthToken
import paufregi.connectfeed.today

class AuthTokenTest {

    private val token = createAuthToken(today)

    @Test
    fun `Valid access token`() {
        val now = today.minusSeconds(30)
        assertThat(token.isExpired(now)).isFalse()
    }

    @Test
    fun `Expired access token`() {
        val now = today.plusSeconds(30)
        assertThat(token.isExpired(now)).isTrue()
    }

    @Test
    fun `Valid refresh token`() {
        val now = today.minusSeconds(30)
        assertThat(token.isRefreshExpired(now)).isFalse()
    }

    @Test
    fun `Expired refresh token`() {
        val now = today.plusSeconds(40)
        assertThat(token.isExpired(now)).isTrue()
    }
}