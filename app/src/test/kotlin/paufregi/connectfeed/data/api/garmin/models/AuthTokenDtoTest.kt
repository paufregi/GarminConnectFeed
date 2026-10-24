package paufregi.connectfeed.data.api.garmin.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.createAuthToken
import paufregi.connectfeed.today
import paufregi.connectfeed.tomorrow
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

class AuthTokenDtoTest {

    val token = createAuthToken(today)

    @Test
    fun `Issued at`() {
        assertThat(token.issuedAt).isEqualTo(today)
    }

    @Test
    fun `Is expired - false`() {
        assertThat(token.isExpired(today)).isFalse()
    }

    @Test
    fun `Is expired - true`() {
        assertThat(token.isExpired(tomorrow)).isTrue()
    }

    @Test
    fun `Is refresh token expired - false`() {
        assertThat(token.isRefreshTokenExpired(today)).isFalse()
    }

    @Test
    fun `Is refresh token expired - false before lifetime ends`() {
        assertThat(token.isRefreshTokenExpired(tomorrow - 1.seconds)).isFalse()
    }

    @Test
    fun `Is refresh token expired - true at lifetime end`() {
        assertThat(token.isRefreshTokenExpired(tomorrow)).isTrue()
    }

    @Test
    fun `Is refresh token expired - true after lifetime`() {
        assertThat(token.isRefreshTokenExpired(tomorrow + 1.days)).isTrue()
    }
}