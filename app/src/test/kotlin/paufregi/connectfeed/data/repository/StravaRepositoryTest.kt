package paufregi.connectfeed.data.repository

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.athlete
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.EventType
import paufregi.connectfeed.core.models.Gear
import paufregi.connectfeed.core.models.GearType
import paufregi.connectfeed.data.api.strava.Strava
import paufregi.connectfeed.data.api.strava.models.SportType
import paufregi.connectfeed.data.api.strava.models.UpdateActivity
import paufregi.connectfeed.data.api.strava.models.UpdateAthlete
import retrofit2.Response
import java.time.LocalDateTime
import paufregi.connectfeed.data.api.strava.models.Activity as StravaActivity

class StravaRepositoryTest {

    private lateinit var repo: StravaRepository
    private val strava = mockk<Strava>()

    val activity = Activity(
        id = 1,
        name = "activity",
        distance = 17803.00,
        type = ActivityType.Cycling,
        eventType = EventType.Training,
        date = LocalDateTime.of(2022, 5, 2, 12, 15, 9),
        stravaId = 10L
    )
    val gear = Gear(id = "gear-1", name = "gear 1", type = GearType.Bike, distance = 1000, stravaId = "strava-gear-id")

    @Before
    fun setup() {
        repo = StravaRepository(strava)
    }

    @After
    fun tearDown() {
        confirmVerified(strava)
        clearAllMocks()
    }

    @Test
    fun `Get athlete`() = runTest {
        coEvery { strava.getAthlete() } returns Response.success(athlete)

        val res = repo.getAthlete()

        assertThat(res.isSuccess).isTrue()
        coVerify { strava.getAthlete() }
    }

    @Test
    fun `Get athlete - failure`() = runTest {
        coEvery { strava.getAthlete() } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.getAthlete()

        assertThat(res.isSuccess).isFalse()
        coVerify { strava.getAthlete() }
    }

    @Test
    fun `Get activities`() = runTest {
        val activities = listOf(
            StravaActivity(
                id = 1,
                name = "activity_1",
                distance = 10234.00,
                sportType = SportType.Run,
                startDateTime = LocalDateTime.of(2018, 5, 2, 12, 15, 9)
            ),
            StravaActivity(
                id = 2,
                name = "activity_2",
                distance = 17759.00,
                sportType = SportType.Ride,
                startDateTime = LocalDateTime.of(2018, 4, 30, 12, 35, 51)
            )
        )
        coEvery { strava.getActivities(perPage = any()) } returns Response.success(activities)

        val res = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(activities)
        coVerify { strava.getActivities(perPage = 5) }
    }

    @Test
    fun `Get Strava activities - empty list`() = runTest {
        coEvery { strava.getActivities(perPage = any()) } returns Response.success(emptyList())

        val res = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<StravaActivity>())
        coVerify { strava.getActivities(perPage = 5) }
    }

    @Test
    fun `Get Strava activities - failure`() = runTest {
        coEvery { strava.getActivities(perPage = any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val res = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isFalse()
        coVerify { strava.getActivities(perPage = 5) }
    }

    @Test
    fun `Update athlete`() = runTest {
        coEvery { strava.updateAthlete(any()) } returns Response.success(Unit)

        val expectedRequest = UpdateAthlete(weight = 75.9f)

        val res = repo.updateAthlete(weight = 75.9f)

        assertThat(res.isSuccess).isTrue()
        coVerify { strava.updateAthlete(expectedRequest) }
    }

    @Test
    fun `Update athlete - failure`() = runTest {
        coEvery { strava.updateAthlete(any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val expectedRequest = UpdateAthlete(weight = 75.9f)

        val res = repo.updateAthlete(weight = 75.9f)

        assertThat(res.isSuccess).isFalse()
        coVerify { strava.updateAthlete(expectedRequest) }
    }

    @Test
    fun `Update activity`() = runTest {
        coEvery { strava.updateActivity(any(), any()) } returns Response.success(Unit)

        val name = "newName"
        val description = "newDescription"
        val commute = true


        val expectedRequest = UpdateActivity(
            name = name,
            description = description,
            commute = commute,
            gearId = gear.stravaId
        )

        val res = repo.updateActivity(
            activity = activity,
            name = name,
            description = description,
            commute = commute,
            gear = gear
        )

        assertThat(res.isSuccess).isTrue()
        coVerify { strava.updateActivity(activity.stravaId!!, expectedRequest) }
    }

    @Test
    fun `Update activity - no strava`() = runTest {
        coEvery { strava.updateActivity(any(), any()) } returns Response.success(Unit)

        val noStravaActivity = activity.copy(stravaId = null)
        val name = "newName"
        val description = "newDescription"
        val commute = true

        val expectedRequest = UpdateActivity(
            name = name,
            description = description,
            commute = commute,
            gearId = null
        )

        val res = repo.updateActivity(
            activity = noStravaActivity,
            name = name,
            description = description,
            commute = commute,
            gear = null
        )

        assertThat(res.isSuccess).isFalse()
    }

    @Test
    fun `Update activity - no gear`() = runTest {
        coEvery { strava.updateActivity(any(), any()) } returns Response.success(Unit)

        val name = "newName"
        val description = "newDescription"
        val commute = true

        val expectedRequest = UpdateActivity(
            name = name,
            description = description,
            commute = commute,
            gearId = null
        )

        val res = repo.updateActivity(
            activity = activity,
            name = name,
            description = description,
            commute = commute,
            gear = null
        )

        assertThat(res.isSuccess).isTrue()
        coVerify { strava.updateActivity(activity.stravaId!!, expectedRequest) }
    }

    @Test
    fun `Update activity - no strava gear`() = runTest {
        coEvery { strava.updateActivity(any(), any()) } returns Response.success(Unit)

        val name = "newName"
        val description = "newDescription"
        val commute = true
        val noStravaGear = gear.copy(stravaId = null)

        val expectedRequest = UpdateActivity(
            name = name,
            description = description,
            commute = commute,
            gearId = null
        )

        val res = repo.updateActivity(
            activity = activity,
            name = name,
            description = description,
            commute = commute,
            gear = noStravaGear
        )

        assertThat(res.isSuccess).isTrue()
        coVerify { strava.updateActivity(activity.stravaId!!, expectedRequest) }
    }

    @Test
    fun `Update activity - failure`() = runTest {
        coEvery { strava.updateActivity(any(), any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val id = 1L
        val name = "newName"
        val description = "newDescription"
        val commute = true

        val expectedRequest = UpdateActivity(
            name = name,
            description = description,
            commute = commute,
            gearId = gear.stravaId
        )

        val res = repo.updateActivity(
            activity = activity,
            name = name,
            description = description,
            commute = commute,
            gear = gear
        )

        assertThat(res.isSuccess).isFalse()
        coVerify { strava.updateActivity(activity.stravaId!!, expectedRequest) }
    }
}