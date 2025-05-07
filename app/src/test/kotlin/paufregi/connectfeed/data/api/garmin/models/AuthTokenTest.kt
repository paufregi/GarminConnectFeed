package paufregi.connectfeed.data.api.garmin.models

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.Date

class AuthTokenTest {

    @Test
    fun `Valid access token`() {
        val token = AuthToken(
            accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NCIsIm5hbWUiOiJQYXVsIEVsbGlzIiwiZXhwIjoxNzA0MDI0MDAwLCJpYXQiOjE3MDQwMjQwMDB9.BAAoEhz3DEQfSe77n1BtDZEYX-e3_2_lfGIgx-QXEew", // ExpiresAt: 2024-01-01T00:00
        )

        val date = Date(1672488000) // 2023-01-01T00:00

        assertThat(token.isExpired(date)).isFalse()
    }

    @Test
    fun `Expired access token`() {
        val token = AuthToken(
            accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NCIsIm5hbWUiOiJQYXVsIEVsbGlzIiwiZXhwIjoxNzA0MDI0MDAwLCJpYXQiOjE3MDQwMjQwMDB9.BAAoEhz3DEQfSe77n1BtDZEYX-e3_2_lfGIgx-QXEew", // ExpiresAt: 2024-01-01T00:00
        )

        val date = Date(1722430800000) // 2024-08-01T00:00

        assertThat(token.isExpired(date)).isTrue()
    }

    @Test
    fun `Empty access token OAuth2`() {
        val token = AuthToken(
            accessToken = "",
        )

        val date = Date(1722430800000) // 2024-08-01T00:00

        assertThat(token.isExpired(date)).isTrue()
    }
}