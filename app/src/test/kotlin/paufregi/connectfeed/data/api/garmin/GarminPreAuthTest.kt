package paufregi.connectfeed.data.api.garmin

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.data.api.garmin.models.Ticket
import paufregi.connectfeed.preAuthToken
import paufregi.connectfeed.preAuthTokenBody

class GarminPreAuthTest {

    private var server: MockWebServer = MockWebServer()
    private lateinit var api: GarminPreAuth
    private val consumerKey = "CONSUMER_KEY"
    private val consumerSecret = "CONSUMER_SECRET"

    @Before
    fun setup() {
        server.start()
        api = GarminPreAuth.client(consumerKey, consumerSecret, server.url("/").toString())
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `Get PreAuthToken`() = runTest {
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(preAuthTokenBody)
        server.enqueue(response)

        val ticket = Ticket("TICKET")
        val res = api.preauthorize(ticket)

        val request = server.takeRequest()

        assertThat(request.method).isEqualTo("GET")
        assertThat(request.requestUrl?.toUrl()?.path).isEqualTo("/oauth-service/oauth/preauthorized")
        assertThat(request.requestUrl?.queryParameterValues("ticket")).isEqualTo(listOf("TICKET"))
        assertThat(request.headers["authorization"]).contains("OAuth")
        assertThat(request.headers["authorization"]).contains("""oauth_consumer_key="CONSUMER_KEY"""")
        assertThat(request.headers["authorization"]).contains("""oauth_signature_method="HMAC-SHA1"""")
        assertThat(request.headers["authorization"]).contains("""oauth_signature""")
        assertThat(request.headers["authorization"]).contains("""oauth_version="1.0"""")
        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(preAuthToken)

    }

    @Test
    fun `Get PreAuthToken - failure`() = runTest {
        val response = MockResponse()
            .setResponseCode(400)
        server.enqueue(response)

        val ticket = Ticket("TICKET")
        val res = api.preauthorize(ticket)

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }
}