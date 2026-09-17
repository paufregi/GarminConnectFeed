package paufregi.connectfeed.data.api.strava.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.createStravaToken
import paufregi.connectfeed.today
import kotlin.time.Duration.Companion.seconds

class AuthTokenTest {

    private val token = createStravaToken(today)

    @Test
    fun `Valid token`() {
        val now = today - 10.seconds
        assertThat(token.isExpired(now)).isFalse()
    }

    @Test
    fun `Expired token`() {
        val now = today + 10.seconds
        assertThat(token.isExpired(now)).isTrue()
    }
}