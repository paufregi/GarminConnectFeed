package paufregi.connectfeed.data.api.garmin

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.MockWebServerRule
import paufregi.connectfeed.authToken
import paufregi.connectfeed.authTokenJson
import paufregi.connectfeed.refreshedAuthTokenJson
import paufregi.connectfeed.refreshedToken

class GarminAuthTest {

    @JvmField @Rule val server = MockWebServerRule()
    private lateinit var api: GarminAuth

    @Before
    fun setup() {
        api = GarminAuth.client(server.url("/").toString())
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `Exchange token`() = runTest {
        server.enqueue(code = 200, body = authTokenJson)

        val clientId = "test-client-id"
        val serviceTicket = "ST-0123456-XXXXXXXXXXXXXXXXXXXX-sso"
        val authorization = GarminAuth.buildBasicAuth(clientId)

        val res = api.exchange(authorization, clientId, serviceTicket)

        val request = server.takeRequest()

        assertThat(request.method).isEqualTo("POST")
        assertThat(request.url.encodedPath).isEqualTo("/di-oauth2-service/oauth/token")
        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(authToken)
    }

    @Test
    fun `Exchange token - failure`() = runTest {
        server.enqueue(400)

        val clientId = "test-client-id"
        val serviceTicket = "ST-0123456-XXXXXXXXXXXXXXXXXXXX-sso"
        val authorization = GarminAuth.buildBasicAuth(clientId)

        val res = api.exchange(authorization, clientId, serviceTicket)

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }

    @Test
    fun `Refresh token`() = runTest {
        server.enqueue(code = 200, body = refreshedAuthTokenJson)

        val clientId = "test-client-id"
        val refreshToken = authToken.refreshToken
        val authorization = GarminAuth.buildBasicAuth(clientId)

        val res = api.refresh(authorization, clientId, refreshToken)

        val request = server.takeRequest()

        assertThat(request.method).isEqualTo("POST")
        assertThat(request.url.encodedPath).isEqualTo("/di-oauth2-service/oauth/token")
        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(refreshedToken)
    }

    @Test
    fun `Refresh token - failure`() = runTest {
        server.enqueue(400)

        val clientId = "test-client-id"
        val refreshToken = "invalid-refresh-token"
        val authorization = GarminAuth.buildBasicAuth(clientId)

        val res = api.refresh(authorization, clientId, refreshToken)

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }
}

