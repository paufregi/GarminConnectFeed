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
import paufregi.connectfeed.data.api.strava.Strava
import paufregi.connectfeed.data.api.strava.models.ActivityDto
import paufregi.connectfeed.data.api.strava.models.UpdateActivityDto
import paufregi.connectfeed.data.api.strava.models.UpdateAthleteDto
import paufregi.connectfeed.data.repository.converters.toCore
import retrofit2.Response

class StravaRepositoryTest {

    private lateinit var repo: StravaRepository
    private val strava = mockk<Strava>()

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
            ActivityDto(
                id = 1,
                name = "activity_1",
                distance = 10234.00,
                sportType = "Run",
                startDate = "2018-05-02T12:15:09Z"
            ),
            ActivityDto(
                id = 2,
                name = "activity_2",
                distance = 17759.00,
                sportType = "Ride",
                startDate = "2018-04-30T12:35:51Z"
            )
        )
        coEvery { strava.getActivities(perPage = any()) } returns Response.success(activities)

        val expected = activities.map { it.toCore() }

        val res = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(expected)
        coVerify { strava.getActivities(perPage = 5) }
    }

    @Test
    fun `Get Strava activities - empty list`() = runTest {
        coEvery { strava.getActivities(perPage = any()) } returns Response.success(emptyList())

        val res = repo.getActivities(limit = 5)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(emptyList<Activity>())
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

        val expectedRequest = UpdateAthleteDto(weight = 75.9f)

        val res = repo.updateAthlete(weight = 75.9f)

        assertThat(res.isSuccess).isTrue()
        coVerify { strava.updateAthlete(expectedRequest) }
    }

    @Test
    fun `Update athlete - failure`() = runTest {
        coEvery { strava.updateAthlete(any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val expectedRequest = UpdateAthleteDto(weight = 75.9f)

        val res = repo.updateAthlete(weight = 75.9f)

        assertThat(res.isSuccess).isFalse()
        coVerify { strava.updateAthlete(expectedRequest) }
    }

    @Test
    fun `Update strava activity`() = runTest {
        coEvery { strava.updateActivity(any(), any()) } returns Response.success(Unit)

        val activity = Activity(
            id = 1,
            name = "activity",
            distance = 17803.00,
            type = ActivityType.Cycling
        )
        val name = "newName"
        val description = "newDescription"
        val commute = true

        val expectedRequest = UpdateActivityDto(
            name = name,
            description = description,
            commute = commute
        )

        val res = repo.updateActivity(
            activity = activity,
            name = name,
            description = description,
            commute = commute
        )

        assertThat(res.isSuccess).isTrue()
        coVerify { strava.updateActivity(activity.id, expectedRequest) }
    }

    @Test
    fun `Update strava activity - failure`() = runTest {
        coEvery { strava.updateActivity(any(), any()) } returns Response.error(400, "error".toResponseBody("text/plain; charset=UTF-8".toMediaType()))

        val activity = Activity(
            id = 1,
            name = "activity",
            distance = 17803.00,
            type = ActivityType.Cycling
        )
        val name = "newName"
        val description = "newDescription"
        val commute = true

        val expectedRequest = UpdateActivityDto(
            name = name,
            description = description,
            commute = commute
        )

        val res = repo.updateActivity(
            activity = activity,
            name = name,
            description = description,
            commute = commute
        )

        assertThat(res.isSuccess).isFalse()
        coVerify { strava.updateActivity(activity.id, expectedRequest) }
    }
}