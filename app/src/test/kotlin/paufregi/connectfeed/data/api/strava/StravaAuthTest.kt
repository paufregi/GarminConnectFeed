package paufregi.connectfeed.data.api.strava

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.MockWebServerRule
import paufregi.connectfeed.stravaAuthToken
import paufregi.connectfeed.stravaAuthTokenJson
import paufregi.connectfeed.stravaDeauthorizationJson
import paufregi.connectfeed.stravaRefreshTokenJson
import paufregi.connectfeed.stravaRefreshedAuthToken

class StravaAuthTest {

    @JvmField @Rule val server = MockWebServerRule()
    private lateinit var api: StravaAuth

    @Before
    fun setup() {
        api = StravaAuth.client(server.url("/").toString())
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `Exchange token`() = runTest {
        server.enqueue(code = 200, body = stravaAuthTokenJson)

        val res = api.exchange("CLIENT_ID", "CLIENT_SECRET", "CODE")

        val request = server.takeRequest()

        assertThat(request.method).isEqualTo("POST")
        assertThat(request.url.encodedPath).isEqualTo("/api/v3/oauth/token")
        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(stravaAuthToken)
    }

    @Test
    fun `Exchange token - failure`() = runTest {
        server.enqueue(400)

        val res = api.exchange("CLIENT_ID", "CLIENT_SECRET", "CODE")

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }

    @Test
    fun `Refresh token`() = runTest {
        server.enqueue(code = 200, body = stravaRefreshTokenJson)

        val res = api.exchange("CLIENT_ID", "CLIENT_SECRET", "TOKEN")

        val request = server.takeRequest()

        assertThat(request.method).isEqualTo("POST")
        assertThat(request.url.encodedPath).isEqualTo("/api/v3/oauth/token")
        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(stravaRefreshedAuthToken)
    }

    @Test
    fun `Refresh - failure`() = runTest {
        server.enqueue(400)

        val res = api.exchange("CLIENT_ID", "CLIENT_SECRET", "TOKEN")

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }

    @Test
    fun `Remove authorization`() = runTest {
        server.enqueue(code = 200, body = stravaDeauthorizationJson)

        val res = api.deauthorization("TOKEN")

        val request = server.takeRequest()

        assertThat(request.method).isEqualTo("POST")
        assertThat(request.url.encodedPath).isEqualTo("/oauth/deauthorize")
        assertThat(res.isSuccessful).isTrue()
    }

    @Test
    fun `Remove authorization - failure`() = runTest {
        server.enqueue(400)

        val res = api.deauthorization("TOKEN")

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }
}