package paufregi.connectfeed.data.api.strava

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.data.api.strava.models.Token
import paufregi.connectfeed.stravaDeauthorizationJson
import paufregi.connectfeed.stravaExchangeTokenJson
import paufregi.connectfeed.stravaRefreshTokenJson

class StravaAuthTest {

    private var server: MockWebServer = MockWebServer()
    private lateinit var api: StravaAuth

    @Before
    fun setup() {
        server.start()
        api = StravaAuth.client(server.url("/").toString())
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `Exchange token`() = runTest {
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(stravaExchangeTokenJson)
        server.enqueue(response)

        val res = api.exchange("CLIENT_ID", "CLIENT_SECRET", "CODE")

        val request = server.takeRequest()

        assertThat(request.method).isEqualTo("POST")
        assertThat(request.requestUrl?.toUrl()?.path).isEqualTo("/oauth/token")
        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(Token(accessToken = "ACCESS_TOKEN", refreshToken = "REFRESH_TOKEN", expiresAt = 1704067200))
    }

    @Test
    fun `Exchange token - failure`() = runTest {
        val response = MockResponse()
            .setResponseCode(400)
        server.enqueue(response)

        val res = api.exchange("CLIENT_ID", "CLIENT_SECRET", "CODE")

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }

    @Test
    fun `Refresh token`() = runTest {
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(stravaRefreshTokenJson)
        server.enqueue(response)

        val res = api.exchange("CLIENT_ID", "CLIENT_SECRET", "TOKEN")

        val request = server.takeRequest()

        assertThat(request.method).isEqualTo("POST")
        assertThat(request.requestUrl?.toUrl()?.path).isEqualTo("/oauth/token")
        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(Token(accessToken = "ACCESS_TOKEN", refreshToken = "REFRESH_TOKEN", expiresAt = 1704067200))
    }

    @Test
    fun `Refresh - failure`() = runTest {
        val response = MockResponse()
            .setResponseCode(400)
        server.enqueue(response)

        val res = api.exchange("CLIENT_ID", "CLIENT_SECRET", "TOKEN")

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }

    @Test
    fun `Remove authorization`() = runTest {
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(stravaDeauthorizationJson)
        server.enqueue(response)

        val res = api.deauthorization("TOKEN")

        val request = server.takeRequest()

        assertThat(request.method).isEqualTo("POST")
        assertThat(request.requestUrl?.toUrl()?.path).isEqualTo("/oauth/deauthorize")
        assertThat(res.isSuccessful).isTrue()
    }

    @Test
    fun `Remove authorization - failure`() = runTest {
        val response = MockResponse()
            .setResponseCode(400)
        server.enqueue(response)

        val res = api.deauthorization("TOKEN")

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }
}