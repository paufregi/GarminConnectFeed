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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.MockServer
import paufregi.connectfeed.data.api.strava.interceptors.StravaAuthInterceptor
import paufregi.connectfeed.data.api.strava.models.ActivityDto
import paufregi.connectfeed.data.api.strava.models.UpdateActivityDto
import paufregi.connectfeed.data.api.strava.models.UpdateProfileDto
import paufregi.connectfeed.stravaActivitiesJson
import paufregi.connectfeed.stravaDetailedAthlete

class StravaTest {

    @JvmField @Rule val server = MockServer()
    private lateinit var api: Strava
    private val authInterceptor = mockk<StravaAuthInterceptor>()

    val chain = slot<Interceptor.Chain>()

    @Before
    fun setup() {
        api = Strava.client(authInterceptor, server.url("/").toString())
        every {
            authInterceptor.intercept(capture(chain))
        } answers {
            chain.captured.proceed(chain.captured.request())
        }
    }

    @After
    fun tearDown() {
        confirmVerified(authInterceptor)
        clearAllMocks()
    }

    @Test
    fun `Get activities`() = runTest {
        server.enqueue(code = 200, body = stravaActivitiesJson)

        val res = api.getActivities(perPage = 3)

        val expected = listOf(
            ActivityDto(
                id = 1,
                name = "Happy Friday",
                distance = 7803.6,
                sportType = "Run",
                startDate = "2018-05-02T12:15:09Z"
            ),
            ActivityDto(
                id = 2,
                name = "Bondcliff",
                distance = 23676.5,
                sportType = "Ride",
                startDate = "2024-10-24T07:15:30Z"
            )
        )

        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(expected)
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Get activities - empty`() = runTest {
        server.enqueue(code = 200, body = "[]")

        val res = api.getActivities(perPage = 3)

        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(emptyList<ActivityDto>())
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Get activities - failure`() = runTest {
        server.enqueue(400)

        val res = api.getActivities(perPage = 3)

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Update activity`() = runTest {
        server.enqueue(200)
        val updateActivityDto = UpdateActivityDto(
            name = "newName",
            description = "newDescription",
            commute = true,
        )
        val res = api.updateActivity(id = 1, updateActivity = updateActivityDto)

        assertThat(res.isSuccessful).isTrue()
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Update activity - failure`() = runTest {
        server.enqueue(400)
        val updateActivityDto = UpdateActivityDto(
            name = "newName",
            description = "newDescription",
            commute = true,
        )
        val res = api.updateActivity(id = 1, updateActivity = updateActivityDto)

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Update profile`() = runTest {
        server.enqueue(code = 200, body = stravaDetailedAthlete)
        val updateProfileDto = UpdateProfileDto(weight = 75.5f)
        val res = api.updateProfile(updateProfile = updateProfileDto)

        assertThat(res.isSuccessful).isTrue()
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Update profile - failure`() = runTest {
        server.enqueue(400)
        val updateProfileDto = UpdateProfileDto(weight = 75.5f)
        val res = api.updateProfile(updateProfile = updateProfileDto)

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
    }
}