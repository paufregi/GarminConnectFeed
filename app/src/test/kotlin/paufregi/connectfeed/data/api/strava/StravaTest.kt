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
import paufregi.connectfeed.data.api.strava.interceptors.AuthInterceptor
import paufregi.connectfeed.data.api.strava.models.Activity
import paufregi.connectfeed.data.api.strava.models.Athlete
import paufregi.connectfeed.data.api.strava.models.Bike
import paufregi.connectfeed.data.api.strava.models.Shoe
import paufregi.connectfeed.data.api.strava.models.SportType
import paufregi.connectfeed.data.api.strava.models.UpdateActivity
import paufregi.connectfeed.data.api.strava.models.UpdateAthlete
import paufregi.connectfeed.stravaActivitiesJson
import paufregi.connectfeed.stravaAthlete
import paufregi.connectfeed.stravaDetailedAthlete
import java.time.LocalDateTime

class StravaTest {

    @JvmField @Rule val server = MockServer()
    private lateinit var api: Strava
    private val authInterceptor = mockk<AuthInterceptor>()

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
    fun `Get athlete`() = runTest {
        server.enqueue(code = 200, body = stravaAthlete)

        val res = api.getAthlete()

        val expected = Athlete(
            id = 1,
            bikes = listOf(
                Bike(
                    id = "b12345678987655",
                    name = "Giant Contend",
                )
            ),
            shoes = listOf(
                Shoe(
                    id = "g12345678987655",
                    name = "Mizuno Neo Vista",
                )
            )
        )

        assertThat(res.isSuccessful).isTrue()
        assertThat(res.body()).isEqualTo(expected)
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Get athlete - failure`() = runTest {
        server.enqueue(400)

        val res = api.getAthlete()

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Update athlete`() = runTest {
        server.enqueue(code = 200, body = stravaDetailedAthlete)
        val updateAthlete = UpdateAthlete(weight = 75.5f)
        val res = api.updateAthlete(updateAthlete = updateAthlete)

        assertThat(res.isSuccessful).isTrue()
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Update athlete - failure`() = runTest {
        server.enqueue(400)
        val updateAthlete = UpdateAthlete(weight = 75.5f)
        val res = api.updateAthlete(updateAthlete = updateAthlete)

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Get activities`() = runTest {
        server.enqueue(code = 200, body = stravaActivitiesJson)

        val res = api.getActivities(perPage = 3)

        val expected = listOf(
            Activity(
                id = 1,
                name = "Happy Friday",
                distance = 7803.6,
                sportType = SportType.Run,
                startDateTime = LocalDateTime.of(2018, 5, 2, 5, 15, 9)
            ),
            Activity(
                id = 2,
                name = "Bondcliff",
                distance = 23676.5,
                sportType = SportType.Ride,
                startDateTime = LocalDateTime.of(2024, 10, 24, 20, 15, 30)
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
        assertThat(res.body()).isEqualTo(emptyList<Activity>())
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
        val updateActivity = UpdateActivity(
            name = "newName",
            description = "newDescription",
            commute = true,
            gearId = "gear-id"
        )
        val res = api.updateActivity(id = 1, updateActivity = updateActivity)

        assertThat(res.isSuccessful).isTrue()
        verify { authInterceptor.intercept(any()) }
    }

    @Test
    fun `Update activity - failure`() = runTest {
        server.enqueue(400)
        val updateActivity = UpdateActivity(
            name = "newName",
            description = "newDescription",
            commute = true,
            gearId = "gear-id"
        )
        val res = api.updateActivity(id = 1, updateActivity = updateActivity)

        assertThat(res.isSuccessful).isFalse()
        verify { authInterceptor.intercept(any()) }
    }
}