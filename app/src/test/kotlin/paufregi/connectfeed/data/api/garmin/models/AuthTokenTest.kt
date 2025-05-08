package paufregi.connectfeed.data.api.garmin.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Instant

class AuthTokenTest {

    @Test
    fun `Valid access token`() {
        val token = AuthToken(
            accessToken = "ACCESS_TOKEN",
            refreshToken = "REFRESH_TOKEN",
            expiresAt = Instant.parse("2025-01-01T01:00:00Z"),
            refreshExpiresAt = Instant.parse("2025-01-01T01:00:00Z"),
        )

        val now = Instant.parse("2025-01-01T00:30:00Z")

        assertThat(token.isExpired(now)).isFalse()
    }

    @Test
    fun `Expired access token`() {
        val token = AuthToken(
            accessToken = "ACCESS_TOKEN",
            refreshToken = "REFRESH_TOKEN",
            expiresAt = Instant.parse("2025-01-01T01:00:00Z"),
            refreshExpiresAt = Instant.parse("2025-01-01T01:00:00Z"),
        )

        val now = Instant.parse("2025-01-01T01:30:00Z")

        assertThat(token.isExpired(now)).isFalse()
    }

    @Test
    fun `Valid refresh token`() {
        val token = AuthToken(
            accessToken = "ACCESS_TOKEN",
            refreshToken = "REFRESH_TOKEN",
            expiresAt = Instant.parse("2025-01-01T01:00:00Z"),
            refreshExpiresAt = Instant.parse("2025-01-01T02:00:00Z"),
        )

        val now = Instant.parse("2025-01-01T01:30:00Z")

        assertThat(token.isExpired(now)).isFalse()
    }

    @Test
    fun `Expired refresh token`() {
        val token = AuthToken(
            accessToken = "ACCESS_TOKEN",
            refreshToken = "REFRESH_TOKEN",
            expiresAt = Instant.parse("2025-01-01T01:00:00Z"),
            refreshExpiresAt = Instant.parse("2025-01-01T02:00:00Z"),
        )

        val now = Instant.parse("2025-01-01T02:30:00Z")

        assertThat(token.isExpired(now)).isFalse()
    }
}