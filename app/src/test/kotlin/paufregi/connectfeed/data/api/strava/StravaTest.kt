package paufregi.connectfeed.data.api.strava

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import okhttp3.Interceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.data.api.strava.interceptors.StravaAuthInterceptor
import paufregi.connectfeed.data.api.strava.models.Activity
import paufregi.connectfeed.data.api.strava.models.UpdateActivity
import paufregi.connectfeed.stravaLatestActivitiesJson

class StravaTest {

    private var server: MockWebServer = MockWebServer()
    private lateinit var api: Strava
    private val authInterceptor = mockk<StravaAuthInterceptor>()

    val chain = slot<Interceptor.Chain>()

    @Before
    fun setup() {
        server.start()
        api = Strava.client(authInterceptor, server.url("/").toString())
        every {
            authInterceptor.intercept(capture(chain))
        } answers {
            chain.captured.proceed(chain.captured.request())
        }
    }

    @After
    fun tearDown() {
        clearAllMocks()
        server.shutdown()
    }

    @Test
    fun `Get latest activities`() = runTest {
        val response = MockResponse().setResponseCode(200).setBody(stravaLatestActivitiesJson)
        server.enqueue(response)

        val res = api.getLatestActivities(perPage = 3)

        val expected = listOf(
            Activity(
                id = 1,
                name = "Happy Friday",
                distance = 7803.6,
                sportType = "Run"

            ),
            Activity(
                id = 2,
                name = "Bondcliff",
                distance = 23676.5,
                sportType = "Ride"
            )
        )

        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(expected)
        verify { authInterceptor.intercept(any()) }
        confirmVerified(authInterceptor)
    }

    @Test
    fun `Get latest activities - empty`() = runTest {
        val response = MockResponse().setResponseCode(200).setBody("[]")
        server.enqueue(response)

        val res = api.getLatestActivities(perPage = 3)

        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(emptyList<Activity>())
        verify { authInterceptor.intercept(any()) }
        confirmVerified(authInterceptor)
    }

    @Test
    fun `Get latest activities - failure`() = runTest {
        val response = MockResponse().setResponseCode(400)
        server.enqueue(response)

        val res = api.getLatestActivities(perPage = 3)

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
        confirmVerified(authInterceptor)
    }

    @Test
    fun `Update activity`() = runTest {
        val response = MockResponse().setResponseCode(200)
        server.enqueue(response)
        val updateActivity = UpdateActivity(
            name = "newName",
            description = "newDescription",
            commute = true,
        )
        val res = api.updateActivity(id = 1, updateActivity = updateActivity)

        assertThat(res.isSuccessful).isTrue()
        verify { authInterceptor.intercept(any()) }
        confirmVerified(authInterceptor)
    }

    @Test
    fun `Update activity - failure`() = runTest {
        val response = MockResponse().setResponseCode(400)
        server.enqueue(response)
        val updateActivity = UpdateActivity(
            name = "newName",
            description = "newDescription",
            commute = true,
        )
        val res = api.updateActivity(id = 1, updateActivity = updateActivity)

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
        confirmVerified(authInterceptor)
    }
}