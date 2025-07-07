package paufregi.connectfeed.data.api.garmin

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.MockWebServerRule
import paufregi.connectfeed.data.api.garmin.models.CSRF
import paufregi.connectfeed.data.api.garmin.models.Ticket
import paufregi.connectfeed.htmlCSRF
import paufregi.connectfeed.htmlTicket

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
    fun `Get CSRF`() = runTest {
        server.enqueue(code = 200, body = htmlCSRF)

        val res = api.getCSRF()

        val request = server.takeRequest()

        assertThat(request.method).isEqualTo("GET")
        assertThat(request.url.encodedPath).isEqualTo("/sso/signin")
        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(CSRF("TEST_CSRF_VALUE"))
    }

    @Test
    fun `Get CSRF - failure`() = runTest {
        server.enqueue(400)

        val res = api.getCSRF()

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }

    @Test
    fun `Get Ticket`() = runTest {
        server.enqueue(code = 200, body = htmlTicket)

        val res = api.login(username = "user", password = "pass", csrf = CSRF("csrf"))

        val request = server.takeRequest()

        assertThat(request.method).isEqualTo("POST")
        assertThat(request.url.encodedPath).isEqualTo("/sso/signin")
        assertThat(request.body.toString()).isEqualTo("[text=username=user&password=pass&_csrf=csrf&embed=true]")
        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(Ticket("TEST_TICKET_VALUE"))
    }

    @Test
    fun `Get Ticket - failure`() = runTest {
        server.enqueue(400)

        val res = api.login(username = "user", password = "pass", csrf = CSRF("csrf"))

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }
}