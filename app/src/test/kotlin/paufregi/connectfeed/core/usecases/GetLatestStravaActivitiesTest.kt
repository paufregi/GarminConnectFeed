package paufregi.connectfeed.core.usecases

import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import paufregi.connectfeed.core.models.Activity
import paufregi.connectfeed.core.models.ActivityType
import paufregi.connectfeed.core.models.Result
import paufregi.connectfeed.data.repository.GarminRepository

class GetLatestStravaActivitiesTest{
    private val repo = mockk<GarminRepository>()
    private val strava = mockk<IsStravaLoggedIn>()
    private lateinit var useCase: GetLatestStravaActivities

    @Before
    fun setup(){
        useCase = GetLatestStravaActivities(repo, strava)
    }

    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `Get latest activities`() = runTest {
        val activities = listOf(
            Activity(id = 1, name = "name", distance = 10234.00, type = ActivityType.Running)
        )
        every { strava() } returns flowOf(true)
        coEvery { repo.getLatestStravaActivities(any()) } returns Result.Success(activities)
        val res = useCase()

        assertThat(res.isSuccessful).isTrue()
        res as Result.Success
        assertThat(res.data).isEqualTo(activities)

        verify { strava() }
        coVerify { repo.getLatestStravaActivities(5) }
        confirmVerified(repo, strava)
    }

    @Test
    fun `Get latest activities - failed`() = runTest {
        every { strava() } returns flowOf(true)
        coEvery { repo.getLatestStravaActivities(any()) } returns Result.Failure("Failed")
        val res = useCase()

        assertThat(res.isSuccessful).isFalse()

        verify { strava() }
        coVerify { repo.getLatestStravaActivities(5) }
        confirmVerified(repo, strava)
    }

    @Test
    fun `Get latest activities - not logged in strava`() = runTest {
        every { strava() } returns flowOf(false)
        val res = useCase()

        assertThat(res.isSuccessful).isTrue()
        res as Result.Success
        assertThat(res.data).isEmpty()

        verify { strava() }
        assertThat(res.isSuccessful).isTrue()
        confirmVerified(repo, strava)
    }
}