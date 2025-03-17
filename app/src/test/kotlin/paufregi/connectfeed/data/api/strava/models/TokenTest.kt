package paufregi.connectfeed.data.api.strava.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import paufregi.connectfeed.data.api.garmin.models.OAuth2
import java.util.Date

class TokenTest {

    @Test
    fun `Valid access token`() {
        val token = Token(
            accessToken = "ACCESS_TOKEN",
            refreshToken = "REFRESH_TOKEN",
            expiresAt = 1704067200, // 2024-01-01T00:00
        )

        val date = 1672531200L // 2023-01-01T00:00

        assertThat(token.isExpired(date)).isFalse()
    }

    @Test
    fun `Expired access token`() {
        val token = Token(
            accessToken = "ACCESS_TOKEN",
            refreshToken = "REFRESH_TOKEN",
            expiresAt = 1672531200, // 2023-01-01T00:00
        )

        val date = 1704067200L // 2024-01-01T00:00

        assertThat(token.isExpired(date)).isTrue()
    }
}