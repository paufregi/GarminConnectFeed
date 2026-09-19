package paufregi.connectfeed.data.repository

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import paufregi.connectfeed.MockServer
import paufregi.connectfeed.athlete
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.data.api.strava.models.Activity
import paufregi.connectfeed.data.api.strava.models.SportType
import paufregi.connectfeed.data.datastore.AuthStore
import paufregi.connectfeed.stravaAuthToken
import paufregi.connectfeed.stravaDispatcher
import paufregi.connectfeed.stravaPort
import javax.inject.Inject
import kotlin.time.Instant
import paufregi.connectfeed.core.models.Activity as CoreActivity

class StravaRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repo: StravaRepository

    @Inject
    lateinit var authStore: AuthStore

    @JvmField @Rule val stravaServer = MockServer.createSecure(stravaPort, stravaDispatcher)

    @Before
    fun setup() {
        hiltRule.inject()
        runBlocking(Dispatchers.IO) {
            authStore.saveStravaToken(stravaAuthToken)
        }
    }

    @After
    fun tearDown() {
        runBlocking(Dispatchers.IO){
            authStore.clear()
        }
    }

    @Test
    fun `Get athlete`() = runTest {
        val res = repo.getAthlete()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(athlete)
    }

    @Test
    fun `Get activities`() = runTest {
        val expected = listOf(
            Activity(
                id = 1,
                name = "Happy Friday",
                distance = 7804.0,
                sportType = SportType.Run,
                startDate = Instant.parse("2018-05-02T12:15:09Z")
            ),
            Activity(
                id = 2,
                name = "Bondcliff",
                distance = 23676.0,
                sportType = SportType.Ride,
                startDate = Instant.parse("2024-10-24T07:15:30Z")
            )
        )

        val res = repo.getActivities(5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
    }

    @Test
    fun `Get update athlete`() = runTest {
        val res = repo.updateAthlete(75.6f)

        assertThat(res.isSuccess).isTrue()
    }

    @Test
    fun `Update strava activity`() = runTest {
        val activity = CoreActivity(
            id = 1,
            name = "activity",
            type = ActivityType.Cycling,
            eventType = EventType.Training,
            date = Instant.parse("2024-10-24T07:15:30Z"),
            stravaId = 1
        )
        val name = "newName"
        val description = "description"
        val commute = true
        val gear = Gear("bike-1", "bike", GearType.Bike, 1000)

        val res = repo.updateActivity(
            activity = activity,
            name = name,
            description = description,
            commute = commute,
            gear = gear
        )

        assertThat(res.isSuccess).isTrue()
    }
}