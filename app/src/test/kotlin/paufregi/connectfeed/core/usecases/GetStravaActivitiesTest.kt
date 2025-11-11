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
import paufregi.connectfeed.core.utils.failure
import paufregi.connectfeed.data.repository.GarminRepository

class GetStravaActivitiesTest{
    private val repo = mockk<GarminRepository>()
    private val strava = mockk<IsStravaLoggedIn>()
    private lateinit var useCase: GetStravaActivities

    @Before
    fun setup(){
        useCase = GetStravaActivities(repo, strava)
    }

    @After
    fun tearDown(){
        confirmVerified(repo, strava)
        clearAllMocks()
    }

    @Test
    fun `Get activities`() = runTest {
        val activities = listOf(
            Activity(id = 1, name = "name", distance = 10234.00, type = ActivityType.Running)
        )
        every { strava() } returns flowOf(true)
        coEvery { repo.getStravaActivities(any(), any()) } returns Result.success(activities)
        val res = useCase(true)

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEqualTo(activities)

        verify { strava() }
        coVerify { repo.getStravaActivities(5, true) }
    }

    @Test
    fun `Get activities - failed`() = runTest {
        every { strava() } returns flowOf(true)
        coEvery { repo.getStravaActivities(any(), any()) } returns Result.failure("Failed")
        val res = useCase()

        assertThat(res.isSuccess).isFalse()

        verify { strava() }
        coVerify { repo.getStravaActivities(5, false) }
    }

    @Test
    fun `Get activities - not logged in strava`() = runTest {
        every { strava() } returns flowOf(false)
        val res = useCase()

        assertThat(res.isSuccess).isTrue()
        assertThat(res.getOrNull()).isEmpty()

        verify { strava() }
        assertThat(res.isSuccess).isTrue()
    }
}