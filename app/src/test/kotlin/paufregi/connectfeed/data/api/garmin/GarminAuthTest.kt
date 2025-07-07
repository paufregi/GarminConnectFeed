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
import paufregi.connectfeed.data.api.garmin.models.PreAuthToken

class GarminAuthTest {
    @JvmField @Rule val server = MockWebServerRule()

    private lateinit var api: GarminAuth
    private val consumerKey = "CONSUMER_KEY"
    private val consumerSecret = "CONSUMER_SECRET"
    private val oauth = PreAuthToken("TOKEN", "SECRET")

    @Before
    fun setup() {
        api = GarminAuth.client(consumerKey, consumerSecret, oauth, server.url("/").toString())
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `Get AuthToken`() = runTest {
        server.enqueue(code = 200, body = authTokenJson)

        val res = api.exchange()

        val request = server.takeRequest()

        assertThat(request.method).isEqualTo("POST")
        assertThat(request.url.toUrl().path).isEqualTo("/oauth-service/oauth/exchange/user/2.0")
        assertThat(request.headers["authorization"]).contains("OAuth")
        assertThat(request.headers["authorization"]).contains("""oauth_consumer_key="CONSUMER_KEY"""")
        assertThat(request.headers["authorization"]).contains("""oauth_token="TOKEN"""")
        assertThat(request.headers["authorization"]).contains("""oauth_signature_method="HMAC-SHA1"""")
        assertThat(request.headers["authorization"]).contains("""oauth_signature""")
        assertThat(request.headers["authorization"]).contains("""oauth_version="1.0"""")
        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(authToken)
    }

    @Test
    fun `Get AuthToken - failure`() = runTest {
        server.enqueue(400)

        val res = api.exchange()

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }

    @Test
    fun `Refresh AuthToken`() = runTest {
        server.enqueue(code = 200, body = authTokenJson)

        val res = api.refresh("REFRESH_TOKEN")

        val request = server.takeRequest()

        assertThat(request.method).isEqualTo("POST")
        assertThat(request.url.encodedPath ).isEqualTo("/oauth-service/oauth/exchange/user/2.0")
        assertThat(request.headers["authorization"]).contains("OAuth")
        assertThat(request.headers["authorization"]).contains("""oauth_consumer_key="CONSUMER_KEY"""")
        assertThat(request.headers["authorization"]).contains("""oauth_token="TOKEN"""")
        assertThat(request.headers["authorization"]).contains("""oauth_signature_method="HMAC-SHA1"""")
        assertThat(request.headers["authorization"]).contains("""oauth_signature""")
        assertThat(request.headers["authorization"]).contains("""oauth_version="1.0"""")
        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(authToken)
    }

    @Test
    fun `Refresh AuthToken - failure`() = runTest {
        server.enqueue(400)

        val res = api.refresh("REFRESH_TOKEN")

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }
}