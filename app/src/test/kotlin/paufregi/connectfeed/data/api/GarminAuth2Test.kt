package paufregi.connectfeed.data.api

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.data.api.garmin.GarminAuth2
import paufregi.connectfeed.data.api.garmin.models.OAuth1
import paufregi.connectfeed.data.api.garmin.models.OAuthConsumer
import paufregi.connectfeed.oauth2
import paufregi.connectfeed.oauth2Body

class GarminAuth2Test {

    private var server: MockWebServer = MockWebServer()
    private lateinit var api: GarminAuth2
    private val consumer = OAuthConsumer("KEY", "SECRET")
    private val oauth = OAuth1("TOKEN", "SECRET")

    @Before
    fun setup() {
        server.start()
        api = GarminAuth2.Companion.client(consumer, oauth, server.url("/").toString())
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `Get OAuth2 token`() = runTest {
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(oauth2Body)
        server.enqueue(response)

        val res = api.getOauth2()

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
        assertThat(res.body()).isEqualTo(oauth2)
    }

    @Test
    fun `Get OAuth2 token - failure`() = runTest {
        val response = MockResponse()
            .setResponseCode(400)
        server.enqueue(response)

        val res = api.getOauth2()

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }
}