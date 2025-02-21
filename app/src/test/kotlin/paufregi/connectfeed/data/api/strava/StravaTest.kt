package paufregi.connectfeed.data.api.strava

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.data.api.garmin.Garth
import paufregi.connectfeed.data.api.garmin.models.OAuthConsumer
import paufregi.connectfeed.data.api.strava.models.Token

class StravaTest {

    private var server: MockWebServer = MockWebServer()
    private lateinit var api: Strava

    @Before
    fun setup() {
        server.start()
//        api = Strava.client(server.url("/").toString())
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}