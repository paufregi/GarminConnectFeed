package paufregi.connectfeed.data.api.garmin

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.consumer
import paufregi.connectfeed.consumerJson

class GarthTest {

    private var server: MockWebServer = MockWebServer()
    private lateinit var api: Garth

    @Before
    fun setup() {
        server.start()
        api = Garth.client(server.url("/").toString())
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `Get OAuth consumer`() = runTest {
        val response = MockResponse()
            .setResponseCode(200)
            .setBody(consumerJson)
        server.enqueue(response)

        val res = api.getOAuthConsumer()

        val request = server.takeRequest()

        assertThat(request.method).isEqualTo("GET")
        assertThat(request.requestUrl?.toUrl()?.path).isEqualTo("/oauth_consumer.json")
        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(consumer)
    }

    @Test
    fun `Get OAuth consumer - failure`() = runTest {
        val response = MockResponse()
            .setResponseCode(400)
        server.enqueue(response)

        val res = api.getOAuthConsumer()

        assertThat(res.isSuccessful).isFalse()
        assertThat(res.body()).isNull()
    }
}