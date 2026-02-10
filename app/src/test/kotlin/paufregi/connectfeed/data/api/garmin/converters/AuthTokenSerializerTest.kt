package paufregi.connectfeed.data.api.garmin.converters


import com.appstractive.jwt.jwt
import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import org.junit.Test
import paufregi.connectfeed.data.api.garmin.models.AuthToken
import java.time.Instant

class AuthTokenSerializerTest {

    val json = Json { encodeDefaults = true }

    private val issuedAt = Instant.parse("2025-01-01T01:00:00Z")
    private val accessToken = jwt { issuedAt }.toString()

    private val jsonToken = """{"access_token":"$accessToken","refresh_token":"REFRESH_TOKEN","expires_in":10000,"refresh_token_expires_in":30000}""".trimIndent()
    private val authToken = AuthToken(
        accessToken = accessToken,
        refreshToken = "REFRESH_TOKEN",
        expiresAt = issuedAt.plusSeconds(10),
        refreshExpiresAt = issuedAt.plusSeconds(30)
    )

    @Test
    fun `Serialize AuthToken to JSON`() {
        val res = json.encodeToString(AuthToken.serializer(), authToken)

        assertThat(res).isEqualTo(jsonToken)
    }

    @Test
    fun `Deserialize JSON to AuthToken`() {
        val res = json.decodeFromString(AuthToken.serializer(), jsonToken)

        assertThat(res).isEqualTo(authToken)
    }
}