package paufregi.connectfeed.data.api.garmin

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.authToken
import paufregi.connectfeed.authTokenJson
import paufregi.connectfeed.data.api.garmin.models.OAuthConsumer
import paufregi.connectfeed.data.api.garmin.models.PreAuthToken

class GarminAuthTest {

    private var server: MockWebServer = MockWebServer()
    private lateinit var api: GarminAuth
    private val consumer = OAuthConsumer("KEY", "SECRET")
    private val oauth = PreAuthToken("TOKEN", "SECRET")

    @Before
    fun setup() {
        server.start()
        api = GarminAuth.client(consumer, oauth, server.url("/").toString())
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `Get AuthToken`() = runTest {
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(authTokenJson)
        server.enqueue(response)

        val res = api.exchange()

        val request = server.takeRequest()

        assertThat(request.method).isEqualTo("POST")
        assertThat(request.requestUrl?.toUrl()?.path).isEqualTo("/oauth-service/oauth/exchange/user/2.0")
        assertThat(request.headers["authorization"]).contains("OAuth")
        assertThat(request.headers["authorization"]).contains("""oauth_consumer_key="KEY"""")
        assertThat(request.headers["authorization"]).contains("""oauth_token="TOKEN"""")
        assertThat(request.headers["authorization"]).contains("""oauth_signature_method="HMAC-SHA1"""")
        assertThat(request.headers["authorization"]).contains("""oauth_signature""")
        assertThat(request.headers["authorization"]).contains("""oauth_version="1.0"""")
        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(authToken)
    }

    @Test
    fun `Get AuthToken - failure`() = runTest {
        val response = MockResponse()
            .setResponseCode(400)
        server.enqueue(response)

        val res = api.exchange()

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }
}