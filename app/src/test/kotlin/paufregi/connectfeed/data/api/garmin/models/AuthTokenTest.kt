package paufregi.connectfeed.data.api.garmin.models

import com.appstractive.jwt.jwt
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.createAuthToken
import paufregi.connectfeed.today
import paufregi.connectfeed.tomorrow
import paufregi.connectfeed.yesterday

class AuthTokenTest {

    val token = createAuthToken(today)

    @Test
    fun `Valid token`() {
        assertThat(token.isExpired(yesterday)).isFalse()
    }

    @Test
    fun `Expired token`() {
        assertThat(token.isExpired(tomorrow)).isTrue()
    }

    @Test
    fun `Expired token - null`() {
        val authToken = AuthToken(
            accessToken = jwt { claims { } }.toString(),
            refreshToken = "TOKEN"
        )

        assertThat(authToken.isExpired(today)).isTrue()
    }
}