package paufregi.connectfeed.data.api.garmin

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.MockWebServerRule
import paufregi.connectfeed.data.api.garmin.models.LoginRequest
import paufregi.connectfeed.invalidLogin
import paufregi.connectfeed.validLogin

class GarminSSOTest {

    @JvmField @Rule val server = MockWebServerRule()
    private lateinit var api: GarminSSO

    @Before
    fun setup() {
        api = GarminSSO.client(server.url("/").toString())
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `Login - success`() = runTest {
        server.enqueue(code = 200, body = validLogin)

        val request = LoginRequest(
            username = "test@example.com",
            password = "password123",
        )

        val res = api.login(request)
        val resBody = res.body()

        val recordedRequest = server.takeRequest()

        assertThat(recordedRequest.method).isEqualTo("POST")
        assertThat(recordedRequest.url.encodedPath).isEqualTo("/mobile/api/login")
        assertThat(res.isSuccessful).isTrue()
        assertThat(resBody?.responseStatus?.type).isEqualTo("SUCCESSFUL")
        assertThat(resBody?.serviceTicketId).isEqualTo("ST-0123456-XXXXXXXXXXXXXXXXXXXX-sso")
    }

    @Test
    fun `Login - invalid credentials`() = runTest {
        server.enqueue(code = 200, body = invalidLogin)

        val request = LoginRequest(username = "test@example.com", password = "wrongpassword")

        val res = api.login(request)
        val resBody = res.body()

        assertThat(res.isSuccessful).isTrue()
        assertThat(resBody?.responseStatus?.type).isEqualTo("INVALID_USERNAME_PASSWORD")
        assertThat(resBody?.serviceTicketId).isNull()
    }

    @Test
    fun `Login - failure`() = runTest {
        server.enqueue(401)

        val request = LoginRequest(
            username = "test@example.com",
            password = "wrongpassword",
        )

        val res = api.login(request)

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }
}




