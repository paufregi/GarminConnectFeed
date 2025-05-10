package paufregi.connectfeed.data.api.strava.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.createStravaToken
import paufregi.connectfeed.today

class AuthTokenTest {

    private val token = createStravaToken(today)

    @Test
    fun `Valid access token`() {
        val now = today.minusSeconds(10)
        assertThat(token.isExpired(now)).isFalse()
    }

    @Test
    fun `Expired access token`() {
        val now = today.plusSeconds(10)
        assertThat(token.isExpired(now)).isTrue()
    }
}