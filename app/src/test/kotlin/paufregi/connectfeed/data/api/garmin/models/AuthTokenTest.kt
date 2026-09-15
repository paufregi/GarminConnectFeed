package paufregi.connectfeed.data.api.garmin.models

import com.appstractive.jwt.jwt
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.createAuthToken
import paufregi.connectfeed.today
import paufregi.connectfeed.tomorrow

class AuthTokenTest {

    val token = createAuthToken(tomorrow)

    @Test
    fun `Is not expired`() {
        assertThat(token.isExpired(today)).isFalse()
    }

    @Test
    fun `Is expired`() {
        assertThat(token.isExpired(tomorrow)).isTrue()
    }

    @Test
    fun `Is expired - no expiration`() {
        val authToken = AuthToken(
            accessToken = jwt { claims { } }.toString(),
            refreshToken = "TOKEN"
        )

        assertThat(authToken.isExpired(today)).isTrue()
    }
}