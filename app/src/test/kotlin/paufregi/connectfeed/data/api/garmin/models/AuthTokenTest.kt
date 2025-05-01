package paufregi.connectfeed.data.api.garmin.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AuthTokenTest {

    @Test
    fun `Valid access token`() {
        val authToken = AuthToken(
            accessToken = "ACCESS_TOKEN",
            expiresAt = 1704067200, // 2024-01-01T00:00
        )

        val date = 1672531200L // 2023-01-01T00:00

        assertThat(authToken.isExpired(date)).isFalse()
    }

    @Test
    fun `Expired access token`() {
        val authToken = AuthToken(
            accessToken = "ACCESS_TOKEN",
            expiresAt = 1672531200, // 2023-01-01T00:00
        )

        val date = 1704067200L // 2024-01-01T00:00

        assertThat(authToken.isExpired(date)).isTrue()
    }
}