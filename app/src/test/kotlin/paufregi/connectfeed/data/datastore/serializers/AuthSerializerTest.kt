package paufregi.connectfeed.data.datastore.serializers

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Test
import paufregi.connectfeed.core.models.User
import paufregi.connectfeed.data.datastore.models.Auth
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.time.Instant
import paufregi.connectfeed.data.api.garmin.models.AuthToken as GarminAuthToken
import paufregi.connectfeed.data.api.strava.models.AuthToken as StravaAuthToken

class AuthSerializerTest {

    private val json = Json { encodeDefaults = true }

    private val user = User(1, "Paul", "https://example.com/avatar.jpg")

    private val garminToken = GarminAuthToken(
        accessToken = "GARMIN_ACCESS",
        refreshToken = "GARMIN_REFRESH"
    )
    private val stravaToken = StravaAuthToken(
        accessToken = "STRAVA_ACCESS",
        refreshToken = "STRAVA_REFRESH",
        expiresAt = Instant.parse("2025-01-01T00:00:00Z")
    )
    private val auth = Auth(
        user = user,
        garminToken = garminToken,
        stravaToken = stravaToken
    )

    @Test
    fun `Serialize Auth to JSON`() = runBlocking {
        val output = ByteArrayOutputStream()

        AuthSerializer.writeTo(auth, output)

        assertThat(output.toString(Charsets.UTF_8)).isEqualTo(
            json.encodeToString(Auth.serializer(), auth)
        )
    }

    @Test
    fun `Deserialize JSON to Auth`() = runBlocking {
        val data = json.encodeToString(Auth.serializer(), auth).toByteArray(Charsets.UTF_8)

        val result = AuthSerializer.readFrom(ByteArrayInputStream(data))

        assertThat(result).isEqualTo(auth)
    }

    @Test
    fun `Fallback to default value when the payload is invalid`() = runBlocking {
        val result = AuthSerializer.readFrom(ByteArrayInputStream("not valid json".toByteArray(Charsets.UTF_8)))

        assertThat(result).isEqualTo(AuthSerializer.defaultValue)
    }
}
